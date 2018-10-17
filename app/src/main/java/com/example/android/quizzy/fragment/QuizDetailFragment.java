package com.example.android.quizzy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.quizzy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizDetailFragment extends Fragment {


    public QuizDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz_detail, container, false);

        return view;
    }

}
