package com.example.exam;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;


public class grades extends AppCompatActivity {


    private  String op ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        Intent intent = getIntent();
        TextView grades = findViewById(R.id.grades);

        Button go = findViewById(R.id.go);
        Button go2 = findViewById(R.id.go2);
        Button save_local = findViewById(R.id.save_local);
        Button save = findViewById(R.id.save);

        String[] answer = intent.getStringArrayExtra("answer");
        HashMap<String, HashMap<String, String>> question = (HashMap<String, HashMap<String, String>>) intent.getSerializableExtra("question");
        HashMap<String, String> total = question.get("total");
        HashMap<String, String> position = question.get("position");
        HashMap<String, String> operation = question.get("op");
        assert operation != null;
        op = operation.get("op");
        HashMap<String, HashMap<String, String>> error = new HashMap<>();
        HashMap<String, String> error_position = new HashMap<>();
        HashMap<String, String> error_total = new HashMap<>();
        ArrayList<String> own_date = SPUtil.getObject(this,"own_date");
        HashMap<String,HashMap<String,HashMap<String,String>>> own_question = SPUtil.getObject(this,"own_question");




        ActivityResultLauncher<Intent> LOCAL_SAVE = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                try {
                    assert result.getData() != null;
                    FileOutputStream fileOutputStream = (FileOutputStream) getContentResolver().openOutputStream(result.getData().getData());
                    ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
                    outputStream.writeObject(question);
                    fileOutputStream.close();
                    outputStream.close();
                    Toast.makeText(grades.this, "题目已经成功保存至本地！", Toast.LENGTH_LONG).show();
                    save_local.setEnabled(false);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(grades.this, "题目保存至本地失败！", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(grades.this, "题目保存至本地失败！", Toast.LENGTH_LONG).show();
            }
        });


        assert position != null;
        ArrayList<String> error_answer = new ArrayList<>();
        if (position.size() != 0) {
            double a = 100.0 / position.size();
            int b = 0;
            int c = 1;
            for (int i = 1; i <= position.size(); i++) {

                assert total != null;
                if (Objects.equals(answer[i - 1], total.get(position.get(String.valueOf(i))))) {
                    b++;
                } else {
                    if (answer[i - 1] == null || answer[i - 1].equals("")) {
                        error_answer.add("未作答");
                    } else {
                        error_answer.add(answer[i - 1]);
                    }
                    error_total.put(position.get(String.valueOf(i)), total.get(position.get(String.valueOf(i))));
                    error_position.put(String.valueOf(c), position.get(String.valueOf(i)));
                    c++;
                }
            }
            error.put("position", error_position);
            error.put("total", error_total);
            error.put("op",operation);
            double result = new BigDecimal(a*b).setScale(1, RoundingMode.HALF_UP).doubleValue();
            if(result*10%10 == 0){
                grades.setText(String.valueOf((int)result));
                go.setEnabled((int)result != 100);
            }else{
                grades.setText(String.valueOf(result));
            }

        }

        System.out.println("现在的题"+error);
        System.out.println(error_answer.size());
        for (String s : error_answer) {
            System.out.println(s);
        }


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(grades.this,Error_question.class);
                intent1.putExtra("error",error);
                intent1.putExtra("check",error_answer);
                grades.this.startActivity(intent1);
            }
        });

        go2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(grades.this,Error_question_pager.class);
                intent1.putExtra("error",error);
                intent1.putExtra("check",error_answer);
                grades.this.startActivity(intent1);
            }
        });


        save_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                intent1.setType("*/*");
                intent1.putExtra(Intent.EXTRA_TITLE, "我的题目");
                LOCAL_SAVE.launch(intent1);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(op, "+")) {
                    Date date = new Date();
                    own_date.add(0, date +"   "+"加法  "+question.get("total").size()+"道题");
                    own_question.put(date +"   "+"加法  "+question.get("total").size()+"道题",question);
                }
                if (Objects.equals(op, "-")) {
                    Date date = new Date();
                    own_date.add(0, date +"   "+"减法  "+question.get("total").size()+"道题");
                    own_question.put(date +"   "+"减法  "+question.get("total").size()+"道题",question);
                }
                if (Objects.equals(op, "+-")) {
                    Date date = new Date();
                    own_date.add(0, date +"   "+"混合加减  "+question.get("total").size()+"道题");
                    own_question.put(date +"   "+"混合加减  "+question.get("total").size()+"道题",question);
                }
                SPUtil.setObject(grades.this,"own_date",own_date);
                SPUtil.setObject(grades.this,"own_question",own_question);
                Toast.makeText(grades.this,"题目已经保存至题库！",Toast.LENGTH_LONG).show();
                save.setEnabled(false);
            }
        });
    }
}