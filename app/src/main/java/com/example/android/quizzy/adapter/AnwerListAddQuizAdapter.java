package com.example.android.quizzy.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.example.android.quizzy.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class AnwerListAddQuizAdapter extends RecyclerView.Adapter<AnwerListAddQuizAdapter.ViewHolder> {

    private List<String> anserList;

    public AnwerListAddQuizAdapter() {
        anserList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anser_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s = anserList.get(position);
        holder.tvAnswer.setText(s);
    }

    public List<String> getAnserList() {
        return anserList;
    }

    @Override
    public int getItemCount() {
        return anserList.size();
    }

    public void add(String s) {
        anserList.add(s);

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvAnswer)
        CheckedTextView tvAnswer;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
