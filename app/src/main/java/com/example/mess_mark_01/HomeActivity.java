package com.example.mess_mark_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mess_mark_01.Adapter.ProductHomeAdapter;
import com.example.mess_mark_01.FireBase.FirebaseHelper;
import com.example.mess_mark_01.Model.Product;
import com.example.mess_mark_01.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    String searchTag = "";
    FirebaseAuth mAuth;
    public List<Product> productList;

    ProductHomeAdapter adapter;
    DatabaseReference databaseReference;
    FirebaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            binding = ActivityHomeBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            mAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference("product");
            helper = new FirebaseHelper();



            setSupportActionBar(binding.toolbar);
        }catch (Exception e){
            Log.d("TAG", "onCreate: "+e.getMessage());
        }



        productList = new ArrayList<>();



//        productList.add(new Product("wheate", "50", "sknd", "sjdbjkabsd", "cdcdscsdc"));
//        productList.add(new Product("wheate", "50", "sknd", "sjdbjkabsd", "cdcdscsdc"));
//        productList.add(new Product("wheate", "50", "sknd", "sjdbjkabsd", "cdcdscsdc"));
//        productList.add(new Product("wheate", "50", "sknd", "sjdbjkabsd", "cdcdscsdc"));
//        productList.add(new Product("wheate", "50", "sknd", "sjdbjkabsd", "cdcdscsdc"));
//        productList.add(new Product("wheate", "50", "sknd", "sjdbjkabsd", "cdcdscsdc"));
//

        adapter = new ProductHomeAdapter(this,productList);
        binding.productRecyclerView.setHasFixedSize(true);
        binding.productRecyclerView.setLayoutManager(new GridLayoutManager(this,2));


        binding.productRecyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("product"); // Replace with your Firebase database node


        Log.d("TAG", "onCreate: "+"ojksnda");






        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                try{
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product data = snapshot.getValue(Product.class);
                        productList.add(data);
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

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(HomeActivity.this,AddCropsActivity.class);
                startActivity(inten);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.log_out) {
            mAuth.signOut();
            finish();
        }
        return true;
    }

    void makt(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    public void send(View view) {

        searchTag = view.getTag().toString();
        Intent intent = new Intent(HomeActivity.this,SearchActivity.class);
        intent.putExtra("tag",searchTag);
        startActivity(intent);
    }
}
