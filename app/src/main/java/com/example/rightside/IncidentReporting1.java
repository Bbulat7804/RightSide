package com.example.rightside;


import static com.example.rightside.Manager.USER;
import static com.example.rightside.Manager.currentUser;
import static com.example.rightside.Manager.userType;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class IncidentReporting1 extends AppCompatActivity {
    private final UserDataSingleton data = UserDataSingleton.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_incident_reporting_1);

        // Initialize buttons
        Button racialButton = findViewById(R.id.button6);
        Button healthButton = findViewById(R.id.button9);
        Button genderButton = findViewById(R.id.button10);
        Button educationButton = findViewById(R.id.button11);
        Button incomeButton = findViewById(R.id.button8);

        racialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPage2();
                data.discriminationType = "Racial";
            }
        });
        healthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPage2();
                data.discriminationType = "Health";
            }
        });
        genderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPage2();
                data.discriminationType = "Gender";
            }
        });
        educationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPage2();
                data.discriminationType = "Education";
            }
        });
        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPage2();
                data.discriminationType = "Income";
            }
        });
    }

    // Helper method to navigate to the second page
    private void navigateToPage2() {
        Intent intent = new Intent(IncidentReporting1.this, IncidentReporting2.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(false)
            super.onBackPressed();
        if(userType.equals(USER)) {
            Intent intent = new Intent(IncidentReporting1.this, UserMainActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(IncidentReporting1.this, AdminMainActivity.class);
            startActivity(intent);
        }
    }
}

