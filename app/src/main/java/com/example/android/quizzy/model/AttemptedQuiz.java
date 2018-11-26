package com.example.android.quizzy.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

import java.util.List;

@Keep
public class AttemptedQuiz implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.state ? (byte) 1 : (byte) 0);
        dest.writeString(this.studentName);
        dest.writeInt(this.grade);
        dest.writeTypedList(this.questionArrayList);
    }

    protected AttemptedQuiz(Parcel in) {
        this.state = in.readByte() != 0;
        this.studentName = in.readString();
        this.grade = in.readInt();
        this.questionArrayList = in.createTypedArrayList(Question.CREATOR);
    }

    public static final Parcelable.Creator<AttemptedQuiz> CREATOR = new Parcelable.Creator<AttemptedQuiz>() {
        @Override
        public AttemptedQuiz createFromParcel(Parcel source) {
            return new AttemptedQuiz(source);
        }

        @Override
        public AttemptedQuiz[] newArray(int size) {
            return new AttemptedQuiz[size];
        }
    };
}
