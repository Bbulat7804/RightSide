package com.example.rightside;

import static com.example.rightside.Manager.currentUser;
import static com.example.rightside.Manager.dailyQuizScore;
import static com.example.rightside.Manager.db;
import static com.example.rightside.Manager.goToPage;
import static com.example.rightside.Manager.quizQuestions;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizIntroPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class
QuizIntroPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView quizScore;

    public QuizIntroPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static QuizIntroPage newInstance(String param1, String param2) {
        QuizIntroPage fragment = new QuizIntroPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_intro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView title = view.findViewById(R.id.QuizTitle);
        TextView description = view.findViewById(R.id.QuizDesc);
        Button startButton = view.findViewById(R.id.QuizIntroStartButton);
        quizScore = view.findViewById(R.id.QuizScore);

        quizScore.setVisibility(View.GONE);
        if (currentUser.dailyQuizScore != -1){
            quizScore.setVisibility(View.VISIBLE);
            quizScore.setText(currentUser.dailyQuizScore+"/5");
            startButton.setText("Try again");
        }

        db.getDocument("Quiz Sets","1").addOnSuccessListener(document -> {
            if (document.exists()) {
                title.setText(document.getString("title"));
                description.setText(document.getString("quiz_description"));
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(quizQuestions, getParentFragmentManager());
            }
        });

    }

}