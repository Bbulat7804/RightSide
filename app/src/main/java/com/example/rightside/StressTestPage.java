package com.example.rightside;

import static com.example.rightside.Manager.currentUser;
import static com.example.rightside.Manager.db;
import static com.example.rightside.Manager.goToPage;
import static com.example.rightside.Manager.stack;
import static com.example.rightside.Manager.stressAssessmentPage;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StressTestPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StressTestPage extends Fragment {

    private RadioGroup[] questionGroups;
    private TextView resultText;

    public StressTestPage() {
        // Required empty public constructor
    }

    public static StressTestPage newInstance() {
        return new StressTestPage();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stress_test, container, false);

        questionGroups = new RadioGroup[10];
        questionGroups[0] = view.findViewById(R.id.radioGroup1);
        questionGroups[1] = view.findViewById(R.id.radioGroup2);
        questionGroups[2] = view.findViewById(R.id.radioGroup3);
        questionGroups[3] = view.findViewById(R.id.radioGroup4);
        questionGroups[4] = view.findViewById(R.id.radioGroup5);
        questionGroups[5] = view.findViewById(R.id.radioGroup6);
        questionGroups[6] = view.findViewById(R.id.radioGroup7);
        questionGroups[7] = view.findViewById(R.id.radioGroup8);
        questionGroups[8] = view.findViewById(R.id.radioGroup9);
        questionGroups[9] = view.findViewById(R.id.radioGroup10);


        view.findViewById(R.id.submitButton).setOnClickListener(v -> verifyAndCalculateScore());

        return view;
    }

    private void verifyAndCalculateScore() {
        boolean allAnswered = true;
        for (RadioGroup group : questionGroups) {
            if (group.getCheckedRadioButtonId() == -1) {
                allAnswered = false;
                break;
            }
        }
        if (allAnswered) {
            calculateScore();
        } else {
            Toast.makeText(getContext(), "Please answer all questions.", Toast.LENGTH_SHORT).show();
        }
    }

    private void calculateScore() {
        try {
            int[] scores = new int[10];
            for (int i = 0; i < 10; i++) {
                int checkedId = questionGroups[i].getCheckedRadioButtonId();
                RadioButton selectedButton = questionGroups[i].findViewById(checkedId);
                String value = selectedButton.getText().toString();
                switch (value) {
                    case "Never":
                        scores[i] = 0;
                        break;
                    case "Almost never":
                        scores[i] = 1;
                        break;
                    case "Sometimes":
                        scores[i] = 2;
                        break;
                    case "Fairly Often":
                        scores[i] = 3;
                        break;
                    case "Often":
                        scores[i] = 4;
                        break;
                    default:
                        Toast.makeText(getContext(), "Invalid option selected.", Toast.LENGTH_SHORT).show();
                        return;
                }
            }

            // Reverse scores for questions 4, 5, 7, and 8
            scores[3] = 4 - scores[3];
            scores[4] = 4 - scores[4];
            scores[6] = 4 - scores[6];
            scores[7] = 4 - scores[7];

            // Calculate total score
            int totalScore = 0;
            for (int score : scores) {
                totalScore += score;
            }

            // Determine stress level
            String stressLevel;
            if (totalScore <= 13) {
                stressLevel = "Low Stress";
            } else if (totalScore <= 26) {
                stressLevel = "Moderate Stress";
            } else {
                stressLevel = "High Stress";
            }

            currentUser.stressScore = totalScore;
            currentUser.stressLevel = stressLevel;
            HashMap<String, Object> data = new HashMap<>();
            data.put("stress_score", Integer.toString(totalScore));
            data.put("stress_level", stressLevel);
            db.updateDocument("Users", Integer.toString(currentUser.userId), data);
            // Navigate to StressAssessmentFragment
            stack.removeFirst();
            stack.removeFirst();
            goToPage(stressAssessmentPage,getParentFragmentManager());
        } catch (Exception e) {
            Toast.makeText(getContext(), "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
        }
    }


}


