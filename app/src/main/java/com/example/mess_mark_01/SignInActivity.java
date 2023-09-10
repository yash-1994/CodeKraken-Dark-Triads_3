package com.example.mess_mark_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mess_mark_01.FireBase.FirebaseHelper;
import com.example.mess_mark_01.Utils.Utils;
import com.example.mess_mark_01.databinding.ActivitySignInBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.internal.Util;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    FirebaseHelper helper;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getUid()!=null){
            Intent inte = new Intent(SignInActivity.this,HomeActivity.class);
            startActivity(inte);
        }

        helper = new FirebaseHelper();
        binding.signInButto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                helper.isValideSignInUser(email, password, new FirebaseHelper.SignInCallback() {
                    @Override
                    public void onResult(String result) {
                        if (result.equals("ok")) {
                            // Sign in successful
                           Intent intent = new Intent(SignInActivity.this,HomeActivity.class);
                           startActivity(intent);
                        } else if (result.equals("not found")) {
                            Utils.makeSnack("Not found",findViewById(android.R.id.content));
                            // User not found
                        } else {
                            Log.d("TAG", "onResult: joye");
                            // Error occurred
                        }
                    }
                });
            }
        });

//        binding.forgatText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SignInActivity.this,VerifyNumberActivity.class);
//                startActivity(intent);
//            }
//        });
        binding.registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent inten = new Intent(SignInActivity.this,CreatAccountActivity.class);

                    startActivity(inten);
                }catch (Exception e){
                    Log.d("TAG", "onClick: ok"+e.getMessage());
                }

            }
        });
    }
}
