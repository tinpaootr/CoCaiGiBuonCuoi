package com.sagarkhurana.quizforfun;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.sagarkhurana.quizforfun.data.Attempt;
import com.sagarkhurana.quizforfun.data.UserDatabase;
import com.sagarkhurana.quizforfun.data.UserDatabaseClient;
import com.sagarkhurana.quizforfun.other.SharedPref;
import com.sagarkhurana.quizforfun.other.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SurvivalActivity extends AppCompatActivity {

    private TextView tvQuestion, tvScore, tvLives, tvTimer;
    private Button btnOption1, btnOption2, btnOption3, btnOption4, btn5050, btnSkip;
    private LinearProgressIndicator progressBar;

    private Map<String, Map<String, Boolean>> allQuestions;
    private List<String> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int lives = 3;
    
    private CountDownTimer countDownTimer;
    private static final long COUNTDOWN_IN_MILLIS = 15000; // 15 seconds for survival

    private long sessionStartTime;
    private int sessionMaxScore = 0;
    private boolean isGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survival);

        sessionStartTime = System.currentTimeMillis();

        initViews();
        loadQuestions();
        displayQuestion();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });
    }

    private void initViews() {
        tvQuestion = findViewById(R.id.tvQuestionSurvival);
        tvScore = findViewById(R.id.tvScoreSurvival);
        tvLives = findViewById(R.id.tvLivesSurvival);
        // We'll reuse the progress bar for timer or progress, let's use it for timer
        progressBar = findViewById(R.id.pbSurvival);
        progressBar.setMax(15);

        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);
        btn5050 = findViewById(R.id.btn5050);
        btnSkip = findViewById(R.id.btnSkip);

        ImageView ivBack = findViewById(R.id.ivBackSurvival);
        ivBack.setOnClickListener(v -> showExitConfirmationDialog());

        View.OnClickListener optionClickListener = v -> {
            Button clickedButton = (Button) v;
            checkAnswer(clickedButton.getText().toString());
        };

        btnOption1.setOnClickListener(optionClickListener);
        btnOption2.setOnClickListener(optionClickListener);
        btnOption3.setOnClickListener(optionClickListener);
        btnOption4.setOnClickListener(optionClickListener);

        btn5050.setOnClickListener(v -> use5050());
        btnSkip.setOnClickListener(v -> useSkip());
    }

    private void loadQuestions() {
        allQuestions = Utils.getAllQuestions();
        questionList = new ArrayList<>(allQuestions.keySet());
        Collections.shuffle(questionList);
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= questionList.size()) {
            // If we run out of questions, reshuffle and start over or end game
            Collections.shuffle(questionList);
            currentQuestionIndex = 0;
        }

        String currentQuestion = questionList.get(currentQuestionIndex);
        tvQuestion.setText(currentQuestion);

        Map<String, Boolean> answersMap = allQuestions.get(currentQuestion);
        List<String> options = new ArrayList<>(answersMap.keySet());
        Collections.shuffle(options);

        btnOption1.setText(options.get(0));
        btnOption2.setText(options.get(1));
        btnOption3.setText(options.get(2));
        btnOption4.setText(options.get(3));
        
        btnOption1.setVisibility(View.VISIBLE);
        btnOption2.setVisibility(View.VISIBLE);
        btnOption3.setVisibility(View.VISIBLE);
        btnOption4.setVisibility(View.VISIBLE);

        updateUI();
        startCountDown();
    }

    private void checkAnswer(String userAnswer) {
        if (countDownTimer != null) countDownTimer.cancel();
        
        Map<String, Boolean> answersMap = allQuestions.get(questionList.get(currentQuestionIndex));
        boolean isCorrect = answersMap.get(userAnswer);

        if (isCorrect) {
            score += 10;
            Toast.makeText(this, getString(R.string.correct_plus_point), Toast.LENGTH_SHORT).show();
        } else {
            lives--;
            Toast.makeText(this, getString(R.string.incorrect_minus_life), Toast.LENGTH_SHORT).show();
        }

        if (lives <= 0) {
            gameOver();
        } else {
            currentQuestionIndex++;
            displayQuestion();
        }
    }

    private void use5050() {
        Map<String, Boolean> answersMap = allQuestions.get(questionList.get(currentQuestionIndex));
        int removed = 0;
        Button[] buttons = {btnOption1, btnOption2, btnOption3, btnOption4};
        
        List<Button> wrongButtons = new ArrayList<>();
        for (Button btn : buttons) {
            if (!answersMap.get(btn.getText().toString())) {
                wrongButtons.add(btn);
            }
        }
        
        Collections.shuffle(wrongButtons);
        for (int i = 0; i < 2; i++) {
            wrongButtons.get(i).setVisibility(View.INVISIBLE);
        }
        
        btn5050.setEnabled(false);
        btn5050.setAlpha(0.5f);
    }

    private void useSkip() {
        if (countDownTimer != null) countDownTimer.cancel();
        currentQuestionIndex++;
        displayQuestion();
        btnSkip.setEnabled(false);
        btnSkip.setAlpha(0.5f);
    }

    private void updateUI() {
        tvScore.setText(getString(R.string.score_text, score));
        tvLives.setText(getString(R.string.lives_text, lives));
    }

    private void startCountDown() {
        if (countDownTimer != null) countDownTimer.cancel();
        
        countDownTimer = new CountDownTimer(COUNTDOWN_IN_MILLIS, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                progressBar.setProgress(secondsRemaining);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                lives--;
                Toast.makeText(SurvivalActivity.this, getString(R.string.timeout_minus_life), Toast.LENGTH_SHORT).show();
                if (lives <= 0) {
                    gameOver();
                } else {
                    currentQuestionIndex++;
                    displayQuestion();
                }
            }
        }.start();
    }

    private void gameOver() {
        if (isGameOver) return;
        isGameOver = true;

        if (countDownTimer != null) countDownTimer.cancel();
        
        if (score > sessionMaxScore) {
            sessionMaxScore = score;
        }
        saveResult();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.game_over_title))
               .setMessage(getString(R.string.game_over_message, score))
               .setCancelable(false)
               .setPositiveButton(getString(R.string.play_again), (dialog, which) -> {
                   isGameOver = false;
                   score = 0;
                   lives = 3;
                   currentQuestionIndex = 0;
                   btn5050.setEnabled(true);
                   btn5050.setAlpha(1.0f);
                   btnSkip.setEnabled(true);
                   btnSkip.setAlpha(1.0f);
                   displayQuestion();
               })
               .setNegativeButton(getString(R.string.exit), (dialog, which) -> finish())
               .show();
    }

    private void saveResult() {
        String email = SharedPref.getInstance().getUser(this).getEmail();
        Attempt attempt = new Attempt(
                sessionStartTime,
                getString(R.string.survival_mode),
                sessionMaxScore / 10,
                3, // Giả sử tối đa 3 lỗi cho mỗi session
                sessionMaxScore,
                email
        );

        new SaveUserAttemptTask(attempt).execute();
    }

    private class SaveUserAttemptTask extends AsyncTask<Void, Void, Void> {
        private final Attempt attempt;

        public SaveUserAttemptTask(Attempt attempt) {
            this.attempt = attempt;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            UserDatabase databaseClient = UserDatabaseClient.getInstance(getApplicationContext());
            databaseClient.userDao().insertAttempt(attempt);
            return null;
        }
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.exit_survival_title))
                .setMessage(getString(R.string.exit_survival_message))
                .setPositiveButton(getString(R.string.exit), (dialog, i) -> finish())
                .setNegativeButton(getString(R.string.continue_text), null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}