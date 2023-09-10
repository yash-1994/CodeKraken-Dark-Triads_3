package com.example.mess_mark_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mess_mark_01.Adapter.ProductHomeAdapter;
import com.example.mess_mark_01.Model.Product;
import com.example.mess_mark_01.Model.User;
import com.example.mess_mark_01.databinding.ActivityDetailsBinding;
import com.example.mess_mark_01.databinding.ActivityHomeBinding;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    ActivityDetailsBinding binding;
    ArrayList<Product> list;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user");

        Intent inten = getIntent();
        String ID = inten.getStringExtra("id");
        String URI = inten.getStringExtra("uri");
        String DES = inten.getStringExtra("dec");
        String PRICES = inten.getStringExtra("price");
        String OWNERID = inten.getStringExtra("owner");
        String NAME = inten.getStringExtra("name");

        String LO = inten.getStringExtra("loc");
        user = new User();
        binding.protag.setText(NAME);
        binding.prodescription.setText(DES);
        binding.location.setText(LO);
        Picasso.get()
                .load(URI)
                .into(binding.proimage);
        binding.proprice.setText(PRICES);

        databaseReference.child(OWNERID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User with the given ID exists
                    user = dataSnapshot.getValue(User.class);
                    binding.ownerNmae.setText(user.getUserName());

                    // Now, you have the user's data in the 'user' object
                    // You can update your UI or perform any further actions with it
                } else {
                    // User with the given ID does not exist
                    // Handle this case as needed
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur
            }
        });

        binding.callOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall(user.getPhoneNumber());
            }
        });

    }

    public void makeCall(String number){
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (callIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(callIntent);
        } else {

            // Handle the case where no app can handle the call intent
            Toast.makeText(this, "No app available to make a call."+number, Toast.LENGTH_SHORT).show();
        }

    }
}