package com.example.android.quizzy.api;

import com.example.android.quizzy.model.Quiz;

/**
 * Created by Mahmoud on 10/22/2018.
 */
public class DataRepo {
    private FirebaseDataSource firebaseDataSource;

    public DataRepo() {
        firebaseDataSource = new FirebaseDataSource();
    }

    public void addQuiz(Quiz quiz) {
        firebaseDataSource.addQuiz(quiz.getTeacherKey(), quiz);
    }
}
