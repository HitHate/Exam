package com.example.exam;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class Error_question_pager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_question_pager);

        Intent intent = getIntent();

        ArrayList<String> answer = intent.getStringArrayListExtra("check");
        HashMap<String, HashMap<String, String>> question = (HashMap<String, HashMap<String, String>>) intent.getSerializableExtra("error");
        HashMap<String, String> total = question.get("total");
        HashMap<String, String> position = question.get("position");
        String op = Objects.requireNonNull(question.get("op")).get("op");
        ArrayList<String> own_date = SPUtil.getObject(this,"own_date");
        HashMap<String,HashMap<String,HashMap<String,String>>> own_question = SPUtil.getObject(this,"own_question");


        ViewPager2 viewpager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        Button save = findViewById(R.id.save);
        Button save_error_local = findViewById(R.id.save_error_local);

        Error_Pager_Adapter epa = new Error_Pager_Adapter(this);

        epa.setAnswer(answer);
        epa.setError(total);
        epa.setError_position(position);

        viewpager.setAdapter(epa);

        viewpager.setOffscreenPageLimit(3);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        int fit = displayMetrics.widthPixels / 5;

        new TabLayoutMediator(tabLayout, viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
           @Override
           public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//               View li = LayoutInflater.from(Error_question_pager.this).inflate(R.layout.show, null);
//               TextView tv = li.findViewById(R.id.num);
               tab.setText(""+(position+1));
//               tab.setCustomView(li);
           }
        }).attach();


        viewpager.setPageTransformer(new DepthPageTransformer());

        ActivityResultLauncher<Intent> LOCAL_SAVE = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                try {
                    assert result.getData() != null;
                    FileOutputStream fileOutputStream = (FileOutputStream) getContentResolver().openOutputStream(result.getData().getData());
                    ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
                    outputStream.writeObject(question);
                    fileOutputStream.close();
                    outputStream.close();
                    Toast.makeText(Error_question_pager.this, "题目成功保存至本地！", Toast.LENGTH_LONG).show();
                    save_error_local.setEnabled(false);
                } catch (IOException | AssertionError e) {
                    e.printStackTrace();
                    Toast.makeText(Error_question_pager.this, "题目保存至本地失败！", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(Error_question_pager.this, "题目保存至本地失败！", Toast.LENGTH_LONG).show();
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
                SPUtil.setObject(Error_question_pager.this,"own_date",own_date);
                SPUtil.setObject(Error_question_pager.this,"own_question",own_question);
                System.out.println(own_date.size());
                Toast.makeText(Error_question_pager.this,"题目已经保存至题库！",Toast.LENGTH_LONG).show();
                save.setEnabled(false);
            }
        });

    }

    @RequiresApi(21)
    public class DepthPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setTranslationZ(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);
                // Move it behind the left page
                view.setTranslationZ(-1f);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }

}