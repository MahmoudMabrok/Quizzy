package com.example.android.quizzy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.quizzy.R;
import com.example.android.quizzy.interfaces.OnQuizzClick;
import com.example.android.quizzy.model.Quiz;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class QuizeListStudentAdapter extends RecyclerView.Adapter<QuizeListStudentAdapter.ViewHolder> {

    Quiz quiz;
    private Context context;
    private List<Quiz> quizList;
    private List<Quiz> completeList;
    private OnQuizzClick onQuizzClick;

    public QuizeListStudentAdapter(Context context, OnQuizzClick click) {
        this.context = context;
        onQuizzClick = click;
        quizList = new ArrayList<>();
    }

    public void setList(List<Quiz> list, List<Quiz> completeList) {
        quizList = new ArrayList<>(list);
        this.completeList = new ArrayList<>(completeList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_quiz_list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Quiz quiz = quizList.get(position);
        holder.tvQuizName.setText(quiz.getName());
        holder.tvQuizTeacherName.setText(quiz.getTeacherKey());
        Quiz temp = getIfisInCompleteList(quiz);
        String text = "N/A";
        if (temp != null) {
            quiz = temp;
            text = temp.getScore() + " %";
            holder.tvQuizTotalScore.setText(text);
            if (temp.getScore() <= 50) {
                holder.tvQuizState.setTextColor(R.color.falid);
            } else {
                holder.tvQuizState.setTextColor(R.color.passed);
            }
        } else {
            holder.tvQuizTotalScore.setText(text);
            holder.tvQuizState.setTextColor(R.color.not_attemped);
        }
        final Quiz finalQuiz = quiz;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" + completeList.size(), Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "" + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                onQuizzClick.onQuizzClick(finalQuiz);
            }
        });
    }

    private Quiz getIfisInCompleteList(Quiz quiz) {
        for (Quiz q : completeList
                ) {
            if (q.getKey().equals(quiz.getKey())) {
                return q;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvQuizName)
        TextView tvQuizName;
        @BindView(R.id.tvQuizTeacherName)
        TextView tvQuizTeacherName;
        @BindView(R.id.tvQuizTotalScore)
        TextView tvQuizTotalScore;

        @BindView(R.id.tvQuizState)
        TextView tvQuizState;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
