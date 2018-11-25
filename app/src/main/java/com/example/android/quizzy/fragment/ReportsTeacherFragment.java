package com.example.android.quizzy.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.adapter.ReportQuizzesTeacherAdapter;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.model.AttemptedQuiz;
import com.example.android.quizzy.model.Data;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.model.ReportQuizzItem;
import com.example.android.quizzy.util.Constants;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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


    Unbinder unbinder;
    @BindView(R.id.rvReportQuiezzTeacher)
    RecyclerView rvReportQuiezzTeacher;
    @BindView(R.id.rvReportStudentsGradesTeacherFragment)
    RecyclerView rvReportStudentsGradesTeacherFragment;
    private long totalStudentNumber;
    private int n_quizzes;

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
        initRv();
        retriveTotalNStudents();

        return view;
    }

    private void retriveTotalNStudents() {
        repo.getStudentOfTeacherRef(teacherKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalStudentNumber = dataSnapshot.getChildrenCount();
                show("n students" + totalStudentNumber);
                start(teacherKey);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initRv() {
        adapter = new ReportQuizzesTeacherAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvReportQuiezzTeacher.setAdapter(adapter);
        rvReportQuiezzTeacher.setLayoutManager(manager);


        studentAdapter = new ReportQuizzesTeacherAdapter();
        LinearLayoutManager manager1 = new LinearLayoutManager(getContext());
        rvReportStudentsGradesTeacherFragment.setAdapter(studentAdapter);
        rvReportStudentsGradesTeacherFragment.setLayoutManager(manager1);

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
                    labels.add(data.getQuizName());
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
                if (barTeacherQuizzes != null) {
                    startQuizAnalysis();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private BarDataSet failsSet;
    private BarDataSet successSet;
    private BarDataSet naSet;

    private List<BarEntry> failsEntries = new ArrayList<>();
    private List<BarEntry> successEntries = new ArrayList<>();
    private List<BarEntry> naEntries = new ArrayList<>();

    List<String> labels = new ArrayList<>();

    private ReportQuizzesTeacherAdapter adapter, studentAdapter;

    private void startQuizAnalysis() {
      /*  for(String a : labels){
            show(a);
        }*/
        int fails = 0, success = 0, na = 0;
        int xValue = 1;

        HashMap<Integer, StringBuilder> grades;
        // (1) -> analysis data and get Entries
        for (Data data : dataList) { //for each quizz
            fails = success = na = 0;
            grades = new HashMap<>();
            for (AttemptedQuiz attemptedQuiz : data.getAttemptedQuizList()) { //for each attemp (student solution)
                if (grades.containsKey(attemptedQuiz.getGrade())) {
                    StringBuilder builder = grades.get(attemptedQuiz.getGrade()).append("\n").append(attemptedQuiz.getStudentName());
                    grades.put(attemptedQuiz.getGrade(), builder);
                } else {
                    grades.put(attemptedQuiz.getGrade(), new StringBuilder(attemptedQuiz.getStudentName()));
                }
                if (attemptedQuiz.getGrade() > Constants.FAILED) {
                    success++;
                } else {
                    fails++;
                }
            }
         /*   int max = Collections.max(grades.keySet());
            show("" + max);

            if (max >= 0 ){

            }*/
            na = (int) (totalStudentNumber - (fails + success));
            na = na >= 0 ? na : 0;
/*
            failsEntries.add(new BarEntry(xValue, fails));
            successEntries.add(new BarEntry(xValue, success));
            naEntries.add(new BarEntry(xValue, na));
*/

            adapter.add(new ReportQuizzItem(fails, success, na, labels.get(xValue - 1)));

            xValue++; // represnet xAxix for barEntry

            Log.d(TAG, "startQuizAnalysis: " + fails + " " + success + " " + na);
        }


        n_quizzes = dataList.size();
        // (2) -> Create  DataSet
        failsSet = new BarDataSet(failsEntries, "Failed");
        failsSet.setColor(Color.RED);

        successSet = new BarDataSet(successEntries, "Success");
        successSet.setColor(Color.GREEN);

        naSet = new BarDataSet(naEntries, "NA");
        naSet.setColor(Color.GRAY);
        BarData barData = new BarData(failsSet, successSet, naSet);
        //        BarData barData = new BarData(failsSet );
        barTeacherQuizzes.setData(barData);
        // barTeacherQuizzes.invalidate();

        // (3) styles for barChart

        XAxis xAxis = barTeacherQuizzes.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        barTeacherQuizzes.getAxisLeft().setAxisMinimum(0);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularityEnabled(true);

        float barSpace = 0.04f;
        float groupSpace = 0.5f;
        //  int groupCount = 4;
        int groupCount = dataList.size();

        barData.setBarWidth(0.15f);

        barTeacherQuizzes.setScaleYEnabled(true);
        barTeacherQuizzes.getXAxis().setAxisMinimum(0);
        barTeacherQuizzes.getXAxis().setAxisMaximum(0 + barTeacherQuizzes.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        barTeacherQuizzes.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
        barTeacherQuizzes.invalidate();

        startStudnetAnalysis();

    }

    private void startStudnetAnalysis() {
        show("n quizzes " + n_quizzes);
        retrieveCompleteList();
    }

    private void retrieveCompleteList() {
        repo.getStudentOfTeacherRef(teacherKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange:  complete" + dataSnapshot);
                ReportQuizzItem item;
                Quiz quiz;

                String name;
                int fails = 0, success = 0, na = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    name = (String) dataSnapshot1.child("name").getValue();
                    Log.d(TAG, "onDataChange:  student " + dataSnapshot1);
                    fails = success = na = 0;
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.child(Constants.COMPLETED_QUIZZ).getChildren()) {
                        // sa
                        quiz = dataSnapshot2.getValue(Quiz.class);
                        if (quiz != null) {
                            if (quiz.getGrade() > Constants.FAILED) {
                                success++;
                            } else {
                                fails++;
                            }
                        }
                    }
                    na = n_quizzes - (fails + success);
                    na = na >= 0 ? na : 0;

                    if (rvReportStudentsGradesTeacherFragment != null) {
                        studentAdapter.add(new ReportQuizzItem(fails, success, na, name));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*    repo.getCompleteListRef("0114919427", studentUUID).addValueEventListener(new ValueEventListener() {
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
                    Log.d(TAG, "size of complete: " + completedList.size());



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        */

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
