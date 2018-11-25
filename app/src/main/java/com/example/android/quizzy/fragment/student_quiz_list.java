package com.example.android.quizzy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.activity.QuizzQuestion;
import com.example.android.quizzy.adapter.QuizeListStudentAdapter;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.interfaces.OnQuizzClick;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class student_quiz_list extends Fragment implements OnQuizzClick {

    private static final String TAG = "student_quiz_list";
    @BindView(R.id.rvQuizListStudent)
    RecyclerView rvQuizListStudent;
    Unbinder unbinder;
    DataRepo dataRepo = new DataRepo();
    QuizeListStudentAdapter adapter;
    private String teacherID;
    private List<Quiz> completedList = new ArrayList<>();
    private List<Quiz> quizList = new ArrayList<>();
    private String studentName;

    public student_quiz_list() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teacherID = "0114919427";
        if (getArguments() != null) {
            teacherID = getArguments().getString(Constants.TEACHERS_KEY);
            show("t_id " + teacherID);
            studentName = getArguments().getString(Constants.STUDENT_NAME);
            show("s_name " + studentName);
        }
    }

    String studentUUID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_quiz_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        // studentUUID = dataRepo.getUUID();
        studentUUID = "0";

        Log.d(TAG, "id " + studentUUID + " teacher " + teacherID);
        initRv();
        retriveCompletedList(studentUUID);
        retriveQuizzList(teacherID);

        return view;
    }

    private void retriveQuizzList(String teacherID) {

        //region fetch data
        FirebaseDatabase.getInstance().
                getReference(Constants.USERS_KEY)
                .child(Constants.TEACHERS_KEY)
                .child(teacherID)
                .child(Constants.QUIZZ_CHILD)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: " + dataSnapshot);
                        List<Quiz> list = new ArrayList<>();
                        Quiz temp;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            temp = snapshot.getValue(Quiz.class);
                            if (temp != null) {
                                list.add(temp);
                            }
                        }
                        if (list.size() > 0) {
                            quizList = new ArrayList<>(list);
                            adapter.setList(list, completedList);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    private void retriveCompletedList(String studentUUID) {
        dataRepo.getCompleteListRef(teacherID, studentUUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange:  completeList" + dataSnapshot);
                List<Quiz> list = new ArrayList<>();
                Quiz temp;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    temp = snapshot.getValue(Quiz.class);
                    if (temp != null) {
                        list.add(temp);
                    }
                }
                completedList = new ArrayList<>(list);
                adapter.addCompleteList(completedList);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initRv() {
        adapter = new QuizeListStudentAdapter(getContext(), this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvQuizListStudent.setLayoutManager(manager);
        rvQuizListStudent.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onQuizzClick(Quiz quiz) {
        Intent intent = new Intent(getContext(), QuizzQuestion.class);

        intent.putExtra("sID", studentUUID);
        intent.putExtra("id", quiz.getKey());
        if (quiz.getQuestionList().get(0).getStudentAnswer() != null) {
            show("Completed ");
            intent.putExtra("s", true);

        }
        intent.putExtra(Constants.STUDENT_NAME, studentName);
        show("in intent " + studentName);
        intent.putExtra(Constants.TEACHERS_KEY, teacherID);
        getContext().startActivity(intent);
    }

    private void show(String name) {
        Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
    }
}
