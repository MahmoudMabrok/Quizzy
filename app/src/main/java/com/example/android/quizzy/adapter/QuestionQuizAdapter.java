package com.example.android.quizzy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.quizzy.R;
import com.example.android.quizzy.model.Question;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class QuestionQuizAdapter extends RecyclerView.Adapter<QuestionQuizAdapter.ViewHolder> {

    private List<Question> questionList;

    public QuestionQuizAdapter() {
        questionList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_question_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String strNum = "Question " + (position + 1);
        holder.tvQuestionNum.setText(strNum);
        Question question = questionList.get(position);
        holder.tvQuestionTitle.setText(question.getQuestion());


      /*  final String s = anserList.get(position);
        holder.tvAnswer.setText(s);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cbAnswer.isChecked()) {
                    // checkedList.remove(s);
                    holder.cbAnswer.setChecked(false);
                } else {
                    //checkedList.add(s);
                    holder.cbAnswer.setChecked(true);
                }
            }
        });*/
/*
        holder.cbAnswer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkedList.add(s);
                } else {
                    checkedList.remove(s);
                }
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }


    public void setList(List<Question> question) {
        questionList = new ArrayList<>(question);
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvQuestionNum)
        TextView tvQuestionNum;
        @BindView(R.id.tvQuestionTitle)
        TextView tvQuestionTitle;
        @BindView(R.id.rvQuizzAnwerQuestion)
        RecyclerView rvQuizzAnwerQuestion;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
