package com.example.android.quizzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.adapter.QuestionListAdapterAddQuiz;
import com.example.android.quizzy.api.DataRepo;
import com.example.android.quizzy.fragment.QuestionAddFragment;
import com.example.android.quizzy.interfaces.OnQuestionEdit;
import com.example.android.quizzy.interfaces.onQuestionAdd;
import com.example.android.quizzy.model.Question;
import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
public class AddEditQuiz extends AppCompatActivity implements onQuestionAdd, OnQuestionEdit {

    @BindView(R.id.edQuizName)
    EditText edQuizName;
    @BindView(R.id.questionContainer)
    LinearLayout questionContainer;
    @BindView(R.id.rvQustionListTeacher)
    RecyclerView rvQustionListTeacher;
    @BindView(R.id.btnSaveQuizz)
    Button btnSaveQuizz;
    @BindView(R.id.fabAddQuestion)
    FloatingActionButton fabAddQuestion;
    @BindView(R.id.quizzAndAddLayout)
    RelativeLayout quizzAndAddLayout;

    private List<Question> questionList = new ArrayList<>();
    private FragmentManager manager = getSupportFragmentManager();
    private FragmentTransaction transaction;
    private QuestionListAdapterAddQuiz adapter;

    private String teacherKey;
    private DataRepo dataRepo;

    private String quizzKey;
    private boolean isUpdate;


    Animation animationDown;
    Animation animationUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz_detali_teacher);
        ButterKnife.bind(this);
        initRv();

        EventBus.getDefault().register(this);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        animationUP = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        dataRepo = new DataRepo();
        Intent intent = getIntent();
        teacherKey = intent.getStringExtra("t_key");
        if (teacherKey != null) {
            quizzKey = intent.getStringExtra("q_key");
            if (quizzKey != null) {
                isUpdate = true;
                fetchQustionList();
                btnSaveQuizz.setText(getResources().getText(R.string.update));
            }
        } else {
            finish();
        }

        replaceQuestionFragment();
    }

    private static final String TAG = "AddEditQuiz";

    private void fetchQustionList() {
        dataRepo.getSpecificQuizRef(teacherKey, quizzKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                edQuizName.setText(name);
                Quiz quiz = dataSnapshot.getValue(Quiz.class);
                //        Log.d(TAG, "onDataChange: " + quiz.toString());
                //    Log.d(TAG, "onDataChange:  1 " + dataSnapshot);
                Question question;
                questionList = new ArrayList<>();
                Log.d(TAG, "quizz  data " + dataSnapshot);
                //   List<String> strings = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.child(Constants.QUIZZ_QUESTION_LIST).getChildren()) {
                    //QuestionToSerialize question2 = snapshot.getValue(QuestionToSerialize.class);
                    question = snapshot.getValue(Question.class);

                    List<String> list = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : snapshot.child(Constants.answerList).getChildren()) {
                        list.add((String) dataSnapshot1.getValue());
                    }
                    question.setAnswerList(list);

                    //  Log.d(TAG, "onDataChange: question data  " + question.toString());
                    if (question != null) {
                        questionList.add(question);
                        Log.d(TAG, "onDataChange: " + question.toString());
                    }
                }
                if (rvQustionListTeacher != null && questionList.size() > 0) {
                    adapter.setQuestionList(questionList);
                    Log.d(TAG, "quizz data " + adapter.getQuestionList().size());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        replaceQuestionFragment();
    }

    private void replaceQuestionFragment() {
        transaction = manager.beginTransaction();
        QuestionAddFragment fragment = new QuestionAddFragment();
        transaction.replace(R.id.questionContainer, fragment).commit();
    }

    private void initRv() {
        adapter = new QuestionListAdapterAddQuiz(this);
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
                if (direction == ItemTouchHelper.RIGHT) { // update
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

    public Question questionToEdit;

    public Question getQuestionToEdit() {
        return questionToEdit;
    }

    public void setQuestionToEdit(Question questionToEdit) {
        this.questionToEdit = questionToEdit;
    }

    private void replaceQuestionFragment(Question question) {
        setQuestionToEdit(question);
        transaction = manager.beginTransaction();
        QuestionAddFragment fragment = new QuestionAddFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constants.answerList, (ArrayList<String>) question.getAnswerList());
        bundle.putString(Constants.question, question.getQuestion());
        fragment.setArguments(bundle);
        transaction.replace(R.id.questionContainer, fragment).commit();
        setViewToAddOrUpdateQuestion();
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
                if (isUpdate) {
                    quiz.setKey(quizzKey);
                    show("updated");
                } else {
                    show("added");
                }
                dataRepo.addQuiz(quiz);
                // blankFields();
                finish();
                overridePendingTransition(0, R.anim.slide_down);
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


    /**
     * when add fragment finish and sent the Question
     * hidden it
     *
     */
    @Subscribe
    public void onEvent(Question question) {
        if (question.getAnswerList().size() > 0) {
            questionList.add(question);
            adapter.addQuestion(question);
            //make animation with gone visibility
            setViewAfterAddQuestion();
        }
    }

    private void setViewAfterAddQuestion() {
        questionContainer.setVisibility(View.GONE);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        questionContainer.setAnimation(animationDown);

        quizzAndAddLayout.setVisibility(View.VISIBLE);
        animationUP = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        quizzAndAddLayout.setAnimation(animationUP);

    }

    @OnClick(R.id.fabAddQuestion)
    public void onViewClicked11() {
        setViewToAddOrUpdateQuestion();
    }

    /**
     * used to make an animation  when call addQuestion Fragment in case of add or update
     * it first hide quize name and button layout and then show fragment
     */
    private void setViewToAddOrUpdateQuestion() {
        //make animation with gone visibility
        questionContainer.setVisibility(View.VISIBLE);
        animationUP = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        questionContainer.setAnimation(animationUP);

        quizzAndAddLayout.setVisibility(View.GONE);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        quizzAndAddLayout.setAnimation(animationDown);

    }

    @Override
    public void onClickEditQuestion(int pos) {
        Question question = questionList.get(pos);
        adapter.remove(pos);
        questionList.remove(pos);
        replaceQuestionFragment(question);

    }

    @Override
    public void onClickDeleteQuestion(int pos) {
        adapter.remove(pos);
        questionList.remove(pos);
    }

    @Override
    public void onQuestionAdd(Question question) {

    }
}
