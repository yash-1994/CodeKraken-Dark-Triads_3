package com.example.mess_mark_01.FireBase;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mess_mark_01.Adapter.ProductHomeAdapter;
import com.example.mess_mark_01.HomeActivity;
import com.example.mess_mark_01.Model.Product;
import com.example.mess_mark_01.Model.User;
import com.example.mess_mark_01.Utils.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.play.integrity.internal.e;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class FirebaseHelper {

    private final FirebaseDatabase database;
    private DatabaseReference userRefrence;
    public DatabaseReference productRef;
    private FirebaseStorage storage;
    private StorageReference imageRef;


    public FirebaseHelper() {
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        userRefrence = database.getReference("user");
        productRef = database.getReference("product");
        imageRef = storage.getReference("Product Pic");
    }

    public void signUpToUser(User user) {
        // Generate a unique key for the user
        try{
            String id = Utils.generateUniqueID();
            String userId = userRefrence.push().getKey();
            if (userId != null) {
                user.setUserID(userId);
                userRefrence.child(userId).setValue(user); // Save the user object under the generated key
            }
        }catch (Exception e){
            Log.d("TAGE", "signUpToUser: "+e.getMessage());
        }
    }

    public void saveImage(Uri imageUri, FirebaseUser userId){
        imageRef = imageRef.child(userId.getUid()+"->");
        imageRef = imageRef.child(userId+".jpg");
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("TAGE", "onSuccess: "+"ok");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAGE", "onFailure: "+e.getMessage());
                    }
                });
    }

    public ArrayList<Product> fetchdata() {
        ArrayList<Product> list = new ArrayList<>();
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap: snapshot.getChildren()){
                    Product p = snap.getValue(Product.class);
                    list.add(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return list;
    }


    public interface SignInCallback {
        void onResult(String result);
    }

    public void isValideSignInUser(String email, String password, final SignInCallback callback) {
        userRefrence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isValid = false;
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    String em = user.getUserEmail();
                    String ps = user.getPassword();
                    if (em.equals(email) && ps.equals(password)) {
                        isValid = true;
                        break;
                    }
                }
                if (isValid) {
                    callback.onResult("ok");
                } else {
                    callback.onResult("not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onResult("error");
                Exception e = error.toException();
                Log.d("TAG", "SignIn Verify Error: " + e.getMessage());
            }
        });
    }

    public void addUser(User user,String id){
        try{
            user.setUserID(id);
            userRefrence.child(id).setValue(user);
        }catch(Exception e){
            Log.d("TAG", "addUser: "+e.getMessage());
        }
    }

    public void addUserThrouFireBaseUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setUserName(firebaseUser.getDisplayName());
        user.setUserEmail(firebaseUser.getEmail());
        user.setPhoneNumber(firebaseUser.getPhoneNumber());
        addUser(user,firebaseUser.getUid());
    }







    public interface OnProductSavedListener {
        void onProductSaved(boolean isSuccess);
    }

    public void addProduct(Product product, Uri imageURI,final OnProductSavedListener listener) {

        String productId = productRef.push().getKey();
        productRef.child(productId).setValue(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Product data saved successfully, now upload the image to Firebase Storage
                        StorageReference imageRef = storage.getReference("Product").child("product_images").child(productId + ".jpg");

                        imageRef.putFile(imageURI)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Image uploaded successfully, get the download URL
                                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri downloadUri) {
                                                // Update the product object with the image URL
                                                product.setProID(productId);
                                                product.setProPicUrl(downloadUri.toString());
                                                productRef.child(productId).setValue(product); // Update the product with the image URL
                                                listener.onProductSaved(true);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                listener.onProductSaved(false);
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        listener.onProductSaved(false);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onProductSaved(false);
                    }
                });
    }

    public ArrayList<Product> fetch(ProductHomeAdapter adapter){
        ArrayList<Product> list = new ArrayList<>();
        productRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product data = snapshot.getValue(Product.class);
                    list.add(data);
                }

                // Notify the RecyclerView adapter of data changes
                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
        return list;
    }
}



