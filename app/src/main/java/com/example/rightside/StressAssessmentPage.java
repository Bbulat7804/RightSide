package com.example.rightside;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class StressAssessmentPage extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_stress_assessment); // Ensure the file name matches your XML layout

        // Find the ImageButton
        ImageButton getStressScoreButton = findViewById(R.id.StressButton);

        // Set an OnClickListener to navigate to the Stress Test page
        getStressScoreButton.setOnClickListener(v -> {
            Intent intent = new Intent(StressAssessmentPage.this, StressTestPage.class);
            startActivity(intent);
        });

        // Bind XML components
        TextView scoreTextView = findViewById(R.id.ScoreTV);
        TextView levelTextView = findViewById(R.id.levelTV);
        TextView normalNumTextView = findViewById(R.id.normalNumTV);
        TextView stressLevelTextView = findViewById(R.id.stressLevelTV);

        // Retrieve data from the Intent
        int totalScore = getIntent().getIntExtra("totalScore", 0);
        String stressLevel = getIntent().getStringExtra("stressLevel");

        // Set text dynamically
        scoreTextView.setText(String.valueOf(totalScore)); // Display total score
        levelTextView.setText(stressLevel); // Display stress level
        normalNumTextView.setText("5 - 25"); // Normal range (static, from XML)
        stressLevelTextView.setText(stressLevel); // Optional: Repeat stress level
    }
}