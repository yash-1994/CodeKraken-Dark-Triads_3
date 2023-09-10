package com.example.mess_mark_01.Fragments;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mess_mark_01.Adapter.ProductHomeAdapter;
import com.example.mess_mark_01.FireBase.FirebaseHelper;
import com.example.mess_mark_01.HomeActivity;
import com.example.mess_mark_01.Model.Product;
import com.example.mess_mark_01.R;
import com.example.mess_mark_01.databinding.FragmentHomeBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    public FragmentHomeBinding binding;


    Context context;

    DatabaseReference databaseReference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        databaseReference = FirebaseDatabase.getInstance().getReference("product");
        context = getActivity();
        binding.productRecyclerView.setLayoutManager(new GridLayoutManager(context,2));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return binding.getRoot();
    }

    public void makt(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}

