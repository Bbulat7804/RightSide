package com.example.rightside;

import static com.example.rightside.Manager.currentUser;
import static com.example.rightside.Manager.goToPage;
import static com.example.rightside.Manager.stressTestPage;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StressAssessmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
/**public class StressAssessmentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StressAssessmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StressAssessmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public class StressAssessmentPage extends Fragment {

    private static final String ARG_TOTAL_SCORE = "totalScore";
    private static final String ARG_STRESS_LEVEL = "stressLevel";

    private int totalScore;
    TextView scoreTextView;
    TextView levelTextView;
    private String stressLevel;
    TextView normalNumTextView;
    TextView stressLevelTextView;

    PieChart semiCircleChart;


    public StressAssessmentPage() {
        // Required empty public constructor
    }

    public static StressAssessmentPage newInstance(int totalScore, String stressLevel) {
        StressAssessmentPage fragment = new StressAssessmentPage();
        Bundle args = new Bundle();
        args.putInt(ARG_TOTAL_SCORE, totalScore);
        args.putString(ARG_STRESS_LEVEL, stressLevel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            totalScore = getArguments().getInt(ARG_TOTAL_SCORE);
            stressLevel = getArguments().getString(ARG_STRESS_LEVEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stress_assessment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout getStressScoreButton = view.findViewById(R.id.StressButton);
        levelTextView = view.findViewById(R.id.levelTV);
        normalNumTextView = view.findViewById(R.id.normalNumTV);
        stressLevelTextView = view.findViewById(R.id.stressLevelTV);
        semiCircleChart = view.findViewById(R.id.semiCircleChart);

        // Display the passed data

        normalNumTextView.setText("5 - 25");


        getStressScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(stressTestPage, getParentFragmentManager());
            }
        });

        setupSemiCircleChart();
    }

    @Override
    public void onResume() {
        super.onResume();
        levelTextView.setText(currentUser.stressLevel);
        updateSemiCircleChart(currentUser.stressScore);
    }

    private void setupSemiCircleChart() {
        semiCircleChart.setUsePercentValues(true);
        semiCircleChart.setDrawHoleEnabled(true);
        semiCircleChart.setHoleRadius(80f);
        semiCircleChart.setTransparentCircleRadius(85f);
        semiCircleChart.setRotationEnabled(false);
        semiCircleChart.setRotationAngle(180f);
        semiCircleChart.setDrawEntryLabels(false);

        Description description = new Description();
        description.setText("");
        semiCircleChart.setDescription(description);
        semiCircleChart.setMaxAngle(180f); // Half pie chart
        semiCircleChart.setRotationAngle(180f);
        semiCircleChart.getLegend().setEnabled(false); // Disable legend
    }

    private void updateSemiCircleChart(int score) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(score));
        entries.add(new PieEntry(40 - score));

        PieDataSet dataSet = new PieDataSet(entries, "");

        // Set colors based on score range
        if (score <= 13) {
            dataSet.setColors(Color.GREEN, Color.LTGRAY);
        } else if (score <= 26) {
            dataSet.setColors(Color.YELLOW, Color.LTGRAY);
        } else {
            dataSet.setColors(Color.RED, Color.LTGRAY);
        }

        dataSet.setDrawValues(false); // Disable value labels

        PieData data = new PieData(dataSet);
        semiCircleChart.setData(data);
        semiCircleChart.setCenterText(String.valueOf(score)); // Set score in center
        semiCircleChart.setCenterTextSize(24f);
        semiCircleChart.setCenterTextColor(Color.BLACK);
        semiCircleChart.setCenterTextTypeface(android.graphics.Typeface.create("roboto", android.graphics.Typeface.BOLD));
        semiCircleChart.invalidate(); // Refresh chart
        semiCircleChart.animateY(1000, Easing.EaseInOutCubic);
    }
    }

