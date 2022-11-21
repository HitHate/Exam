package com.example.exam.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.exam.Choose_number;
import com.example.exam.R;
import com.example.exam.SPUtil;
import com.example.exam.databinding.FragmentHomeBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        TextView add = binding.add;
        TextView sub = binding.sub;
        TextView mix = binding.mix;




        add.setOnClickListener(onClickListener);
        sub.setOnClickListener(onClickListener);
        mix.setOnClickListener(onClickListener);



        return binding.getRoot();
    }

    View.OnClickListener onClickListener = view ->{
        int id = view.getId();
        if(id == R.id.add){
            Intent intent = new Intent(requireActivity(), Choose_number.class);
            intent.putExtra("op","+");
            startActivity(intent);
        }
        if(id == R.id.sub){
            Intent intent = new Intent(requireActivity(), Choose_number.class);
            intent.putExtra("op","-");
            startActivity(intent);
        }
        if(id == R.id.mix){
            Intent intent = new Intent(requireActivity(), Choose_number.class);
            intent.putExtra("op","+-");
            startActivity(intent);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}