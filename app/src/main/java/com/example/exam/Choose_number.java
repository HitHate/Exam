package com.example.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.exam.databinding.ActivityChooseNumberBinding;
import com.example.exam.databinding.FragmentHomeBinding;

import java.util.HashMap;
import java.util.Objects;

public class Choose_number extends AppCompatActivity {

    String op;
    Make_question mq = new Make_question();
    HashMap<String, HashMap<String, String>> question = new HashMap<>();
    HashMap<String,String> operation = new HashMap<>();
    private ActivityChooseNumberBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_number);
        Intent intent = getIntent();
        op =  intent.getStringExtra("op");
        operation.put("op",op);
        TextView ten = findViewById(R.id.ten);
        TextView fifty = findViewById(R.id.fifty);
        TextView hundred = findViewById(R.id.hundred);
        ten.setOnClickListener(onClickListener);
        fifty.setOnClickListener(onClickListener);
        hundred.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = view -> {
        int id = view.getId();
        if (id == R.id.ten) {
            if (Objects.equals(op, "+")) {
                question = mq.add(10);
                question.put("op",operation);
            }
            if (Objects.equals(op, "-")) {
                question = mq.sub(10);
                question.put("op",operation);
            }
            if (Objects.equals(op, "+-")) {
                question = mq.mix(10);
                question.put("op",operation);
            }
            Intent intent1 = new Intent(Choose_number.this, Question.class);
            intent1.putExtra("question", question);
            startActivity(intent1);

        } else if (id == R.id.fifty) {

            if (Objects.equals(op, "+")) {
                question = mq.add(50);
                question.put("op",operation);
            }
            if (Objects.equals(op, "-")) {
                question = mq.sub(50);
                question.put("op",operation);
            }
            if (Objects.equals(op, "+-")) {
                question = mq.mix(50);
                question.put("op",operation);
            }

            Intent intent1 = new Intent(Choose_number.this, Question.class);
            intent1.putExtra("question", question);
            startActivity(intent1);

        } else if (id == R.id.hundred) {

            if (Objects.equals(op, "+")) {
                question = mq.add(100);
                question.put("op",operation);
            }
            if (Objects.equals(op, "-")) {
                question = mq.sub(100);
                question.put("op",operation);
            }
            if (Objects.equals(op, "+-")) {
                question = mq.mix(100);
                question.put("op",operation);
            }
            Intent intent1 = new Intent(Choose_number.this, Question.class);
            intent1.putExtra("question", question);
            startActivity(intent1);

        }
    };

}