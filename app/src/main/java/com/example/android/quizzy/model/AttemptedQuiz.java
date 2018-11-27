package com.example.android.quizzy.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

import java.util.List;

@Keep
public class AttemptedQuiz {
    boolean state;
    private String studentName;
    private int grade;
    private int percentage;
    private List<Question> questionArrayList;


    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

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
