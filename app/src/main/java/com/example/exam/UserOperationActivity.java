package com.example.exam;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exam.ui.Gallery_Adapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.exam.databinding.ActivityUserOperationBinding;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class UserOperationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityUserOperationBinding binding;
    private ArrayList<String> date = new ArrayList<>();
    private HashMap<String,HashMap<String, HashMap<String, String>>> question = new HashMap<>();
    private HashMap<String,String[]> answer = new HashMap<>();
    private ArrayList<String> own_name = new ArrayList<>();
    private HashMap<String,HashMap<String, HashMap<String, String>>> own_question = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserOperationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarUserOperation.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        if(SPUtil.getObject(this,"history_date") == null && SPUtil.getObject(this,"history_answer") == null && SPUtil.getObject(this,"history_question") == null){
            SPUtil.setObject(this,"history_date",date);
            SPUtil.setObject(this,"history_question",question);
            SPUtil.setObject(this,"history_answer",answer);
        }

        if(SPUtil.getObject(this,"own_name") == null  && SPUtil.getObject(this,"own_question") == null){
            SPUtil.setObject(this,"own_date",own_name);
            SPUtil.setObject(this,"own_question",own_question);
        }

        ActivityResultLauncher<Intent> GET_LOCAL = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK){
                try {
                    assert result.getData() != null;
                    if(result.getData().getData() != null){
                        FileInputStream fileInputStream = (FileInputStream) getContentResolver().openInputStream(result.getData().getData());
                        ObjectInputStream objectInputStream;
                        objectInputStream = new ObjectInputStream(fileInputStream);
                        HashMap<String, HashMap<String, String>> b = (HashMap<String, HashMap<String, String>>) objectInputStream.readObject();
                        save_name(builder,b);
                        System.out.println(b);
                    }else{
                        ClipData s = result.getData().getClipData();
                        for(int i = 0 ; i < s.getItemCount(); i++){
                            FileInputStream fileInputStream = (FileInputStream) getContentResolver().openInputStream(s.getItemAt(i).getUri());
                            ObjectInputStream objectInputStream;
                            objectInputStream = new ObjectInputStream(fileInputStream);
                            HashMap<String, HashMap<String, String>> b = (HashMap<String, HashMap<String, String>>) objectInputStream.readObject();
                            save_name(builder,b);
                            System.out.println(b);
                        }
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }else{
                Snackbar.make(binding.appBarUserOperation.coordinatorLayout, "题目导入失败！", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        binding.appBarUserOperation.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                intent2.setType("*/*");
                intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                GET_LOCAL.launch(intent2);
            }
        });

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_user_operation);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_operation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_user_operation);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    public void save_name (AlertDialog.Builder builder, HashMap<String,HashMap<String,String>> question){

        View view = this.getLayoutInflater().inflate(R.layout.name, null);
        EditText name = view.findViewById(R.id.name);
        builder.setView(view)
                .setTitle("取个名字吧")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String yourName = String.valueOf(name.getText());
                        save(yourName,question);
                    }
                })
                .setNegativeButton("使用默认名字", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date date = new Date();
                        String op = question.get("op").get("op");
                        String name;
                        if (Objects.equals(op, "+")) {
                            name = date + "   " + "加法  " + question.get("total").size() + "道题";
                            save(name,question);
                        }
                        if (Objects.equals(op, "-")) {
                            name = date + "   " + "减法  " + question.get("total").size() + "道题";
                            save(name,question);
                        }
                        if (Objects.equals(op, "+-")) {
                            name = date +"   "+"混合加减  "+question.get("total").size()+"道题";
                            save(name,question);
                        }

                    }
                });
        builder.show();
    }

    public void save(String name , HashMap<String,HashMap<String,String>> question){
        ArrayList<String> own_date = SPUtil.getObject(this,"own_date");
        HashMap<String,HashMap<String, HashMap<String, String>>> own_question = SPUtil.getObject(this,"own_question");

        own_date.add(0, name);
        own_question.put(name,question);

        SPUtil.setObject(this,"own_date",own_date);
        SPUtil.setObject(this,"own_question",own_question);
        Snackbar.make(binding.appBarUserOperation.coordinatorLayout, "题目导入成功！", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

}