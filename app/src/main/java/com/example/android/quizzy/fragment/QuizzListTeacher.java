package com.example.android.quizzy.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.quizzy.R;
import com.example.android.quizzy.activity.AddEditQuiz;
import com.example.android.quizzy.activity.TeacherHome;
import com.example.android.quizzy.adapter.QuizeListTeacherAdapter;
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
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizzListTeacher extends Fragment implements OnQuizzClick {


    @BindView(R.id.rvQuizListTeacher)
    RecyclerView rvQuizListTeacher;
    Unbinder unbinder;
    @BindView(R.id.fabAddQuizz)
    FloatingActionButton fabAddQuizz;
    @BindView(R.id.tvNoInternet)
    TextView tvNoInternet;
    private QuizeListTeacherAdapter adapter;

    private static final String TAG = "QuizzListTeacher";

    public QuizzListTeacher() {
        // Required empty public constructor
    }

    private String teacherKey;
    private FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quizz_list_teacher, container, false);
        unbinder = ButterKnife.bind(this, view);
        teacherKey = getArguments().getString(Constants.TEACHERS_KEY);
        initRv();

        ((TeacherHome) getActivity()).name = "DDD";
        return view;
    }

    private void initRv() {
        adapter = new QuizeListTeacherAdapter(getContext(), this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvQuizListTeacher.setAdapter(adapter);
        rvQuizListTeacher.setLayoutManager(manager);
        controlTextView(true);
        database = FirebaseDatabase.getInstance();

        //region fetch data
        FirebaseDatabase.getInstance().
                getReference(Constants.USERS_KEY)
                .child(Constants.TEACHERS_KEY)
                .child(teacherKey)
                .child(Constants.QUIZZ_CHILD)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        controlTextView(false);
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
                            adapter.setList(list);
                        } else {
                            makeNoItem();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        //endregion
    }

    private void makeNoItem() {
        //  tvNoInternet.setText(getString(R.strings.no_data_found));
    }


    private void controlTextView(boolean b) {
        if (tvNoInternet != null) {
            if (b) {
                tvNoInternet.setVisibility(View.VISIBLE);
            } else {
                tvNoInternet.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onQuizzClick(Quiz quiz) {
        Intent view = new Intent(getContext(), AddEditQuiz.class);
        view.putExtra("t_key", teacherKey);
        view.putExtra("q_key", quiz.getKey());
        startActivity(view);
    }

    @OnClick(R.id.fabAddQuizz)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), AddEditQuiz.class);
        intent.putExtra("t_key", teacherKey);
        startActivity(intent);
      /*  // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent,  ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        } else {
            startActivity(intent );
        }*/


    }
}
