package com.example.android.quizzy.api;

import com.example.android.quizzy.model.Quiz;
import com.google.firebase.database.DatabaseReference;

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

    public DatabaseReference getSpecificQuizRef(String teacherKey, String quizzKey) {
        return firebaseDataSource.getSpecificQuizRef(teacherKey, quizzKey);
    }

    public String getUUID() {
        return firebaseDataSource.getCurrentUserUUID();
    }

    public DatabaseReference getCompleteListRef(String teacherID, String studentUUID) {
        return firebaseDataSource.getCompleteListRef(teacherID, studentUUID);
    }

    public void addQuizTOCompleteList(Quiz quiz, String sID) {
        firebaseDataSource.addQuizTOCompleteList(quiz, sID);
    }
}
