package com.example.mess_mark_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mess_mark_01.FireBase.FirebaseHelper;
import com.example.mess_mark_01.databinding.ActivityVerifyNumberBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
public class VerifyNumberActivity extends AppCompatActivity {

    private static String NUMBER = "";
    private ActivityVerifyNumberBinding binding;
    private String number = "";
    public String VERIFICATION_ID = "",ENTEREDOTP = "";
    public FirebaseAuth mAuth;
    public FirebaseHelper heler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVerifyNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        heler = new FirebaseHelper();

        Intent intent = getIntent();

        //SETUP NUMBER FOR SEND OTP
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+1234567890") // Replace with user's phone number
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        // Save the verification ID
                        VERIFICATION_ID = s;
                    }

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        // Automatically verify the code on some devices
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // Handle verification failure
                    }
                })
                .build();

        //SEND OTP
        try{
            PhoneAuthProvider.verifyPhoneNumber(options);
        }catch (Exception e){
            Log.d("TAG", "send it: "+e.getMessage());
        }

        binding.verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ENTEREDOTP==null || VERIFICATION_ID==null){
                    Log.d("TAG", "ID NULL: ");
                }else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VERIFICATION_ID, ENTEREDOTP);

                    signInWithNumber(credential);
                }
            }
        });

        binding.characterBox1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    ENTEREDOTP=s.toString();
                   binding.characterBox2.requestFocus(); // Move focus to the second box
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.characterBox2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    ENTEREDOTP=ENTEREDOTP+s.toString();
                    binding.characterBox3.requestFocus(); // Move focus to the third box
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.characterBox3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    ENTEREDOTP=ENTEREDOTP+s.toString();
                    binding.characterBox4.requestFocus(); // Move focus to the fourth box
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.characterBox4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Optionally, you can implement logic to handle the completion of the OTP entry here.
                ENTEREDOTP=ENTEREDOTP+s.toString();
                if(ENTEREDOTP.length() != 4){
                    Toast.makeText(VerifyNumberActivity.this, ENTEREDOTP, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //END...
    }

    private void signInWithNumber(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(VerifyNumberActivity.this, "ok phone", Toast.LENGTH_SHORT).show();
                        }else{
                            Exception e = task.getException();
                            Log.d("TAG", "phoneenter: "+e.getMessage());
                        }
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // User signed in successfully
                    } else {
                       Exception e = task.getException();
                        Log.d("TAG", "signInWithPhoneAuthCredential: "+e.getMessage());
                    }
                });
    }
}