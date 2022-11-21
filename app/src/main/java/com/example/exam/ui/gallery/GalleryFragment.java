package com.example.exam.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.exam.Question;
import com.example.exam.R;
import com.example.exam.SPUtil;
import com.example.exam.databinding.FragmentGalleryBinding;
import com.example.exam.grades;
import com.example.exam.ui.Gallery_Adapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Spliterator;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;



    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Gallery_Adapter ga;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        layoutManager = new LinearLayoutManager(requireActivity());
        recyclerView = binding.errorShow;
        ga = new Gallery_Adapter(requireActivity());
        ga.setAnswer(SPUtil.getObject(requireActivity(),"history_answer"));
        ga.setDate(SPUtil.getObject(requireActivity(),"history_date"));
        ga.setQuestion(SPUtil.getObject(requireActivity(),"history_question"));

        System.out.println(1);

        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(ga);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false));


        ga.setOnItemLongClickListener(new Gallery_Adapter.onItemLongClickListener() {
            @Override
            public boolean OnLongItemClick(int position) {
                AlertDialog.Builder normalDialog = new AlertDialog.Builder(requireActivity());
                normalDialog.setTitle("删除？");
                normalDialog.setMessage("确定删除 "+ ga.getDate().get(position)+" ?");
                normalDialog.setPositiveButton("确定", (dialog, which) -> {
                    ga.remove(position);
                });
                normalDialog.setNegativeButton("取消", (dialog, which) -> {
                });
                normalDialog.show();
                return true;
            }
        });



        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }
}