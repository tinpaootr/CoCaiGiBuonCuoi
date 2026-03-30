package com.sagarkhurana.quizforfun;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.Random;

public class QuickMathActivity extends AppCompatActivity {

    private TextView tvScore, tvEquation;
    private LinearProgressIndicator progressBar;
    private Button btnTrue, btnFalse;
    private int score = 0;
    private boolean isCorrectAnswer;
    private CountDownTimer countDownTimer;
    private final long INITIAL_TIME = 5000; // 5 seconds per question
    private long currentTimeLeft;
    private Random random = new Random();
    
    private long sessionStartTime;
    private int sessionMaxScore = 0;
    private boolean isGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_math);
        
        sessionStartTime = System.currentTimeMillis();

        tvScore = findViewById(R.id.tvScoreQuickMath);
        tvEquation = findViewById(R.id.tvEquationQuickMath);
        progressBar = findViewById(R.id.pbQuickMath);
        btnTrue = findViewById(R.id.btnTrue);
        btnFalse = findViewById(R.id.btnFalse);

        findViewById(R.id.ivBackQuickMath).setOnClickListener(v -> showExitDialog());

        btnTrue.setOnClickListener(v -> checkAnswer(true));
        btnFalse.setOnClickListener(v -> checkAnswer(false));

        progressBar.setMax(100);
        generateQuestion();
    }

    private void generateQuestion() {
        int a = random.nextInt(20) + 1;
        int b = random.nextInt(20) + 1;
        int operation = random.nextInt(2); // 0: +, 1: -
        int result;
        String opStr;

        if (operation == 0) {
            result = a + b;
            opStr = "+";
        } else {
            result = a - b;
            opStr = "-";
        }

        isCorrectAnswer = random.nextBoolean();
        if (!isCorrectAnswer) {
            int offset = random.nextInt(5) + 1;
            if (random.nextBoolean()) result += offset;
            else result -= offset;
        }

        tvEquation.setText(getString(R.string.quick_math_equation_format, a, opStr, b, result));
        startTimer();
    }

    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();

        currentTimeLeft = INITIAL_TIME;
        countDownTimer = new CountDownTimer(INITIAL_TIME, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentTimeLeft = millisUntilFinished;
                int progress = (int) (millisUntilFinished * 100 / INITIAL_TIME);
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                gameOver();
            }
        }.start();
    }

    private void checkAnswer(boolean userGuess) {
        if (userGuess == isCorrectAnswer) {
            score++;
            tvScore.setText(getString(R.string.score_text, score));
            generateQuestion();
        } else {
            gameOver();
        }
    }

    private void gameOver() {
        if (isGameOver) return;
        isGameOver = true;
        
        if (countDownTimer != null) countDownTimer.cancel();

        if (score > sessionMaxScore) {
            sessionMaxScore = score;
        }
        saveResult();

        new AlertDialog.Builder(this)
                .setTitle(R.string.time_up)
                .setMessage(getString(R.string.quick_math_game_over_message, score))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.play_again), (dialog, which) -> {
                    isGameOver = false;
                    score = 0;
                    tvScore.setText(getString(R.string.score_text, score));
                    generateQuestion();
                })
                .setNegativeButton(getString(R.string.exit), (dialog, which) -> finish())
                .show();
    }

    private void saveResult() {
        String email = com.sagarkhurana.quizforfun.other.SharedPref.getInstance().getUser(this).getEmail();
        com.sagarkhurana.quizforfun.data.Attempt attempt = new com.sagarkhurana.quizforfun.data.Attempt(
                sessionStartTime,
                getString(R.string.quick_math),
                sessionMaxScore,
                1,
                sessionMaxScore * 5,
                email
        );

        new android.os.AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                com.sagarkhurana.quizforfun.data.UserDatabase databaseClient =
                        com.sagarkhurana.quizforfun.data.UserDatabaseClient.getInstance(getApplicationContext());
                databaseClient.userDao().insertAttempt(attempt);
                return null;
            }
        }.execute();
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.exit_quick_math_title))
                .setMessage(getString(R.string.exit_survival_message))
                .setPositiveButton(getString(R.string.exit), (dialog, which) -> finish())
                .setNegativeButton(getString(R.string.continue_text), null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }
}