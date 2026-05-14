package com.folo.app.questions;

public class Question {
    public String id, category, questionText, answerType, helpText;
    public String[] choices;
    public boolean required;

    public Question(String id, String category, String questionText, String answerType,
                    String[] choices, boolean required, String helpText) {
        this.id = id; this.category = category; this.questionText = questionText;
        this.answerType = answerType; this.choices = choices;
        this.required = required; this.helpText = helpText;
    }
}
