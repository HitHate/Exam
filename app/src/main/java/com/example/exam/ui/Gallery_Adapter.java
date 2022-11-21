package com.example.exam.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exam.R;
import com.example.exam.SPUtil;
import com.example.exam.grades;

import java.util.ArrayList;
import java.util.HashMap;

public class Gallery_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private ArrayList<String> date = new ArrayList<>();
    private HashMap<String,HashMap<String, HashMap<String, String>>> question = new HashMap<>();
    private HashMap<String,String[]> answer = new HashMap<>();
    private onItemLongClickListener onItemLongClickListener;

    public ArrayList<String> getDate() {
        return date;
    }

    public void setDate(ArrayList<String> date) {
        this.date = date;
    }

    public void setQuestion(HashMap<String, HashMap<String, HashMap<String, String>>> question) {
        this.question = question;
    }

    public void setAnswer(HashMap<String, String[]> answer) {
        this.answer = answer;
    }

    public Gallery_Adapter(Context context) {

        this.context = context;
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

    public void add(int position,String date_set){
        date.add(position,date_set);
    }

    public void remove(int position){
        question.remove(date.get(position));
        answer.remove(date.get(position));
        date.remove(position);
        SPUtil.setObject(context,"history_date",date);
        SPUtil.setObject(context,"history_question",question);
        SPUtil.setObject(context,"history_answer",answer);
        notifyItemRemoved(position);
    }


    public interface onItemLongClickListener{

        boolean OnLongItemClick(int position);
    }

    public void setOnItemLongClickListener(onItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
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
                    Intent push = new Intent(context, grades.class);
                    String position = date.get(getLayoutPosition());
                    push.putExtra("answer",answer.get(position));
                    push.putExtra("question",question.get(position));
                    context.startActivity(push);
                    System.out.println(position);
                    System.out.println(question.get(position));
                    System.out.println(answer.get(position));
                }
            });
        }
    }
}
