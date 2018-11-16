package com.example.android.quizzy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.quizzy.R;
import com.example.android.quizzy.adapter.QuestionQuizAdapter;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.model.Question;
import com.example.android.quizzy.model.Quiz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * now handle case is to solve a non-attemped
 */
public class QuizzQuestion extends AppCompatActivity {

    private static final String TAG = "QuizzQuestion";
    @BindView(R.id.rvQuizzQuestionList)
    RecyclerView rvQuizzQuestionList;
    DataRepo repo;
    Quiz quiz;
    private QuestionQuizAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_question);
        ButterKnife.bind(this);

/*
        Bundle bundle = getIntent().getBundleExtra("quiz");
        QuizSeriazle quizSeriazle = (QuizSeriazle) bundle.getSerializable("quiz");
*/

        String quizeID = getIntent().getStringExtra("id");
        String teacher = getIntent().getStringExtra("teacher");

        repo = new DataRepo();
        repo.getSpecificQuizRef(teacher, quizeID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: " + dataSnapshot);
                quiz = dataSnapshot.getValue(Quiz.class);
                setQuestionList(quiz.getQuestionList());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        initRv();

    }

    private void setQuestionList(List<Question> questionList) {
        adapter.setList(questionList);
    }


    private void initRv() {
        adapter = new QuestionQuizAdapter();
        rvQuizzQuestionList.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvQuizzQuestionList.setLayoutManager(manager);
    }


}
