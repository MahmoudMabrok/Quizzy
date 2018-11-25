package com.example.android.quizzy.model;

import java.util.List;

public class Data {

    private String quizName;
    private List<AttemptedQuiz> attemptedQuizList;
    private String topStudent;

    public String getTopStudent() {
        return topStudent;
    }

    public void setTopStudent(String topStudent) {
        this.topStudent = topStudent;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public List<AttemptedQuiz> getAttemptedQuizList() {
        return attemptedQuizList;
    }

    public void setAttemptedQuizList(List<AttemptedQuiz> attemptedQuizList) {
        this.attemptedQuizList = attemptedQuizList;
    }
}
