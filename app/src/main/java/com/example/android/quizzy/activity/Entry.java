package com.example.android.quizzy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.example.android.quizzy.R;
import com.example.android.quizzy.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.plattysoft.leonids.ParticleSystem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Entry extends AppCompatActivity {

    @BindView(R.id.btnIoenTeacher)
    Button btnIoenTeacher;
    @BindView(R.id.btnIoenStudent)
    Button btnIoenStudent;

    private static final String TAG = "Entry";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);

        FirebaseDatabase.getInstance().getReference().child("users").child("teachers").child("0114919427")
                .child("quiz").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d(TAG, "onDataChange: " + dataSnapshot1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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
