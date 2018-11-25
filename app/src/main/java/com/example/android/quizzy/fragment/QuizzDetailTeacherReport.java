package com.example.android.quizzy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.quizzy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizzDetailTeacherReport extends Fragment {


    @BindView(R.id.quizzNameDetailReport)
    TextView quizzNameDetailReport;
    @BindView(R.id.tvquizSuccess)
    TextView tvquizSuccess;
    @BindView(R.id.tvquizFailed)
    TextView tvquizFailed;
    @BindView(R.id.tvquizNA)
    TextView tvquizNA;
    @BindView(R.id.rvReportStudentDetailTeacher)
    RecyclerView rvReportStudentDetailTeacher;
    Unbinder unbinder;

    public QuizzDetailTeacherReport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quizz_detail_teacher_report, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
