package com.example.android.quizzy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.quizzy.R;
import com.example.android.quizzy.model.Quiz;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class QuizeListTeacherAdapter extends RecyclerView.Adapter<QuizeListTeacherAdapter.ViewHolder> {

    private Context context;
    private List<Quiz> quizList;

    public QuizeListTeacherAdapter(Context context) {
        this.context = context;
        quizList = new ArrayList<>();
    }

    public void setList(List<Quiz> list) {
        quizList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.tvQuizName.setText(quiz.getName());
        holder.tvQuizTeacherName.setText(quiz.getCreatorName());
        holder.tvQuizTotalScore.setText(quiz.getQuestionList().size());
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvQuizName)
        TextView tvQuizName;
        @BindView(R.id.tvQuizTeacherName)
        TextView tvQuizTeacherName;
        @BindView(R.id.tvQuizTotalScore)
        TextView tvQuizTotalScore;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
