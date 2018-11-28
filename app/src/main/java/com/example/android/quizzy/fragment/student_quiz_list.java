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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.activity.QuizzQuestion;
import com.example.android.quizzy.adapter.QuizeListStudentAdapter;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.api.FirebaseDataSource;
import com.example.android.quizzy.interfaces.OnQuizzClick;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
    @BindView(R.id.pbLoadingquizz)
    ProgressBar pbLoadingquizz;
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
        show("aaa " + teacherID);
        dataRepo.getTeacherQuizz(teacherID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Quiz temp;
                quizList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d(TAG, "@2 onDataChange: " + dataSnapshot1);
                    temp = dataSnapshot1.getValue(Quiz.class);
                    if (temp != null && temp.getName() != null) {
                        quizList.add(temp);
                    }
                }
                show("quiz size " + quizList.size());
                pbLoadingquizz.setVisibility(View.GONE);

                if (quizList.size() > 0 && completedList != null) {
                    adapter.setList(quizList, completedList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference(Constants.USERS_KEY)
                .child(Constants.TEACHERS_KEY)
                .child(teacherID)
                .child("quiz")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d(TAG, "onChildAdded: " + dataSnapshot);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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
