package com.example.exam.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.exam.SPUtil;
import com.example.exam.databinding.FragmentSlideshowBinding;

import com.example.exam.ui.Gallery_Adapter;
import com.example.exam.ui.Slideshow_Adapter;
import com.google.android.material.snackbar.Snackbar;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Slideshow_Adapter sa;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        sa = new Slideshow_Adapter(requireActivity());
        recyclerView = binding.questionShow;

        swipeRefreshLayout = binding.fresh;
        sa.setDate(SPUtil.getObject(requireActivity(),"own_date"));
        sa.setQuestion(SPUtil.getObject(requireActivity(),"own_question"));

        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(sa);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false));


        sa.setOnItemLongClickListener(new Slideshow_Adapter.onItemLongClickListener() {
            @Override
            public boolean OnLongItemClick(int position) {
                AlertDialog.Builder normalDialog = new AlertDialog.Builder(requireActivity());
                normalDialog.setTitle("删除？");
                normalDialog.setMessage("确定删除 "+ sa.getDate().get(position)+" ?");
                normalDialog.setPositiveButton("确定", (dialog, which) -> {
                    sa.remove(position);
                });
                normalDialog.setNegativeButton("取消", (dialog, which) -> {
                });
                normalDialog.show();
                return true;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sa.setDate(SPUtil.getObject(requireActivity(),"own_date"));
                sa.setQuestion(SPUtil.getObject(requireActivity(),"own_question"));
                sa.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
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