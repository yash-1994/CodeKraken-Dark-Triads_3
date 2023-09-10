package com.example.mess_mark_01.Utils;



import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.i18n.phonenumbers.*;
import com.example.mess_mark_01.R;
import java.util.Random;

public class Utils {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static String generateOTP(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("OTP length must be at least 1 digit");
        }
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10)); // Generate a random digit (0-9)
        }
        return otp.toString();
    }

    public static boolean isUserValide(String name, String number, String password) {
        return true;
    }

//    public static boolean loadFragment(FragmentManager manager, Fragment fragment) {
//        if (fragment != null) {
//             manager.beginTransaction()
//                    .replace(R.id.fragmentContainer, fragment)
//                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                    .commit();
//            return true;
//        }
//        return false;
//    }

    public static String convertToE164Format(String phoneNumber) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

        try {
            // Parse the phone number
            Phonenumber.PhoneNumber parsedPhoneNumber = phoneNumberUtil.parse(phoneNumber, null);

            // Check if the phone number is valid
            if (!phoneNumberUtil.isValidNumber(parsedPhoneNumber)) {
                // Handle invalid phone number
                return null;
            }

            // Format the phone number in E.164 format
            String e164PhoneNumber = phoneNumberUtil.format(parsedPhoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

            return e164PhoneNumber;
        } catch (NumberParseException e) {
            // Handle parsing errors
            e.printStackTrace();
            return null;
        }
    }

    public static void makeSnack(String st, View view){
        Snackbar.make( view,st, Snackbar.LENGTH_SHORT).show();
    }


    public static String generateUniqueID() {
        StringBuilder idBuilder = new StringBuilder();
        Random random = new Random();

        // Add current timestamp to the ID
        long timestamp = System.currentTimeMillis();
        idBuilder.append(timestamp);

        // Add random characters from CHARACTERS
        for (int i = 0; i < 12 - String.valueOf(timestamp).length(); i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            idBuilder.append(randomChar);
        }

        // Ensure the ID is exactly 12 characters long
        String id = idBuilder.toString();
        return id;
    }

}
