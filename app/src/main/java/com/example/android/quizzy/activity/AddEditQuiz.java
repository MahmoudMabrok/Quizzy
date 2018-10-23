package com.example.android.quizzy.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.adapter.QuestionListAdapterAddQuiz;
import com.example.android.quizzy.fragment.QuestionAddFragment;
import com.example.android.quizzy.interfaces.onQuestionAdd;
import com.example.android.quizzy.model.Question;
import com.example.android.quizzy.model.Quiz;

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
public class AddEditQuiz extends AppCompatActivity implements onQuestionAdd {

    @BindView(R.id.edQuizName)
    EditText edQuizName;
    @BindView(R.id.questionContainer)
    LinearLayout questionContainer;
    @BindView(R.id.rvQustionListTeacher)
    RecyclerView rvQustionListTeacher;
    @BindView(R.id.btnSaveQuizz)
    Button btnSaveQuizz;


    private List<Question> questionList = new ArrayList<>();
    private FragmentManager manager = getSupportFragmentManager();
    private FragmentTransaction transaction;
    private QuestionListAdapterAddQuiz adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_detali_teacher);
        ButterKnife.bind(this);

        initRv();
        replaceQuestionFragment();

    }

    private void replaceQuestionFragment() {
        transaction = manager.beginTransaction();
        QuestionAddFragment fragment = new QuestionAddFragment();
        transaction.replace(R.id.questionContainer, fragment).commit();
    }

    private void initRv() {
        adapter = new QuestionListAdapterAddQuiz();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvQustionListTeacher.setLayoutManager(manager);
        rvQustionListTeacher.setAdapter(adapter);
    }

    @OnClick(R.id.btnSaveQuizz)
    public void onViewClicked() {
        String quizName = edQuizName.getText().toString();
        if (!TextUtils.isEmpty(quizName)) {
            if (questionList.size() > 0) {
                Quiz quiz = new Quiz();
                quiz.setName(quizName);
                quiz.setQuestionList(questionList);
                //// TODO: 10/23/2018 add weight/total score
                //// TODO: 10/23/2018 api
            } else {
                show("Please add Question");
            }
        } else {
            show("Enter Quiz Name");
        }
    }

    private void show(String a) {
        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQuestionAdd(Question question) {
        questionList.add(question);
        adapter.addQuestion(question);
    }

}
