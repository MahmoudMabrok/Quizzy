package com.example.android.quizzy.api;

import com.example.android.quizzy.model.Quiz;
import com.example.android.quizzy.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class FirebaseDataSource {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference userRef;
    DatabaseReference teacherRef;
    DatabaseReference studentRef;
    FirebaseAuth auth;

    public FirebaseDataSource() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        userRef = firebaseDatabase.getReference(Constants.USERS_KEY);
        teacherRef = userRef.child(Constants.TEACHERS_KEY);
        studentRef = userRef.child(Constants.STUDENTS_KEY);
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public String getCurrentUserUUID() {
        return auth.getCurrentUser().getUid();
    }


    public void addQuiz(String teacherKey, Quiz quiz) {
        String key = teacherRef.child(teacherKey).child(Constants.QUIZZ_CHILD).push().getKey();
        quiz.setKey(key);
        teacherRef.child(teacherKey).child(Constants.QUIZZ_CHILD).child(key).setValue(quiz);
    }

    public void updateQuiz(String teacherKey, Quiz quiz) {
        teacherRef.child(teacherKey).child(Constants.QUIZZ_CHILD).child(quiz.getKey()).setValue(quiz);
    }
}
