package com.example.android.quizzy.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mahmoud on 10/21/2018.
 */
@Keep
public class Question implements Parcelable {
    private String key;
    private String question ;
    private List<String> answerList ;
    private String correctAnswer ;
    private String StudentAnswer;
    private boolean state;
    private int weight;

    @Override
    public String toString() {
        return "Question{" +
                "key='" + key + '\'' +
                ", question='" + question + '\'' +
                ", answerList=" + (answerList != null ? answerList.size() : " ") +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", StudentAnswer='" + StudentAnswer + '\'' +
                ", state=" + state +
                ", weight=" + weight +
                '}';
    }

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

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getStudentAnswer() {
        return StudentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        StudentAnswer = studentAnswer;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.question);
        dest.writeStringList(this.answerList);
        dest.writeString(this.correctAnswer);
        dest.writeString(this.StudentAnswer);
        dest.writeByte(this.state ? (byte) 1 : (byte) 0);
        dest.writeInt(this.weight);
    }

    protected Question(Parcel in) {
        this.key = in.readString();
        this.question = in.readString();
        this.answerList = in.createStringArrayList();
        this.correctAnswer = in.readString();
        this.StudentAnswer = in.readString();
        this.state = in.readByte() != 0;
        this.weight = in.readInt();
    }

    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
