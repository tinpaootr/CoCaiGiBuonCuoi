package com.sagarkhurana.quizforfun;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sagarkhurana.quizforfun.adapter.QuizDetailAdapter;
import com.sagarkhurana.quizforfun.data.QuestionResult;
import com.sagarkhurana.quizforfun.other.Constants;

import java.lang.reflect.Type;
import java.util.List;

public class QuizDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        String json = getIntent().getStringExtra(Constants.QUESTIONS_DETAILS);
        Type listType = new TypeToken<List<QuestionResult>>() {}.getType();
        List<QuestionResult> results = new Gson().fromJson(json, listType);

        RecyclerView rv = findViewById(R.id.rvQuizDetail);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new QuizDetailAdapter(results));

        findViewById(R.id.ivBackDetail).setOnClickListener(v -> finish());
    }
}