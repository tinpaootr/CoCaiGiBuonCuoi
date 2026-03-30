package com.sagarkhurana.quizforfun;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;
import com.sagarkhurana.quizforfun.data.QuestionResult;
import com.sagarkhurana.quizforfun.other.Constants;
import com.sagarkhurana.quizforfun.other.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MathQuizActivity extends AppCompatActivity {

    private int currentQuestionIndex = 0;
    private TextView tvQuestion, tvQuestionNumber, tvTimer;
    private Button btnNext;
    private List<String> questions;
    private int correctQuestion = 0;
    private EditText etAnswer;
    private HashMap<String, String> questionsAnswerMap;
    
    private LinearProgressIndicator progressBar;
    private CountDownTimer countDownTimer;
    private static final long COUNTDOWN_IN_MILLIS = 20000; // 20 seconds
    
    private List<QuestionResult> questionResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_quiz);

        questionsAnswerMap = (HashMap<String, String>) Utils.getRandomMathQuestions(Constants.QUESTION_SHOWING);
        questions = new ArrayList<>(questionsAnswerMap.keySet());

        tvQuestion = findViewById(R.id.textView8);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumberMath);
        btnNext = findViewById(R.id.btnNextQuestionMath);
        etAnswer = findViewById(R.id.tietEnterAnswerMath);
        tvTimer = findViewById(R.id.tvTimerMath);
        progressBar = findViewById(R.id.progressBarMath);

        progressBar.setMax(Constants.QUESTION_SHOWING);

        findViewById(R.id.imageViewStartQuiz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitConfirmationDialog();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerAndNext();
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });

        displayData();
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.exit_quiz_title));
        builder.setMessage(getString(R.string.exit_quiz_message));
        builder.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.continue_quiz), null);
        builder.show();
    }

    private void checkAnswerAndNext() {
        String answer = etAnswer.getText().toString();

        if (answer.isEmpty()){
            Toast.makeText(MathQuizActivity.this, getString(R.string.please_enter_answer), Toast.LENGTH_SHORT).show();
            return;
        }

        String correctAnswer = questionsAnswerMap.get(questions.get(currentQuestionIndex));
        boolean isCorrect = correctAnswer.equals(answer);
        
        if (isCorrect) {
            correctQuestion++;
        }
        
        questionResults.add(new QuestionResult(getString(R.string.question_format, questions.get(currentQuestionIndex)), correctAnswer, answer, isCorrect));
        
        goToNextQuestion();
    }

    private void goToNextQuestion() {
        if (countDownTimer != null) countDownTimer.cancel();
        currentQuestionIndex++;

        if (currentQuestionIndex < Constants.QUESTION_SHOWING) {
            displayNextQuestions();
        } else {
            finishQuiz();
        }
    }

    private void finishQuiz() {
        Intent intentResult = new Intent(MathQuizActivity.this, FinalResultActivity.class);
        intentResult.putExtra(Constants.SUBJECT, getString(R.string.math));
        intentResult.putExtra(Constants.CORRECT, correctQuestion);
        intentResult.putExtra(Constants.INCORRECT, Constants.QUESTION_SHOWING - correctQuestion);
        
        String questionsJson = new Gson().toJson(questionResults);
        intentResult.putExtra(Constants.QUESTIONS_DETAILS, questionsJson);
        
        intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentResult);
        finish();
    }

    private void displayNextQuestions() {
        etAnswer.setText("");
        tvQuestion.setText(getString(R.string.question_format, questions.get(currentQuestionIndex)));
        tvQuestionNumber.setText(getString(R.string.current_question_format, currentQuestionIndex + 1));
        progressBar.setProgress(currentQuestionIndex + 1);

        if (currentQuestionIndex == Constants.QUESTION_SHOWING - 1) {
            btnNext.setText(getText(R.string.finish));
        }
        startCountDown();
    }

    private void displayData() {
        tvQuestion.setText(getString(R.string.question_format, questions.get(currentQuestionIndex)));
        tvQuestionNumber.setText(getString(R.string.current_question_format, currentQuestionIndex + 1));
        progressBar.setProgress(currentQuestionIndex + 1);
        startCountDown();
    }

    private void startCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(COUNTDOWN_IN_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                tvTimer.setText(getString(R.string.timer_format, seconds));
                if (seconds < 5) {
                    tvTimer.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                } else {
                    tvTimer.setTextColor(getResources().getColor(R.color.peach));
                }
            }

            @Override
            public void onFinish() {
                tvTimer.setText(getString(R.string.timer_zero));
                Toast.makeText(MathQuizActivity.this, getString(R.string.time_up), Toast.LENGTH_SHORT).show();
                
                String correctAnswer = questionsAnswerMap.get(questions.get(currentQuestionIndex));
                questionResults.add(new QuestionResult(getString(R.string.question_format, questions.get(currentQuestionIndex)), correctAnswer, getString(R.string.timeout_text), false));

                goToNextQuestion();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}