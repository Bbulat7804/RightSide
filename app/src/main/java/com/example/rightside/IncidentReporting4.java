package com.example.rightside;

import static com.example.rightside.Manager.*;
import static com.example.rightside.Manager.userHomePage;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Intent;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class IncidentReporting4 extends AppCompatActivity {

    private CheckBox impact1, impact2, impact3, impact4, action1, action2, action3;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_incident_reporting_4);

        // Initialize checkboxes
        impact1 = findViewById(R.id.Impact1);
        impact2 = findViewById(R.id.Impact2);
        impact3 = findViewById(R.id.Impact3);
        impact4 = findViewById(R.id.Impact4);
        action1 = findViewById(R.id.Action1);
        action2 = findViewById(R.id.Action2);
        action3= findViewById(R.id.Action3);

        // Initialize buttons
        ImageButton viewReportButton = findViewById(R.id.imageButton6);
        Button saveButton = findViewById(R.id.button4); // Save button
        Button submitButton = findViewById(R.id.button3); // Submit button

        // Load saved checkbox states
        loadSavedData();

        // Set click listener for Save button
        saveButton.setOnClickListener(v -> {
            // Save user data into the singleton
            saveUserData();

            // Show a toast message to indicate saving
            Toast.makeText(this, "Info Saved", Toast.LENGTH_SHORT).show();
        });

        // Set click listener for Submit button
        submitButton.setOnClickListener(v -> {
            // Save user data before submission
            saveUserData();
            UserDataSingleton data = UserDataSingleton.getInstance();
            data.uploadReport();
            // Show a toast message to indicate submission
            Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();

            // Navigate to UserHomePage Fragment
            navigateToUserHomePage();
        });

        // Set click listener for View Report button
        viewReportButton.setOnClickListener(v -> {
            // Save user data before navigating back
            saveUserData();

            // Navigate back to Page 2 (IncidentReporting2)
            navigateToPage2();
        });
    }

    // Save user data into the singleton
    private void saveUserData() {
        UserDataSingleton data = UserDataSingleton.getInstance();
        data.page4Impact1 = impact1.isChecked();
        data.page4Impact2 = impact2.isChecked();
        data.page4Impact3 = impact3.isChecked();
        data.page4Impact4 = impact4.isChecked();
        data.page4Action1 = action1.isChecked();
        data.page4Action2 = action2.isChecked();
        data.page4Action3 = action3.isChecked();
    }

    // Load saved checkbox states from the singleton
    private void loadSavedData() {
        UserDataSingleton data = UserDataSingleton.getInstance();
        if (data.page4Impact1 != null) impact1.setChecked(data.page4Impact1);
        if (data.page4Impact2 != null) impact2.setChecked(data.page4Impact2);
        if (data.page4Impact3 != null) impact3.setChecked(data.page4Impact3);
        if (data.page4Impact4 != null) impact4.setChecked(data.page4Impact4);
        if (data.page4Action1 != null) action1.setChecked(data.page4Action1);
        if (data.page4Action2 != null) action2.setChecked(data.page4Action2);
        if (data.page4Action3 != null) action3.setChecked(data.page4Action3);
    }

    // Navigate to UserHomePage Fragment
    private void navigateToUserHomePage() {
        if(userType.equals(USER)) {
            Intent intent = new Intent(IncidentReporting4.this, UserMainActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(IncidentReporting4.this, AdminMainActivity.class);
            startActivity(intent);
        }
    }

    private void navigateToPage2() {
        Intent intent = new Intent(IncidentReporting4.this, IncidentReporting2.class);
        startActivity(intent);
    }
}
