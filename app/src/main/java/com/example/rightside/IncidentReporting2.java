package com.example.rightside;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class IncidentReporting2 extends AppCompatActivity {

    private static final int REQUEST_CODE_FILES = 1;
    private static final int REQUEST_CODE_IMAGES = 2;
    private static final int REQUEST_CODE_AUDIO = 3;

    private EditText locationField, describeMoreField;
    TextView dateField;
    private RadioButton checkBoxOnlyMe, checkBoxMeAndOthers, checkBoxPreferNotToDisclose, checkBoxInjured, checkBoxNotInjured;
    CheckBox checkBoxAnonymitySubmission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_incident_reporting_2);

        // Initialize fields and buttons
        locationField = findViewById(R.id.searchBar4);
        dateField = findViewById(R.id.searchBar2);
        describeMoreField = findViewById(R.id.searchBar3); // "Describe More" field
        checkBoxOnlyMe = findViewById(R.id.Action1);
        checkBoxMeAndOthers = findViewById(R.id.Impact2);
        checkBoxPreferNotToDisclose = findViewById(R.id.Impact4);
        checkBoxInjured = findViewById(R.id.Action2);
        checkBoxNotInjured = findViewById(R.id.Condition2);
        checkBoxAnonymitySubmission = findViewById(R.id.Impact3); // "Anonymity Submission" checkbox

        Button continueButton = findViewById(R.id.button2); // CONTINUE button

        // Load saved data
        loadSavedData();

        DatePickerDialog datePickerDialog = initializeDatePicker(dateField);
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(datePickerDialog);
            }
        });
        // Set click listener for CONTINUE button to navigate to the third page
        continueButton.setOnClickListener(v -> {
            // Save data before navigating
            saveUserData();
            navigateToPage3();
        });
    }

    // Load saved data from singleton
    private void loadSavedData() {
        UserDataSingleton data = UserDataSingleton.getInstance();
        if (data.location != null) {
            locationField.setText(data.location);
        }
        if (data.date != null) {
            dateField.setText(data.date);
        }
        if (data.description != null) {
            describeMoreField.setText(data.description);
        }
        if (data.page2CheckBoxOnlyMe != null) {
            checkBoxOnlyMe.setChecked(data.page2CheckBoxOnlyMe);
        }
        if (data.page2CheckBoxMeAndOthers != null) {
            checkBoxMeAndOthers.setChecked(data.page2CheckBoxMeAndOthers);
        }
        if (data.page2CheckBoxPreferNotToDisclose != null) {
            checkBoxPreferNotToDisclose.setChecked(data.page2CheckBoxPreferNotToDisclose);
        }
        if (data.page2CheckBoxInjured != null) {
            checkBoxInjured.setChecked(data.page2CheckBoxInjured);
        }
        if (data.page2CheckBoxNotInjured != null) {
            checkBoxNotInjured.setChecked(data.page2CheckBoxNotInjured);
        }
        if (data.page2CheckBoxAnonymitySubmission != null) {
            checkBoxAnonymitySubmission.setChecked(data.page2CheckBoxAnonymitySubmission);
        }
    }

    // Save user data into the singleton
    private void saveUserData() {
        UserDataSingleton data = UserDataSingleton.getInstance();
        data.location = locationField.getText().toString();
        data.date = dateField.getText().toString();
        data.description = describeMoreField.getText().toString();
        data.page2CheckBoxOnlyMe = checkBoxOnlyMe.isChecked();
        data.page2CheckBoxMeAndOthers = checkBoxMeAndOthers.isChecked();
        data.page2CheckBoxPreferNotToDisclose = checkBoxPreferNotToDisclose.isChecked();
        data.page2CheckBoxInjured = checkBoxInjured.isChecked();
        data.page2CheckBoxNotInjured = checkBoxNotInjured.isChecked();
        data.page2CheckBoxAnonymitySubmission = checkBoxAnonymitySubmission.isChecked(); // Save the state
    }

    // Method to open file chooser for general files

    // Method to open gallery for selecting images

    // Handle the result from the intents
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedFile = data.getData(); // Get the selected file URI

            if (selectedFile != null) {
                switch (requestCode) {
                    case REQUEST_CODE_FILES:
                        Toast.makeText(this, "File selected: " + selectedFile.getPath(), Toast.LENGTH_SHORT).show();
                        break;
                    case REQUEST_CODE_IMAGES:
                        Toast.makeText(this, "Image selected: " + selectedFile.getPath(), Toast.LENGTH_SHORT).show();
                        break;
                    case REQUEST_CODE_AUDIO:
                        Toast.makeText(this, "Audio selected: " + selectedFile.getPath(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToPage3() {
        Intent intent = new Intent(IncidentReporting2.this, IncidentReporting3.class);
        startActivity(intent);
    }

    public DatePickerDialog initializeDatePicker(TextView dateButton){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day ,month,year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        return new DatePickerDialog(IncidentReporting2.this,style,dateSetListener,year,month,day);
    }

    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        return Integer.toString(month);
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day,month,year);
    }
    public void openDatePicker(DatePickerDialog datePickerDialog){
        datePickerDialog.show();
    }
}
