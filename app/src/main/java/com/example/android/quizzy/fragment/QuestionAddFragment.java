package com.example.android.quizzy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.activity.AddEditQuiz;
import com.example.android.quizzy.adapter.AnwerListAddQuizAdapter;
import com.example.android.quizzy.model.Question;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionAddFragment extends Fragment {


    @BindView(R.id.edQuestionAddFragment)
    EditText edQuestionAddFragment;
    @BindView(R.id.edAnswerAddFragment)
    EditText edAnswerAddFragment;
    @BindView(R.id.btnAddAnswer)
    Button btnAddAnswer;
    @BindView(R.id.rvAnsWersAddQuizz)
    RecyclerView rvAnsWersAddQuizz;
    @BindView(R.id.btnAddQuestion)
    Button btnAddQuestion;
    Unbinder unbinder;

    AnwerListAddQuizAdapter adapter;
    boolean isUpdate;
    public QuestionAddFragment() {
        // Required empty public constructor
    }

    private List<String> answerList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_add, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRv();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable("q") != null) {
            Question question = (Question) bundle.getSerializable("q");
            fillUi(question);
            isUpdate = true;
        }
        return view;
    }

    private void initRv() {
        adapter = new AnwerListAddQuizAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvAnsWersAddQuizz.setLayoutManager(manager);
        rvAnsWersAddQuizz.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void fillUi(Question question) {
        edQuestionAddFragment.setText(question.getQuestion());
        adapter.setList(question.getAnswerList());
    }

    @OnClick(R.id.btnAddAnswer)
    public void onBtnAddAnswerClicked() {
        String anser = edAnswerAddFragment.getText().toString();
        if (!TextUtils.isEmpty(anser)) {
            adapter.add(anser);
            answerList.add(anser);
            edAnswerAddFragment.setText("");

        } else {
            show("Enter Answer");
        }
    }

    private void show(String a) {
        Toast.makeText(getContext(), a, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnAddQuestion)
    public void onBtnAddQuestionClicked() {
        String questionName = edQuestionAddFragment.getText().toString();
        if (!TextUtils.isEmpty(questionName)) {
            if (answerList.size() > 0) {
                Question question = new Question();
                question.setQuestion(questionName);
                question.setAnswerList(answerList);
                if (adapter.getCheckedList().size() > 0) {
                    question.setCorrectAnswer(adapter.getCheckedList().get(0));
                }
                question.setWeight(1);
                addQuestion(question);
                blankFields();
            } else {
                show("Add Answers");
            }
        } else {
            show("Add Question Name");
        }
    }

    private void addQuestion(Question question) {
        if (question != null && question.getAnswerList().size() > 0) {
            ((AddEditQuiz) getActivity()).onQuestionAdd(question);
        } else {
            show("aaa");
        }
    }

    private void blankFields() {
        edQuestionAddFragment.setText("");
        adapter.deleteAll();
        answerList.clear();
    }
}
