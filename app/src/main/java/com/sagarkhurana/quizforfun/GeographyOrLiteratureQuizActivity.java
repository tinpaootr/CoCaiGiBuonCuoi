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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;
import com.sagarkhurana.quizforfun.data.QuestionResult;
import com.sagarkhurana.quizforfun.other.Constants;
import com.sagarkhurana.quizforfun.other.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GeographyOrLiteratureQuizActivity extends AppCompatActivity {

    private int currentQuestionIndex = 0;
    private TextView tvQuestion, tvQuestionNumber, tvTimer;
    private Button btnNext;
    private RadioGroup radioGroup;
    private RadioButton radioButton1, radioButton2, radioButton3, radioButton4;
    private List<String> questions;
    private int correctQuestion = 0;
    private Map<String, Map<String, Boolean>> questionsAnswerMap;

    private LinearProgressIndicator progressBar;
    private CountDownTimer countDownTimer;
    private static final long COUNTDOWN_IN_MILLIS = 20000; // 20 seconds
    
    private List<QuestionResult> questionResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geography_or_literature_quiz);

        Intent intent = getIntent();
        String subject = intent.getStringExtra(Constants.SUBJECT);

        TextView tvTitle = findViewById(R.id.textView26);

        if (subject.equals(getString(R.string.literature))) {
            questionsAnswerMap = Utils.getRandomLiteratureAndGeographyQuestions(this, getString(R.string.literature), Constants.QUESTION_SHOWING);
            tvTitle.setText(getString(R.string.literature_quiz));
        } else if (subject.equals(getString(R.string.history_subject))) {
            questionsAnswerMap = Utils.getRandomLiteratureAndGeographyQuestions(this, getString(R.string.history_subject), Constants.QUESTION_SHOWING);
            tvTitle.setText(getString(R.string.history_quiz));
        } else {
            questionsAnswerMap = Utils.getRandomLiteratureAndGeographyQuestions(this, getString(R.string.geography), Constants.QUESTION_SHOWING);
            tvTitle.setText(getString(R.string.geography_quiz));
        }
        questions = new ArrayList<>(questionsAnswerMap.keySet());


        tvQuestion = findViewById(R.id.textView78);
        tvQuestionNumber = findViewById(R.id.textView18);
        btnNext = findViewById(R.id.btnNextQuestionLiteratureAndGeography);
        radioGroup = findViewById(R.id.radioGroup);
        tvTimer = findViewById(R.id.tvTimerGeographyOrLiterature);
        progressBar = findViewById(R.id.progressBarGeographyOrLiterature);

        progressBar.setMax(Constants.QUESTION_SHOWING);

        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswerAndNext(subject);
            }
        });

        findViewById(R.id.imageViewStartQuizGeographyOrLiterature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitConfirmationDialog();
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

    private void checkAnswerAndNext(String subject) {
        int checkedId = radioGroup.getCheckedRadioButtonId();
        if (checkedId == -1) {
            Toast.makeText(this, getString(R.string.please_select_option), Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton radioButton = findViewById(checkedId);
        String userAnswer = radioButton.getText().toString();
        
        Map<String, Boolean> answersMap = questionsAnswerMap.get(questions.get(currentQuestionIndex));
        String correctAnswer = "";
        for (Map.Entry<String, Boolean> entry : answersMap.entrySet()) {
            if (entry.getValue()) {
                correctAnswer = entry.getKey();
                break;
            }
        }
        
        boolean isCorrect = answersMap.get(userAnswer);

        if (isCorrect) {
            correctQuestion++;
        }
        
        questionResults.add(new QuestionResult(questions.get(currentQuestionIndex), correctAnswer, userAnswer, isCorrect));

        goToNextQuestion(subject);
    }

    private void goToNextQuestion(String subject) {
        if (countDownTimer != null) countDownTimer.cancel();
        currentQuestionIndex++;

        if (currentQuestionIndex < questions.size()) {
            displayNextQuestions();
        } else {
            finishQuiz(subject);
        }
    }

    private void finishQuiz(String subject) {
        Intent intentResult = new Intent(GeographyOrLiteratureQuizActivity.this, FinalResultActivity.class);
        intentResult.putExtra(Constants.SUBJECT, subject);
        intentResult.putExtra(Constants.CORRECT, correctQuestion);
        intentResult.putExtra(Constants.INCORRECT, questions.size() - correctQuestion);
        
        String questionsJson = new Gson().toJson(questionResults);
        intentResult.putExtra(Constants.QUESTIONS_DETAILS, questionsJson);
        
        intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentResult);
        finish();
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

    private void displayNextQuestions() {
        radioGroup.clearCheck();
        setAnswersToRadioButton();
        tvQuestion.setText(questions.get(currentQuestionIndex));
        tvQuestionNumber.setText(getString(R.string.current_question_format, currentQuestionIndex + 1));
        progressBar.setProgress(currentQuestionIndex + 1);

        if (currentQuestionIndex == questions.size() - 1) {
            btnNext.setText(getText(R.string.finish));
        }
        startCountDown();
    }

    private void displayData() {
        tvQuestion.setText(questions.get(currentQuestionIndex));
        tvQuestionNumber.setText(getString(R.string.current_question_format, currentQuestionIndex + 1));
        progressBar.setProgress(currentQuestionIndex + 1);
        setAnswersToRadioButton();
        startCountDown();
    }

    private void setAnswersToRadioButton() {
        ArrayList<String> questionKey = new ArrayList<>(questionsAnswerMap.get(questions.get(currentQuestionIndex)).keySet());

        radioButton1.setText(questionKey.get(0));
        radioButton2.setText(questionKey.get(1));
        radioButton3.setText(questionKey.get(2));
        radioButton4.setText(questionKey.get(3));
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
                Toast.makeText(GeographyOrLiteratureQuizActivity.this, getString(R.string.time_up), Toast.LENGTH_SHORT).show();
                
                Map<String, Boolean> answersMap = questionsAnswerMap.get(questions.get(currentQuestionIndex));
                String correctAnswer = "";
                for (Map.Entry<String, Boolean> entry : answersMap.entrySet()) {
                    if (entry.getValue()) {
                        correctAnswer = entry.getKey();
                        break;
                    }
                }
                questionResults.add(new QuestionResult(questions.get(currentQuestionIndex), correctAnswer, getString(R.string.timeout_text), false));

                Intent intent = getIntent();
                String subject = intent.getStringExtra(Constants.SUBJECT);
                goToNextQuestion(subject);
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