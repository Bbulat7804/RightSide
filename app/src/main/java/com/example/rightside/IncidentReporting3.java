package com.example.rightside;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
            if (validateFields()) {
                // Save data before navigating
                saveUserData();
                navigateToPage4();
            }
        });
    }

    // Validate all required fields
    private boolean validateFields() {
        boolean isValid = true;

        if (phoneNumberField.getText().toString().trim().isEmpty()) {
            phoneNumberField.setError("Phone Number cannot be empty");
            isValid = false;
        }

        if (emailField.getText().toString().trim().isEmpty()) {
            emailField.setError("Email Address cannot be empty");
            isValid = false;
        }

        if (witnessField.getText().toString().trim().isEmpty()) {
            witnessField.setError("Witness field cannot be empty");
            isValid = false;
        }

        if (extraInfoField.getText().toString().trim().isEmpty()) {
            extraInfoField.setError("Extra Information cannot be empty");
            isValid = false;
        }

        if (!isValid) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
        }

        return isValid;
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
