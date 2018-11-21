package com.example.android.quizzy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.adapter.QuestionQuizAdapter;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.model.Question;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * now handle case is to solve a non-attemped
 */
public class QuizzQuestion extends AppCompatActivity {

    private static final String TAG = "QuizzQuestion";
    @BindView(R.id.rvQuizzQuestionList)
    RecyclerView rvQuizzQuestionList;
    DataRepo repo;
    Quiz quiz;
    @BindView(R.id.quizzSubmit)
    Button quizzSubmit;
    private QuestionQuizAdapter adapter;


    String sID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_question);
        ButterKnife.bind(this);

/*
        Bundle bundle = getIntent().getBundleExtra("quiz");
        QuizSeriazle quizSeriazle = (QuizSeriazle) bundle.getSerializable("quiz");
*/

        sID = getIntent().getStringExtra("sID");
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


    @OnClick(R.id.quizzSubmit)
    public void onViewClicked() {
        List<Question> questionList = adapter.getList();
        int score = 0;
        for (Question question : questionList) {
            Log.d(TAG, "onViewClicked: 1 " + question.getStudentAnswer());
            Log.d(TAG, "onViewClicked: 2 " + question.getCorrectAnswer());
            if (question.getStudentAnswer().equals(question.getCorrectAnswer())) {
                question.setState(true);
                score++;
            }
        }
        Log.d(TAG, "score: " + score);

        quiz.setQuestionList(questionList);
        quiz.setScore(score);
        int percentage = (score / quiz.getQuestionList().size()) * 100;
        quiz.setPercentage(percentage);
        if (percentage < 50) {
            quiz.setGrade(Constants.FAILED);
        } else if (percentage < 65) {
            quiz.setGrade(Constants.ACCEPT);
        } else if (percentage < 75) {
            quiz.setGrade(Constants.GOOD);
        } else if (percentage < 85) {
            quiz.setGrade(Constants.VGOOD);
        } else {
            quiz.setGrade(Constants.Excellent);
        }

        repo.addQuizTOCompleteList(quiz, sID);

        show("Quizz Solved ");
        finish();
    }

    private void show(String quizz_solved_) {
        Toast.makeText(this, quizz_solved_, Toast.LENGTH_SHORT).show();
    }
}
