package com.example.android.quizzy.model;

import android.support.annotation.Keep;

import java.util.List;

@Keep
public class AttemptedQuiz {
    boolean state;
    private String studentName;
    private int grade;
    private List<Question> questionArrayList;


    public AttemptedQuiz() {
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public List<Question> getQuestionArrayList() {
        return questionArrayList;
    }

    public void setQuestionArrayList(List<Question> questionArrayList) {
        this.questionArrayList = questionArrayList;
    }
}
