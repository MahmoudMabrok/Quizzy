package com.example.android.quizzy.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.model.AttemptedQuiz;
import com.example.android.quizzy.model.Data;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsTeacherFragment extends Fragment {


    @BindView(R.id.barTeacherQuizzes)
    BarChart barTeacherQuizzes;
    @BindView(R.id.barTeacherQuizzesofStudents)
    BarChart barTeacherQuizzesofStudents;
    Unbinder unbinder;

    public ReportsTeacherFragment() {
        // Required empty public constructor
    }

    String teacherKey;
    ArrayList<List<BarEntry>> listOfListEntries = new ArrayList<>();
    private DataRepo repo = new DataRepo();
    List<Data> dataList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teacherKey = getArguments().getString(Constants.TEACHERS_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports_teacher, container, false);
        unbinder = ButterKnife.bind(this, view);
        start(teacherKey);

        return view;
    }

    private static final String TAG = "ReportsTeacherFragment";

    private void start(final String teacherKey) {
        repo.getTeacherQuizz(teacherKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList = new ArrayList<>();
                Data data;
                List<AttemptedQuiz> list;
                AttemptedQuiz attemptedQuiz;
                for (DataSnapshot quiz : dataSnapshot.getChildren()) { // for each quizz
                    data = new Data();
                    data.setQuizName((String) quiz.child(Constants.QUIZZ_NAME).getValue());
                    Log.d(TAG, "onDataChange: Quizz name " + data.getQuizName());
                    list = new ArrayList<>();
                    for (DataSnapshot iteraateAttemp : quiz.child(Constants.AttemptedList).getChildren()) {
                        attemptedQuiz = iteraateAttemp.getValue(AttemptedQuiz.class);
                        Log.d(TAG, "onDataChange: " + attemptedQuiz);
                        list.add(attemptedQuiz);
                    }

                    data.setAttemptedQuizList(list);
                    dataList.add(data);
                }
                Log.d(TAG, "onDataChange: final size " + dataList.size());
                // startQuizAnalysis();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void startQuizAnalysis() {

    }

    private void show(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
