package com.example.android.quizzy.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button

import com.example.android.quizzy.R
import com.example.android.quizzy.model.Quiz
import com.example.android.quizzy.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.plattysoft.leonids.ParticleSystem

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import durdinapps.rxfirebase2.RxFirebaseDatabase

class Entry : AppCompatActivity() {

    @BindView(R.id.btnIoenTeacher)
    internal var btnIoenTeacher: Button? = null
    @BindView(R.id.btnIoenStudent)
    internal var btnIoenStudent: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)
        ButterKnife.bind(this)

        val quizRef = FirebaseDatabase.getInstance().getReference().child("users").child("teachers").child("0114919427")
                .child("quiz")

        RxFirebaseDatabase.observeSingleValueEvent<Quiz>(quizRef, Quiz::class.java!!)
                .subscribe({ q: Quiz -> Log.d(TAG, "onCreate: " + q.getName()) })


        val intent = Intent(this, TeacherHome::class.java)
        intent.putExtra(Constants.TEACHERS_KEY, "0114919427")
        startActivity(intent)
    }

    @OnClick(R.id.btnIoenTeacher)
    fun onBtnIoenTeacherClicked() {
        val intent = Intent(this, TeacherHome::class.java)
        intent.putExtra(Constants.TEACHERS_KEY, "0114919427")
        startActivity(intent)
    }

    @OnClick(R.id.btnIoenStudent)
    fun onBtnIoenStudentClicked() {
        val intent = Intent(this, StudentActivity::class.java)
        intent.putExtra(Constants.TEACHERS_KEY, "0114919427")
        startActivity(intent)
    }

    companion object {
        private val TAG = "Entry"
    }
}
