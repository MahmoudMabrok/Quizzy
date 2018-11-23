package com.example.android.quizzy.model;

import android.support.annotation.Keep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mahmoud on 10/21/2018.
 */
@Keep
public class Quiz {
    private String key;
    private String name ;
    private String creatorName ;
    private String teacherKey;
    private List<Question> questionList ;
    private int score;
    private int percentage;
    private int grade;
    public Quiz() {
    }


    public Quiz(String name, String creatorName) {
        this.name = name;
        this.creatorName = creatorName;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTeacherKey() {
        return teacherKey;
    }

    public void setTeacherKey(String teacherKey) {
        this.teacherKey = teacherKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = new ArrayList<>(questionList);
    }
}
