package com.example.rightside;

import static com.example.rightside.Manager.userType;
import static com.example.rightside.Manager.USER;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class IncidentReporting4 extends AppCompatActivity {

    private CheckBox impact1, impact2, impact3, impact4, action1, action2, action3, condition1, condition2;
    private TextView questionImpact, questionAction;

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
        action3 = findViewById(R.id.Action3);
        condition1 = findViewById(R.id.Condition1); // Acknowledgment checkbox
        condition2 = findViewById(R.id.Condition2); // Understanding checkbox

        // Initialize TextViews for questions
        questionImpact = findViewById(R.id.textView37); // "What impact has this discrimination caused?"
        questionAction = findViewById(R.id.textView36); // "Would you like to suggest..."

        // Initialize buttons
        ImageButton viewReportButton = findViewById(R.id.imageButton6);
        Button saveButton = findViewById(R.id.button4); // Save button
        Button submitButton = findViewById(R.id.button3); // Submit button

        // Load saved checkbox states
        loadSavedData();

        // Set click listener for Save button
        saveButton.setOnClickListener(v -> {
            saveUserData();
            Toast.makeText(this, "Info Saved", Toast.LENGTH_SHORT).show();
        });

        // Set click listener for Submit button
        submitButton.setOnClickListener(v -> {
            if (validateCheckBoxGroups()) {
                if (!condition1.isChecked() || !condition2.isChecked()) {
                    Toast.makeText(this, "Please acknowledge and agree to the terms before submitting.", Toast.LENGTH_LONG).show();
                    return;
                }

                saveUserData();
                UserDataSingleton data = UserDataSingleton.getInstance();
                data.uploadReport();
                Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();
                navigateToUserHomePage();
            }
        });

        // Set click listener for View Report button
        viewReportButton.setOnClickListener(v -> {
            saveUserData();
            navigateToPage2();
        });
    }

    private boolean validateCheckBoxGroups() {
        boolean isImpactSelected = impact1.isChecked() || impact2.isChecked() || impact3.isChecked() || impact4.isChecked();
        boolean isActionSelected = action1.isChecked() || action2.isChecked() || action3.isChecked();

        // Reset question text color to white
        questionImpact.setTextColor(Color.WHITE);
        questionAction.setTextColor(Color.WHITE);

        if (!isImpactSelected) {
            questionImpact.setTextColor(Color.RED); // Highlight question in red
            questionImpact.setText("Please select at least one impact.");
        }

        if (!isActionSelected) {
            questionAction.setTextColor(Color.RED); // Highlight question in red
            questionAction.setText("Please select at least one action.");
        }

        return isImpactSelected && isActionSelected;
    }

    private void saveUserData() {
        UserDataSingleton data = UserDataSingleton.getInstance();
        data.page4Impact1 = impact1.isChecked();
        data.page4Impact2 = impact2.isChecked();
        data.page4Impact3 = impact3.isChecked();
        data.page4Impact4 = impact4.isChecked();
        data.page4Action1 = action1.isChecked();
        data.page4Action2 = action2.isChecked();
        data.page4Action3 = action3.isChecked();
        data.page4Condition1 = condition1.isChecked();
        data.page4Condition2 = condition2.isChecked();
    }

    private void loadSavedData() {
        UserDataSingleton data = UserDataSingleton.getInstance();
        if (data.page4Impact1 != null) impact1.setChecked(data.page4Impact1);
        if (data.page4Impact2 != null) impact2.setChecked(data.page4Impact2);
        if (data.page4Impact3 != null) impact3.setChecked(data.page4Impact3);
        if (data.page4Impact4 != null) impact4.setChecked(data.page4Impact4);
        if (data.page4Action1 != null) action1.setChecked(data.page4Action1);
        if (data.page4Action2 != null) action2.setChecked(data.page4Action2);
        if (data.page4Action3 != null) action3.setChecked(data.page4Action3);
        if (data.page4Condition1 != null) condition1.setChecked(data.page4Condition1);
        if (data.page4Condition2 != null) condition2.setChecked(data.page4Condition2);
    }

    private void navigateToUserHomePage() {
        Intent intent;
        if (userType.equals(USER)) {
            intent = new Intent(IncidentReporting4.this, UserMainActivity.class);
        } else {
            intent = new Intent(IncidentReporting4.this, AdminMainActivity.class);
        }
        startActivity(intent);
    }

    private void navigateToPage2() {
        Intent intent = new Intent(IncidentReporting4.this, IncidentReporting2.class);
        startActivity(intent);
    }
}
