package com.example.rightside;

import java.util.List;

public class Questions {

    private String questionText;
    private List<String> options;
    private int correctAnswerIndex;

    public Questions(){}
    public Questions(String questionText, List<String> options, int correctAnswerIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}
