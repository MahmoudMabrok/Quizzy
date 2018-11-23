package com.example.android.quizzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.android.quizzy.R;
import com.example.android.quizzy.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Entry extends AppCompatActivity {

    @BindView(R.id.btnIoenTeacher)
    Button btnIoenTeacher;
    @BindView(R.id.btnIoenStudent)
    Button btnIoenStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnIoenTeacher)
    public void onBtnIoenTeacherClicked() {
        Intent intent = new Intent(this, TeacherHome.class);
        intent.putExtra(Constants.TEACHERS_KEY, "0114919427");
        startActivity(intent);
    }

    @OnClick(R.id.btnIoenStudent)
    public void onBtnIoenStudentClicked() {
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra(Constants.TEACHERS_KEY, "0114919427");
        startActivity(intent);
    }
}
