package com.example.exam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.HashMap;

public class Error_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<String> answer;
    private HashMap<String, String> error_position;
    private HashMap<String, String> error;
    private Context context;

    public void setAnswer(ArrayList<String> answer) {
        this.answer = answer;
    }

    public void setError_position(HashMap<String, String> error_position) {
        this.error_position = error_position;
    }

    public void setError(HashMap<String, String> error) {
        this.error = error;
    }

    public Error_Adapter(Context context) {

        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.error_question_show, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.question.setText(error_position.get(String.valueOf(position+1)));
        myViewHolder.answer.setText(answer.get(position));
        myViewHolder.current_answer.setText(error.get(error_position.get(String.valueOf(position+1))));
        myViewHolder.your_answer.setText(answer.get(position));
    }

    @Override
    public int getItemCount() {
        return error.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView question;
        private TextView answer;
        private TextView current_answer;
        private TextView your_answer;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
            current_answer = itemView.findViewById(R.id.current_answer);
            your_answer = itemView.findViewById(R.id.your_answer);
        }
    }

}
