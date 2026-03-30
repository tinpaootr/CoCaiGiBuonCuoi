package com.sagarkhurana.quizforfun;

import android.content.DialogInterface;
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
import com.sagarkhurana.quizforfun.other.Utils;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survival);

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
            Toast.makeText(this, "Chính xác! +10 điểm", Toast.LENGTH_SHORT).show();
        } else {
            lives--;
            Toast.makeText(this, "Sai rồi! -1 mạng", Toast.LENGTH_SHORT).show();
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
        tvScore.setText("Điểm: " + score);
        tvLives.setText("Mạng: " + lives);
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
                Toast.makeText(SurvivalActivity.this, "Hết thời gian! -1 mạng", Toast.LENGTH_SHORT).show();
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
        if (countDownTimer != null) countDownTimer.cancel();
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kết thúc!")
               .setMessage("Bạn đã hết mạng.\nTổng điểm: " + score)
               .setCancelable(false)
               .setPositiveButton("Chơi lại", (dialog, which) -> {
                   score = 0;
                   lives = 3;
                   currentQuestionIndex = 0;
                   btn5050.setEnabled(true);
                   btn5050.setAlpha(1.0f);
                   btnSkip.setEnabled(true);
                   btnSkip.setAlpha(1.0f);
                   displayQuestion();
               })
               .setNegativeButton("Thoát", (dialog, which) -> finish())
               .show();
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Thoát chế độ sinh tồn?")
                .setMessage("Bạn có chắc chắn muốn thoát không?")
                .setPositiveButton("Thoát", (dialog, i) -> finish())
                .setNegativeButton("Tiếp tục", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
