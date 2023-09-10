package com.example.mess_mark_01;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mess_mark_01.Adapter.CropIListtemAdapter;
import com.example.mess_mark_01.Adapter.ProductHomeAdapter;
import com.example.mess_mark_01.FireBase.FirebaseHelper;
import com.example.mess_mark_01.Model.Product;
import com.example.mess_mark_01.Model.User;
import com.example.mess_mark_01.Utils.Utils;
import com.example.mess_mark_01.databinding.ActivityAddCropsBinding;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Arrays;


public class AddCropsActivity extends AppCompatActivity {

    ActivityAddCropsBinding binding;
    Uri imageURI;
    FirebaseHelper helper;
    FirebaseAuth mAuth;

    String selectedItemName = "Nothing", selectedCityName = "Nothing", title = null, description = null, itemQuantity = null, cropProce = null, number = null;
    boolean isUserAddImage = false;
    private static final int PICK_IMAGE_FROM_GALLERY = 1;
    private static final int PICK_IMAGE_FROM_CAMERA = 2;
    private Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCropsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        helper = new FirebaseHelper();
        mAuth = FirebaseAuth.getInstance();


        String[] items = {"Cotton", "Lentil", "Millet", "Mung Beans", "Peanut", "Rice", "Soybean", "Wheat"};
        int[] imageIds = {R.drawable.li_cotton_icon, R.drawable.li_lentil_icon, R.drawable.li_millet_icon, R.drawable.li_mung_beans_icon, R.drawable.li_peanut_icon, R.drawable.li_rice_icon, R.drawable.li_soybean_icon, R.drawable.li_wheat_icon};

        CropIListtemAdapter adapter = new CropIListtemAdapter(this, items, imageIds);
        binding.spinner.setAdapter(adapter);

        // Set an OnItemSelectedListener for the Spinner
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the selected item text
                selectedItemName = items[position];

                // Display a Toast message with the selected item text

                // changing upload icon
                binding.uploadNewCropImage.setImageResource(imageIds[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing here
            }
        });


        // city address binding.spinner

        String[] cityList = {"Ahmedabad", "Amreli", "Anand", "Aravalli", "Banaskantha", "Bharuch", "Bhavnagar", "Botad", "Chhota Udaipur", "Dahod", "Dang", "Devbhoomi Dwarka", "Gandhinagar", "Gir Somnath", "Jamnagar", "Junagadh", "Kheda", "Kutch", "Mahisagar", "Mehsana", "Morbi", "Narmada", "Navsari", "Panchmahal", "Patan", "Porbandar", "Rajkot", "Sabarkantha", "Surat", "Surendranagar", "Tapi", "Valsad"};
        int temp = R.drawable.baseline_location_city_24;
        int[] cityImage = new int[32];
        Arrays.fill(cityImage, temp);

        CropIListtemAdapter cityAdapter = new CropIListtemAdapter(this, cityList, cityImage);
        binding.cityChosserSpinner.setAdapter(cityAdapter);

        binding.cityChosserSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Retrieve the selected item text
                selectedCityName = cityList[position];

                // Display a Toast message with the selected item text

                // changing upload icon

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing here
            }
        });
        binding.mainAddCropSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = binding.spinner.getSelectedItem().toString();
                String description = binding.mainAddCropDescription.getText().toString();
                String price = binding.mainAddCropPrice.getText().toString();
                String title = binding.mainAddCropTitle.getText().toString();
                String location = binding.cityChosserSpinner.getSelectedItem().toString();
                Product product = new Product(tag,description,price,mAuth.getUid(),imageURI.toString());
                product.setCity(location);
                helper.addProduct(product, imageURI, new FirebaseHelper.OnProductSavedListener() {
                    @Override
                    public void onProductSaved(boolean isSuccess) {
                        if(isSuccess){
                            Utils.makeSnack("Added sucessfully!",findViewById(android.R.id.content));
                            //sleep
                            finish();
                        }else{
                            Utils.makeSnack("something went wrong",findViewById(android.R.id.content));
                        }
                    }
                });
            }
        });
        //END
    }


    public void saveCrop(View view) {
        // Get the values from the EditText fields
        title = binding.mainAddCropTitle.getText().toString();
        description = binding.mainAddCropDescription.getText().toString();
        itemQuantity = binding.mainAddCropItemQuantity.getText().toString();
        cropProce = binding.mainAddCropPrice.getText().toString();
        number = binding.mainAddCropNumber.getText().toString();



        // Check if any of the fields are empty or if no image is selected
        if (title.isEmpty() || description.isEmpty() || itemQuantity.isEmpty() || cropProce.isEmpty() || !isUserAddImage || number.length() != 10) {
            // Display an alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("All fields and an image are required to save the crop.");
            builder.setPositiveButton("OK", null);
            builder.show();
        } else {
            helper.saveImage(imageURI, FirebaseAuth.getInstance().getCurrentUser());
            Utils.makeSnack("New Crop Added",findViewById(android.R.id.content));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        }
    }


    // image seliction
    // image selection
    public void selectImage(View view) {
        // Create an AlertDialog to let the user choose the image source
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(new CharSequence[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Choose from the gallery
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_IMAGE_FROM_GALLERY);
                        break;
                    case 1:
                        // Take a photo with the camera
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, PICK_IMAGE_FROM_CAMERA);
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_FROM_GALLERY) {
                if (data != null) {
                    Uri imageUri = data.getData();
                    isUserAddImage = true;
                    imageURI = imageUri;
                    binding.uploadImageIconTxtLinearlayout.setVisibility(View.GONE);
                    // Set the selected image in the ImageView
                    binding.mainUploadCropImage.setImageURI(imageUri);
                }
            } else if (requestCode == PICK_IMAGE_FROM_CAMERA) {
                if (data != null && data.getExtras() != null) {
                    // Get the captured image
                    Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), (android.graphics.Bitmap) data.getExtras().get("data"), null, null));
                    isUserAddImage = true;
                    imageURI = imageUri;
                    // Set the selected image in the ImageView
                    binding.mainUploadCropImage.setImageURI(imageUri);
                }
            }
        }
    }


    public void onExit(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Confirmation")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform exit action here, for example, finish the activity
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
    //FULL END

}