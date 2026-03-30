package com.sagarkhurana.quizforfun.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sagarkhurana.quizforfun.R;
import com.sagarkhurana.quizforfun.data.QuestionResult;

import java.util.List;

public class QuizDetailAdapter extends RecyclerView.Adapter<QuizDetailAdapter.ViewHolder> {

    private final List<QuestionResult> results;

    public QuizDetailAdapter(List<QuestionResult> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionResult result = results.get(position);
        holder.tvQuestion.setText(result.getQuestion());
        holder.tvUserAnswer.setText("Câu trả lời của bạn: " + result.getUserAnswer());
        holder.tvCorrectAnswer.setText("Đáp án đúng: " + result.getCorrectAnswer());

        if (result.isCorrect()) {
            holder.tvResultStatus.setText("ĐÚNG");
            holder.tvResultStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
            holder.tvUserAnswer.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            holder.tvResultStatus.setText("SAI");
            holder.tvResultStatus.setBackgroundColor(Color.parseColor("#F44336"));
            holder.tvUserAnswer.setTextColor(Color.parseColor("#F44336"));
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion, tvUserAnswer, tvCorrectAnswer, tvResultStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tvQuestionText);
            tvUserAnswer = itemView.findViewById(R.id.tvUserAnswer);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            tvResultStatus = itemView.findViewById(R.id.tvResultStatus);
        }
    }
}