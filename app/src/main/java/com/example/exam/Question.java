package com.example.exam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;


import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.gridlayout.widget.GridLayout;


import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.Objects;

public class Question extends AppCompatActivity {


    HashMap<String,HashMap<String,HashMap<String,String>>> history_question ;
    HashMap<String,String[]> history_answer;
    ArrayList<String> history_date;
    String op;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);



        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.show_all);
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        history_question = SPUtil.getObject(this,"history_question");
        history_answer  = SPUtil.getObject(this,"history_answer");
        history_date = SPUtil.getObject(this,"history_date");

        Intent intent = getIntent();
        final int[] i = {1};
        int time_set = 0;
        TextView question = findViewById(R.id.question);
        EditText answer = findViewById(R.id.answer);
        TextView time = findViewById(R.id.time);
        TextView number = findViewById(R.id.number);
        TextView now = findViewById(R.id.now);
        TextView card = findViewById(R.id.card);
        Button last = findViewById(R.id.last);
        Button next = findViewById(R.id.next);
        Button commit = findViewById(R.id.commit);
        GridLayout sl = findViewById(R.id.show_list);
        Button button = findViewById(R.id.back);

        now.setText("1");


        last.setEnabled(false);

        Toast toast =new Toast(this);
        View out = LayoutInflater.from(Question.this).inflate(R.layout.time_out, null);
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(Question.this);
        HashMap<String, HashMap<String, String>> question_list = (HashMap<String, HashMap<String, String>>) intent.getSerializableExtra("question");
        HashMap<String, String> total = question_list.get("total");
        HashMap<String, String> position = question_list.get("position");
        HashMap<String, String> operation = question_list.get("op");
        assert operation != null;
        op = operation.get("op");

        next.setEnabled(position.size()!=1);
        assert position != null;
        final String[] check = new String[position.size()];
        number.setText(String.valueOf(position.size()));
        System.out.println(position);
        System.out.println(total);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        System.out.println(displayMetrics.widthPixels);
        int fit = displayMetrics.widthPixels / 6;
        int margin =(displayMetrics.widthPixels - (fit * 5)) /6;
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(out);


        for (int q = 1; q < position.size() + 1; q++) {
            View li = LayoutInflater.from(Question.this).inflate(R.layout.show, null);
            TextView tv = li.findViewById(R.id.num);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)tv.getLayoutParams();

            lp.width = fit;
            lp.height = fit;
            lp.leftMargin = margin;
            lp.topMargin = 30;
            tv.setLayoutParams(lp);

            tv.setText(String.valueOf(q));
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check[i[0] - 1] = String.valueOf(answer.getText());
                    TextView tv_now = sl.findViewById(i[0]);
                    if(check[i[0] - 1] != null && !check[i[0] - 1].equals("")){
//                        tv_now.setBackgroundColor(getResources().getColor(R.color.teal_200));
                        tv_now.setBackground(AppCompatResources.getDrawable(Question.this,R.drawable.circle_blue));
                    }else {
//                        tv_now.setBackgroundColor(getResources().getColor(R.color.white));
                        tv_now.setBackground(AppCompatResources.getDrawable(Question.this,R.drawable.circle));
                    }
                    i[0] = Integer.parseInt((String) tv.getText());
                    now.setText(String.valueOf(i[0]));

                    next.setEnabled(i[0] < position.size());

                    last.setEnabled(i[0] > 1);

                    question.setText(position.get(Integer.toString(i[0])));
                    answer.setText(check[i[0] - 1]);
                    answer.setSelection(answer.getText().toString().length());

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    assert fragment != null;
                    transaction.hide(fragment);
                    transaction.commit();

                }
            });
            tv.setId(q);
            sl.addView(li);
        }


        assert fragment != null;
        transaction.hide(fragment).commit();


        question.setText(position.get("1"));

        answer.setText(check[0]);

        if (position.size() != 0) {
            time_set = 72000 * position.size();
        }

        new CountDownTimer(time_set, 1000) {

            public void onTick(long millisUntilFinished) {
                String s = millisUntilFinished / 60000 + "分" + millisUntilFinished / 1000 % 60 + "秒";
                time.setText(s);
                if(Question.this.isFinishing()){
                    cancel();
                }
            }

            public void onFinish() {

                cancel();
                toast.show();
                check[i[0] - 1] = String.valueOf(answer.getText());
                store(check,question_list, Question.this);
                Intent push = new Intent(Question.this,grades.class);
                push.putExtra("answer",check);
                push.putExtra("question",question_list);
                startActivity(push);
                Question.this.finish();

            }
        }.start();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check[i[0] - 1] = String.valueOf(answer.getText());
                TextView tv = sl.findViewById(i[0]);
                if(check[i[0] - 1] != null && !check[i[0] - 1].equals("")){
//                    tv.setBackgroundColor(getResources().getColor(R.color.teal_200));
                    tv.setBackground(AppCompatResources.getDrawable(Question.this,R.drawable.circle_blue));
                }else {
//                    tv.setBackgroundColor(getResources().getColor(R.color.white));
                    tv.setBackground(AppCompatResources.getDrawable(Question.this,R.drawable.circle));
                }
                i[0] = i[0] + 1;
                answer.setText(check[i[0] - 1]);
                answer.setSelection(answer.getText().toString().length());
                now.setText(String.valueOf(i[0]));
                last.setEnabled(true);
                question.setText(position.get(Integer.toString(i[0])));
                if (i[0] >= position.size()) {
                    next.setEnabled(false);
                }
            }
        });
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check[i[0] - 1] = String.valueOf(answer.getText());
                TextView tv = sl.findViewById(i[0]);
                if(check[i[0] - 1] != null && !check[i[0] - 1].equals("")){
//                    tv.setBackgroundColor(getResources().getColor(R.color.teal_200));
                    tv.setBackground(AppCompatResources.getDrawable(Question.this,R.drawable.circle_blue));
                }else {
//                    tv.setBackgroundColor(getResources().getColor(R.color.white));
                    tv.setBackground(AppCompatResources.getDrawable(Question.this,R.drawable.circle));
                }
                i[0] = i[0] - 1;
                now.setText(String.valueOf(i[0]));
                next.setEnabled(true);
                question.setText(position.get(Integer.toString(i[0])));
                answer.setText(check[i[0] - 1]);
                answer.setSelection(answer.getText().toString().length());
                if (i[0] < 2) {
                    last.setEnabled(false);
                }
            }
        });
        now.setOnClickListener(v -> {
            next.setEnabled(false);
            last.setEnabled(false);
            FragmentTransaction transaction13 = fragmentManager.beginTransaction();
            transaction13.show(fragment);
            transaction13.commit();
        });
        number.setOnClickListener(v -> {
            next.setEnabled(false);
            last.setEnabled(false);
            FragmentTransaction transaction12 = fragmentManager.beginTransaction();
            transaction12.show(fragment);
            transaction12.commit();
        });
        card.setOnClickListener(v -> {
            next.setEnabled(false);
            last.setEnabled(false);
            FragmentTransaction transaction1 = fragmentManager.beginTransaction();
            transaction1.show(fragment);
            transaction1.commit();
        });

        button.setOnClickListener(v -> {
            next.setEnabled(i[0] < position.size());
            last.setEnabled(i[0] > 1);
            FragmentTransaction transaction14 = fragmentManager.beginTransaction();
            transaction14.hide(fragment);
            transaction14.commit();
        });
        commit.setOnClickListener(v -> {
            check[i[0] - 1] = String.valueOf(answer.getText());
            TextView tv = sl.findViewById(i[0]);
            if(check[i[0] - 1] != null && !check[i[0]-1].equals("")){
                tv.setBackgroundColor(getResources().getColor(R.color.teal_200));
            }else {
                tv.setBackgroundColor(getResources().getColor(R.color.white));
            }

            for(String s : check){
                if(s == null || s.equals("")){
                    normalDialog.setTitle("提交");
                    normalDialog.setMessage("还有试题未完成，确定提交试卷？");
                    normalDialog.setPositiveButton("确定", (dialog, which) -> {
                        store(check,question_list, this);
                        Intent push = new Intent(Question.this,grades.class);
                        push.putExtra("answer",check);
                        push.putExtra("question",question_list);
                        startActivity(push);
                        Question.this.finish();
                    });
                    normalDialog.setNegativeButton("取消", (dialog, which) -> {
                    });
                    normalDialog.show();
                    return;
                }
            }
            normalDialog.setTitle("提交");
            normalDialog.setMessage("确定提交试卷？");
            normalDialog.setPositiveButton("确定", (dialog, which) -> {
                store(check,question_list, this);
                Intent push = new Intent(Question.this,grades.class);
                push.putExtra("answer",check);
                push.putExtra("question",question_list);
                startActivity(push);
                Question.this.finish();
            });
            normalDialog.setNegativeButton("取消", (dialog, which) -> {
            });
            normalDialog.show();
        });
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder normalDialog = new AlertDialog.Builder(Question.this);
        normalDialog.setTitle("退出");
        normalDialog.setMessage("确定退出考试？");
        normalDialog.setPositiveButton("确定", (dialog, which) -> {
            Question.this.finish();
            super.onBackPressed();
        });
        normalDialog.setNegativeButton("取消", (dialog, which) -> {
        });
        normalDialog.show();
    }

    public void store(String[] answer, HashMap<String,HashMap<String,String>> question_list, Context context){

        if (Objects.equals(op, "+")) {
            Date date = new Date();
            history_date.add(0, date +"   "+"加法  "+question_list.get("total").size()+"道题");
            history_answer.put(date +"   "+"加法  "+question_list.get("total").size()+"道题",answer);
            history_question.put(date +"   "+"加法  "+question_list.get("total").size()+"道题",question_list);
        }
        if (Objects.equals(op, "-")) {
            Date date = new Date();
            history_date.add(0, date +"   "+"减法  "+question_list.get("total").size()+"道题");
            history_answer.put(date +"   "+"减法  "+question_list.get("total").size()+"道题",answer);
            history_question.put(date +"   "+"减法  "+question_list.get("total").size()+"道题",question_list);
        }
        if (Objects.equals(op, "+-")) {
            Date date = new Date();
            history_date.add(0, date +"   "+"混合加减  "+question_list.get("total").size()+"道题");
            history_answer.put(date +"   "+"混合加减  "+question_list.get("total").size()+"道题",answer);
            history_question.put(date +"   "+"混合加减  "+question_list.get("total").size()+"道题",question_list);
        }
        SPUtil.setObject(context,"history_date",history_date);
        SPUtil.setObject(context,"history_question",history_question);
        SPUtil.setObject(context,"history_answer",history_answer);

    }
}