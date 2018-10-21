package com.example.android.quizzy.model;

import android.support.annotation.Keep;

import java.util.List;

/**
 * Created by Mahmoud on 10/21/2018.
 */
@Keep
public class Quiz {
    private String name ;
    private String creatorName ;
    private List<Question> questionList ;

    public Quiz() {
    }

    public Quiz(String name, String creatorName) {
        this.name = name;
        this.creatorName = creatorName;
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
        this.questionList = questionList;
    }
}
