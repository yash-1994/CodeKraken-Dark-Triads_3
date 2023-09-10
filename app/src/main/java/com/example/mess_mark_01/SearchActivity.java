package com.example.mess_mark_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mess_mark_01.Adapter.ProductHomeAdapter;
import com.example.mess_mark_01.FireBase.FirebaseHelper;
import com.example.mess_mark_01.Model.Product;
import com.example.mess_mark_01.databinding.ActivitySearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    ProductHomeAdapter adapter;
    DatabaseReference databaseReference;
    FirebaseHelper helper;
    FirebaseAuth mAuth;
    String tag = "";
    public List<Product> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("product");
        helper = new FirebaseHelper();

        productList = new ArrayList<>();


        adapter = new ProductHomeAdapter(this,productList);
        binding.re.setHasFixedSize(true);
        binding.re.setLayoutManager(new GridLayoutManager(this,2));


        binding.re.setAdapter(adapter);
        binding.tagName.setText(tag);

        databaseReference = FirebaseDatabase.getInstance().getReference("product"); // Replace with your Firebase database node

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                try{
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product data = snapshot.getValue(Product.class);
                        if(data.getProTag().equals(tag)){
                            productList.add(data);
                        }

                        Log.d("TAG", "onDataChange: "+"ksdnfknsdsanfkansnca");
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    Log.d("TAG", "onDataChange: "+e.getMessage());
                }


                // Notify the RecyclerView adapter of data changes
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });

    }
}