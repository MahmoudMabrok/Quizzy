package com.example.android.quizzy.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.android.quizzy.R;
import com.example.android.quizzy.model.Question;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * add and edit quiz created by teacher
 * Input: is key of teacher to add quiz in its child node
 * EditState: when accept quiz key so it be edited
 */
public class AddEditQuiz extends AppCompatActivity {

    @BindView(R.id.edQuizName)
    EditText edQuizName;
    @BindView(R.id.questionContainer)
    LinearLayout questionContainer;
    @BindView(R.id.rvQustionListTeacher)
    RecyclerView rvQustionListTeacher;
    @BindView(R.id.btnSaveQuizz)
    Button btnSaveQuizz;

    private List<Question> questionList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_detali_teacher);
        ButterKnife.bind(this);

        initRv();


    }

    private void initRv() {
    }

    @OnClick(R.id.btnSaveQuizz)
    public void onViewClicked() {
    }
}
