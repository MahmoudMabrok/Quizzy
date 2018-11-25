package com.example.android.quizzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.android.quizzy.R;
import com.example.android.quizzy.util.Constants;
import com.plattysoft.leonids.ParticleSystem;

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

      /*  new ParticleSystem(this, 4445554, R.drawable.add, 2000)
                .setSpeedModuleAndAngleRange(0f, 0.3f, 180, 180)
                .setRotationSpeed(144)
                .setAcceleration(0.00005f, 90)
                .emit(btnIoenStudent, 50);*/

      /*  new ParticleSystem(this, 10, R.drawable.button, 2000)
                .setSpeedRange(0.2f , 0.5f)
                .oneShot(btnIoenTeacher, 2);*/



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
