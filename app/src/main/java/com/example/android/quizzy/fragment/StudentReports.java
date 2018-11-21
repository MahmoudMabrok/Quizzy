package com.example.android.quizzy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.quizzy.R;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentReports extends Fragment {

    private static final String TAG = "StudentReports";
    @BindView(R.id.piQuizzStudent)
    PieChart piQuizzStudent;
    @BindView(R.id.piQuizzStudentGrades)
    PieChart piQuizzStudentGrades;
    @BindView(R.id.tvAverageValueStudent)
    TextView tvAverageValueStudent;
    @BindView(R.id.tvmaxValueStudent)
    TextView tvmaxValueStudent;
    @BindView(R.id.tvminValueStudent)
    TextView tvminValueStudent;
    Unbinder unbinder;
    private ArrayList<Quiz> completedList = new ArrayList<>();
    private DataRepo dataRepo;
    private ArrayList<Quiz> quizList = new ArrayList<>();

    public StudentReports() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_reports, container, false);
        unbinder = ButterKnife.bind(this, view);
        dataRepo = new DataRepo();
        retriveQuizzList("0114919427");
        retriveCompletedList("-mahmoud");

        Log.d(TAG, "onCreateView: ");
        computeDistributionQuizzes();
        computeDistributionGrades();
        comptueParamter();

        return view;
    }

    private void comptueParamter() {
        int max = 0, min = 0;
        float avg = 0;
        ArrayList<Integer> list = new ArrayList<>();
        for (Quiz quiz : completedList) {
            list.add(quiz.getPercentage());
            avg += quiz.getPercentage();
        }
        Log.d(TAG, "comptueParamter: " + list.size());
        if (list.size() > 0) {
            max = Collections.max(list);
            Log.d(TAG, "comptueParamter:  max " + max);
            min = Collections.min(list);
            avg = (float) (avg / list.size() * 1.0);
        }

        if (tvAverageValueStudent != null) {
            tvAverageValueStudent.setText(avg + " %");
            tvmaxValueStudent.setText(max + " %");
            tvminValueStudent.setText(min + " %");
        }

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
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }

    private void retriveCompletedList(String studentUUID) {
        dataRepo.getCompleteListRef("0114919427", studentUUID).addValueEventListener(new ValueEventListener() {
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
                computeDistributionGrades();
                computeDistributionQuizzes();
                comptueParamter();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void computeDistributionGrades() {
        String[] labels = new String[]{"Excellent", "VGood", "Good", "Accept", "Kahka"};
        int execellent = 0, vgood = 0, good = 0, accept = 0, fail = 0;
        for (Quiz quiz : completedList) {
            switch (quiz.getGrade()) {
                case Constants.FAILED:
                    fail++;
                    break;
                case Constants.ACCEPT:
                    accept++;
                    break;
                case Constants.GOOD:
                    good++;
                    break;
                case Constants.VGOOD:
                    vgood++;
                    break;

                case Constants.Excellent:
                    execellent++;
                    break;
            }
        }
        int[] values = new int[]{execellent, vgood, good, accept, fail};
        /* int[] values = new int[]{10, 15, 25, 14, 2};
         */

        PieData pieData = getPieDataFromEntries("Quizzes", values, labels);
        if (piQuizzStudentGrades != null && values.length > 0) {
            piQuizzStudentGrades.setData(pieData);
            piQuizzStudentGrades.invalidate();
        }

    }

    private void computeDistributionQuizzes() {
        String[] labels = new String[]{"Success", "Failed", "Not Attemped"};
        int success = 0, falid = 0, na;
        for (Quiz quiz : completedList) {
            if (quiz.getScore() >= quiz.getQuestionList().size() / 2)
                success++;
            else
                falid++;
        }
        na = quizList.size() - (success + falid);
        int[] values = new int[]{success, falid, na};
        // int[] values = new int[]{10, 15, 25};
        PieData pieData = getPieDataFromEntries("Quizzes", values, labels);
        if (piQuizzStudent != null && values.length > 0) {
            piQuizzStudent.setData(pieData);
            piQuizzStudent.invalidate();
        }
    }

    private PieData getPieDataFromEntries(String label, int[] values, String[] labels) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            pieEntries.add(new PieEntry(values[i], labels[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, label);
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(15f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(15f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        dataSet.setColors(colors);


        return new PieData(dataSet);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
