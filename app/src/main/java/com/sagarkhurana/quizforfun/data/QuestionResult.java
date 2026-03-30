package com.sagarkhurana.quizforfun.data;

public class QuestionResult {
    private String question;
    private String correctAnswer;
    private String userAnswer;
    private boolean isCorrect;

    public QuestionResult(String question, String correctAnswer, String userAnswer, boolean isCorrect) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.userAnswer = userAnswer;
        this.isCorrect = isCorrect;
    }

    public String getQuestion() { return question; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getUserAnswer() { return userAnswer; }
    public boolean isCorrect() { return isCorrect; }
}