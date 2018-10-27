package com.example.android.quizzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.adapter.QuestionListAdapterAddQuiz;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.fragment.QuestionAddFragment;
import com.example.android.quizzy.interfaces.onQuestionAdd;
import com.example.android.quizzy.model.Question;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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

    private String teacherKey;
    private DataRepo dataRepo;

    private String quizzKey;
    private boolean isUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_detali_teacher);
        ButterKnife.bind(this);

        dataRepo = new DataRepo();
        Intent intent = getIntent();
        teacherKey = intent.getStringExtra("t_key");
        if (teacherKey != null) {
            quizzKey = intent.getStringExtra("q_key");
            if (quizzKey != null) {
                isUpdate = true;
                fetchQustionList();
            }
        } else {
            finish();
        }

        initRv();
        replaceQuestionFragment();
    }

    private static final String TAG = "AddEditQuiz";
    private void fetchQustionList() {
        dataRepo.getSpecificQuizRef(teacherKey, quizzKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                edQuizName.setText(name);
                Log.d(TAG, "onDataChange:  1 " + dataSnapshot);
                Question question;
                questionList = new ArrayList<>();
                List<String> strings = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.child(Constants.QUIZZ_QUESTION_LIST).getChildren()) {
                    question = getQuestionFromJson(snapshot);
                    Log.d(TAG, "onDataChange: 2 " + snapshot);
                    if (question != null) {
                        questionList.add(question);
                        Log.d(TAG, "onDataChange: " + question.toString());
                    }
                }
                if (questionList.size() > 0) {
                    adapter.setQuestionList(questionList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Question getQuestionFromJson(DataSnapshot snapshot) {
        Question question = new Question();
        question.setQuestion((String) snapshot.child("question").getValue());
        question.setCorrectAnswer((String) snapshot.child("correctAnswer").getValue());
        List<String> ansewrs = new ArrayList<>();
        for (DataSnapshot snapshot1 : snapshot.child(Constants.Question_answer_list).getChildren()) {
            ansewrs.add((String) snapshot1.getValue());
        }
        question.setAnswerList(ansewrs);
        return question;
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                Question question = questionList.get(pos);
                if (direction == ItemTouchHelper.RIGHT) {
                    adapter.remove(pos);
                    questionList.remove(pos);
                    replaceQuestionFragment(question);
                } else {
                    questionList.remove(question);
                    adapter.remove(pos);
                }
            }
        }).attachToRecyclerView(rvQustionListTeacher);
    }

    private void replaceQuestionFragment(Question question) {
        transaction = manager.beginTransaction();
        QuestionAddFragment fragment = new QuestionAddFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("q", question);
        fragment.setArguments(bundle);
        transaction.replace(R.id.questionContainer, fragment).commit();
    }

    @OnClick(R.id.btnSaveQuizz)
    public void onViewClicked() {
        String quizName = edQuizName.getText().toString();
        if (!TextUtils.isEmpty(quizName)) {
            if (questionList.size() > 0) {
                Quiz quiz = new Quiz();
                quiz.setName(quizName);
                quiz.setQuestionList(questionList);
                quiz.setTeacherKey(teacherKey);
                //// TODO: 10/23/2018 add weight/total score
                if (isUpdate) {
                    quiz.setKey(quizzKey);
                }
                dataRepo.addQuiz(quiz);
                show("added");
                blankFields();
            } else {
                show("Please add Question");
            }
        } else {
            show("Enter Quiz Name");
        }
    }

    private void blankFields() {
        questionList.clear();
        adapter.clear();
        edQuizName.setText("");
    }

    private void show(String a) {
        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQuestionAdd(Question question) {
        if (question.getAnswerList().size() > 0) {
            questionList.add(question);
            adapter.addQuestion(question);
        }
    }

}
