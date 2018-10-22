package com.example.android.quizzy.model;

import android.support.annotation.Keep;

import java.util.List;

/**
 * Created by Mahmoud on 10/21/2018.
 */
@Keep
public class Question {
    private String key;
    private String question ;
    private List<String> answerList ;
    private int weight ;
    private String correctAnswer ;

    public Question() {
    }

    public Question(String question, int weight, String correctAnswer) {
        this.question = question;
        this.weight = weight;
        this.correctAnswer = correctAnswer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
