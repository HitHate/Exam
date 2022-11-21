package com.example.exam.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.Question;
import com.example.exam.R;
import com.example.exam.SPUtil;
import com.example.exam.grades;

import java.util.ArrayList;
import java.util.HashMap;

public class Slideshow_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private ArrayList<String> date = new ArrayList<>();
    private HashMap<String,HashMap<String, HashMap<String, String>>> question = new HashMap<>();

    private onItemLongClickListener onItemLongClickListener;

    public void setDate(ArrayList<String> date) {
        this.date = date;
    }

    public void setQuestion(HashMap<String, HashMap<String, HashMap<String, String>>> question) {
        this.question = question;
    }

    public ArrayList<String> getDate() {
        return date;
    }

    public HashMap<String, HashMap<String, HashMap<String, String>>> getQuestion() {
        return question;
    }

    public interface onItemLongClickListener{
        boolean OnLongItemClick(int position);
    }

    public void setOnItemLongClickListener(onItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


    public Slideshow_Adapter(Context context) {
        this.context = context;
    }

    public void remove(int position){
        question.remove(date.get(position));
        date.remove(position);
        SPUtil.setObject(context,"own_date",date);
        SPUtil.setObject(context,"own_question",question);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_show, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.date_op.setText(date.get(position));
    }

    @Override
    public int getItemCount() {
        return date.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView date_op;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date_op = itemView.findViewById(R.id.history);
            date_op.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if( onItemLongClickListener != null ){
                        onItemLongClickListener.OnLongItemClick(getLayoutPosition());
                    }
                    return true;
                }
            });

            date_op.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent push = new Intent(context, Question.class);
                    String position = date.get(getLayoutPosition());
                    push.putExtra("question",question.get(position));
                    context.startActivity(push);
                    System.out.println(question.get(position));
                }
            });
        }
    }
}
