package com.example.android.quizzy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.quizzy.R;
import com.example.android.quizzy.interfaces.OnQuestionEdit;
import com.example.android.quizzy.model.Question;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class QuestionListAdapterAddQuiz extends RecyclerView.Adapter<QuestionListAdapterAddQuiz.ViewHolder> {


    private List<Question> questionList;
    private OnQuestionEdit edit;

    public QuestionListAdapterAddQuiz(OnQuestionEdit onQuestionEdit) {
        edit = onQuestionEdit;
        questionList = new ArrayList<>();
    }

    public void setQuestionList(List<Question> list) {
        questionList.clear();
        questionList = new ArrayList<>(list);
        /*for (Question question : list
                ) {
            addQuestion(question);
        }*/
        notifyDataSetChanged();
    }

    public void addQuestion(Question question) {
        questionList.add(question);
        notifyItemInserted(questionList.size() - 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String question = questionList.get(position).getQuestion();
        holder.tvQuestion.setText(question);

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.onClickEditQuestion(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void clear() {
        questionList.clear();
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        questionList.remove(pos);
        notifyDataSetChanged();
    }

    public List<Question> getQuestionList() {
        return questionList;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvQuestion)
        TextView tvQuestion;

        @BindView(R.id.ivEdit)
        ImageView ivEdit;
        @BindView(R.id.ivDelete)
        ImageView ivDelete;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
