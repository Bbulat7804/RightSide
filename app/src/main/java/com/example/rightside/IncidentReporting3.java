package com.example.rightside;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class IncidentReporting3 extends AppCompatActivity {

    private EditText phoneNumberField, emailField, witnessField, extraInfoField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_incident_reporting_3);

        // Initialize fields and buttons
        phoneNumberField = findViewById(R.id.searchBar6);
        emailField = findViewById(R.id.searchBar8);
        witnessField = findViewById(R.id.searchBar5);
        extraInfoField = findViewById(R.id.searchBar7);
        Button continueButton = findViewById(R.id.button3);

        // Load saved data
        loadSavedData();

        // Set click listener for CONTINUE button
        continueButton.setOnClickListener(v -> {
            // Save data before navigating
            saveUserData();
            navigateToPage4();
        });
    }

    // Load saved data from singleton
    private void loadSavedData() {
        UserDataSingleton data = UserDataSingleton.getInstance();
        if (data.phoneNumber != null) {
            phoneNumberField.setText(data.phoneNumber);
        }
        if (data.email != null) {
            emailField.setText(data.email);
        }
        if (data.witness != null) {
            witnessField.setText(data.witness);
        }
        if (data.extraInfo != null) {
            extraInfoField.setText(data.extraInfo);
        }
    }

    // Save user data into the singleton
    private void saveUserData() {
        UserDataSingleton data = UserDataSingleton.getInstance();
        data.phoneNumber = phoneNumberField.getText().toString();
        data.email = emailField.getText().toString();
        data.witness = witnessField.getText().toString();
        data.extraInfo = extraInfoField.getText().toString();
    }

    // Navigate to the next page
    private void navigateToPage4() {
        Intent intent = new Intent(IncidentReporting3.this, IncidentReporting4.class);
        startActivity(intent);
    }
}
