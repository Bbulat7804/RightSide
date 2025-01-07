package com.example.rightside;

import static android.content.Intent.getIntent;

import static com.example.rightside.Manager.goToPage;
import static com.example.rightside.Manager.stressTestPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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
    public class StressAssessmentFragment extends Fragment {

    private static final String ARG_TOTAL_SCORE = "totalScore";
    private static final String ARG_STRESS_LEVEL = "stressLevel";

    private int totalScore;
    private String stressLevel;

    public StressAssessmentFragment() {
    // Required empty public constructor
    }

    public static StressAssessmentFragment newInstance(int totalScore, String stressLevel) {
    StressAssessmentFragment fragment = new StressAssessmentFragment();
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

     ImageButton getStressScoreButton = view.findViewById(R.id.StressButton);
     TextView scoreTextView = view.findViewById(R.id.score_TV);
     TextView levelTextView = view.findViewById(R.id.levelTV);
     TextView normalNumTextView = view.findViewById(R.id.normalNumTV);
     TextView stressLevelTextView = view.findViewById(R.id.stressLevelTV);

     // Display the passed data
     scoreTextView.setText(String.valueOf(totalScore));
     levelTextView.setText(stressLevel);
     normalNumTextView.setText("5 - 25");


         getStressScoreButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 goToPage(stressTestPage,getParentFragmentManager());
             }
         });

     }
     }