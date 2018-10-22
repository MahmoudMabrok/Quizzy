package com.example.android.quizzy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.quizzy.R;
import com.example.android.quizzy.adapter.QuizeListTeacherAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizzListTeacher extends Fragment {


    @BindView(R.id.rvQuizListTeacher)
    RecyclerView rvQuizListTeacher;
    Unbinder unbinder;
    private QuizeListTeacherAdapter adapter;


    public QuizzListTeacher() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quizz_list_teacher, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRv();
        return view;
    }

    private void initRv() {
        adapter = new QuizeListTeacherAdapter(getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvQuizListTeacher.setAdapter(adapter);
        rvQuizListTeacher.setLayoutManager(manager);

        FirebaseDatabase.getInstance().
                getReference(Constants.USERS_KEY).child(Constants.TEACHERS_KEY).child(Constants.QUIZZ_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
