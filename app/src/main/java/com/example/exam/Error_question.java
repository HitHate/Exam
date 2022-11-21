package com.example.exam;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Error_question extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_question);

        Intent intent = getIntent();

        ArrayList<String> answer = intent.getStringArrayListExtra("check");
        HashMap<String, HashMap<String, String>> question = (HashMap<String, HashMap<String, String>>) intent.getSerializableExtra("error");
        HashMap<String, String> total = question.get("total");
        HashMap<String, String> position = question.get("position");
        String op = Objects.requireNonNull(question.get("op")).get("op");
        ArrayList<String> own_date = SPUtil.getObject(this,"own_date");
        HashMap<String,HashMap<String,HashMap<String,String>>> own_question = SPUtil.getObject(this,"own_question");


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button save = findViewById(R.id.save);
        Button save_error_local = findViewById(R.id.save_error_local);

        Error_Adapter ea = new Error_Adapter(this);

        ea.setAnswer(answer);
        ea.setError(total);
        ea.setError_position(position);

        recyclerView.setAdapter(ea);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        ActivityResultLauncher<Intent> LOCAL_SAVE = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                try {
                    assert result.getData() != null;
                    FileOutputStream fileOutputStream = (FileOutputStream) getContentResolver().openOutputStream(result.getData().getData());
                    ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
                    outputStream.writeObject(question);
                    fileOutputStream.close();
                    outputStream.close();
                    Toast.makeText(Error_question.this, "题目成功保存至本地！", Toast.LENGTH_LONG).show();
                    save_error_local.setEnabled(false);
                } catch (IOException | AssertionError e) {
                    e.printStackTrace();
                    Toast.makeText(Error_question.this, "题目保存至本地失败！", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(Error_question.this, "题目保存至本地失败！", Toast.LENGTH_LONG).show();
            }
        });


        save_error_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent1.addCategory(Intent.CATEGORY_OPENABLE);
                intent1.setType("*/*");
                intent1.putExtra(Intent.EXTRA_TITLE, "我的错题");
                LOCAL_SAVE.launch(intent1);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(op, "+")) {
                    Date date = new Date();
                    own_date.add(0, date +"  错题  "+"加法  "+question.get("total").size()+"道题");
                    own_question.put(date +"  错题  "+"加法  "+question.get("total").size()+"道题",question);
                }
                if (Objects.equals(op, "-")) {
                    Date date = new Date();
                    own_date.add(0, date +"  错题  "+"减法  "+question.get("total").size()+"道题");
                    own_question.put(date +"  错题  "+"减法  "+question.get("total").size()+"道题",question);
                }
                if (Objects.equals(op, "+-")) {
                    Date date = new Date();
                    own_date.add(0, date +"  错题  "+"混合加减  "+question.get("total").size()+"道题");
                    own_question.put(date +"  错题  "+"混合加减  "+question.get("total").size()+"道题",question);
                }
                SPUtil.setObject(Error_question.this,"own_date",own_date);
                SPUtil.setObject(Error_question.this,"own_question",own_question);
                System.out.println(own_date.size());
                Toast.makeText(Error_question.this,"题目已经保存至题库！",Toast.LENGTH_LONG).show();
                save.setEnabled(false);
            }
        });

    }
}