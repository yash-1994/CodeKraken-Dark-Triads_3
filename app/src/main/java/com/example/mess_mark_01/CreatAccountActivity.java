package com.example.mess_mark_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import com.example.mess_mark_01.FireBase.FirebaseHelper;
import com.example.mess_mark_01.Model.User;
import com.example.mess_mark_01.Utils.Utils;
import com.example.mess_mark_01.databinding.ActivityCreateAccountBinding;
import com.example.mess_mark_01.databinding.*;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class CreatAccountActivity extends AppCompatActivity {

    public ActivityCreateAccountBinding binding;
    public FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;

    private FirebaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            helper = new FirebaseHelper();
            mAuth = FirebaseAuth.getInstance();
        }catch (Exception e){
            Log.d("TAG", "onCreate: "+e.getMessage());
        }


        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = binding.signUpPasswordEdittext.getText().toString();
                String email = binding.signUpEmail.getText().toString();
                String name = binding.signUpNameEdittext.getText().toString();
                String phoneNumber = binding.signUpNumberEdittext.getText().toString();
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Utils.makeSnack("Registered Suscessfully!",findViewById(android.R.id.content));
                                    User user = new User(name,phoneNumber,password,email);

                                    signInUser(user);
                                }else{
                                    Exception e = task.getException();
                                    Log.d("TAG", "onComplete: hmmm"+e.getMessage());
                                }
                            }
                        });

            }
        });

//
//        binding.signUpGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signInWithGoogle();
//            }
//        });
    }

//    private void signInWithGoogle(){
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .requestScopes(new Scope(Scopes.PLUS_ME), new Scope(Scopes.EMAIL), new Scope(Scopes.PROFILE), new Scope(Scopes.EMAIL))
//                .requestProfile()
//                .build();
//
//        // Create a GoogleSignInClient with the options
//        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        // Start the Google Sign-In process
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
    private void signInUser(User user) {
        mAuth.signInWithEmailAndPassword(user.getUserEmail(),user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            helper.addUser(user,mAuth.getUid());
                            finish();
                        }else{
                            Exception e = task.getException();
                            Log.d("TAG", "onComplete: "+e.getMessage());
                        }
                    }
                });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//
//            try {
//                // Successfully signed in with Google
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//
//                // Now, you have the GoogleSignInAccount object
//                // You can use it to get the user's information, including the ID token
//                String googleIdToken = account.getIdToken();
//
//                // Use the ID token to create a Firebase user
//                AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
//
//                mAuth.signInWithCredential(credential)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if(task.isSuccessful()){
//                                    FirebaseUser user = task.getResult().getUser();
//                                    helper.addUserThrouFireBaseUser(user);
//                                    Intent inten = new Intent(CreatAccountActivity.this,HomeActivity.class);
//                                    startActivity(inten);
//                                }else{
//                                    Exception e = task.getException();
//                                    Log.d("TAG", "onComplete: "+e.getMessage());
//                                }
//                            }
//                        });
////
//            } catch (ApiException e) {
//                // Google Sign-In failed, handle the error
//            }
//        }
//    }

}
