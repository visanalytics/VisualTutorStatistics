/*
 * Decompiled with CFR 0_114.
 */
package com.deakin.datavis;

public class Question {
    String[] answers;
    String answerCorrect;
    String question;

    public Question(String question, String[] answers, String answerCorrect) {
        this.answers = answers;
        this.answerCorrect = answerCorrect;
        this.question = question;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getCorrectAnswer() {
        return this.answerCorrect;
    }

    public String[] getAnswers() {
        return this.answers;
    }
}

