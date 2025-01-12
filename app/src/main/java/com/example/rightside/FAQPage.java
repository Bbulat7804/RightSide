package com.example.rightside;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FAQPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FAQPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FAQPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_FAQ.
     */
    // TODO: Rename and change types and number of parameters
    public static FAQPage newInstance(String param1, String param2) {
        FAQPage fragment = new FAQPage();
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
        return inflater.inflate(R.layout.fragment_faq, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView faqQuestion1 = view.findViewById(R.id.faqQuestion1);
        TextView faqAnswer1 = view.findViewById(R.id.faqAnswer1);
        faqQuestion1.setOnClickListener(v -> {
            displayRetract(faqAnswer1);
        });

        TextView faqQuestion2 = view.findViewById(R.id.faqQuestion2);
        TextView faqAnswer2 = view.findViewById(R.id.faqAnswer2);
        faqQuestion2.setOnClickListener(v -> {
            displayRetract(faqAnswer2);
        });

        TextView faqQuestion3 = view.findViewById(R.id.faqQuestion3);
        TextView faqAnswer3 = view.findViewById(R.id.faqAnswer3);
        faqQuestion3.setOnClickListener(v -> {
            displayRetract(faqAnswer3);
        });

        TextView faqQuestion4 = view.findViewById(R.id.faqQuestion4);
        TextView faqAnswer4 = view.findViewById(R.id.faqAnswer4);
        faqQuestion4.setOnClickListener(v -> {
            displayRetract(faqAnswer4);
        });

        TextView faqQuestion5 = view.findViewById(R.id.faqQuestion5);
        TextView faqAnswer5 = view.findViewById(R.id.faqAnswer5);
        faqQuestion5.setOnClickListener(v -> {
            displayRetract(faqAnswer5);
        });

        TextView faqQuestion6 = view.findViewById(R.id.faqQuestion6);
        TextView faqAnswer6 = view.findViewById(R.id.faqAnswer6);
        faqQuestion6.setOnClickListener(v -> {
            displayRetract(faqAnswer6);
        });

        TextView faqQuestion7 = view.findViewById(R.id.faqQuestion7);
        TextView faqAnswer7 = view.findViewById(R.id.faqAnswer7);
        faqQuestion7.setOnClickListener(v -> {
            displayRetract(faqAnswer7);
        });

        TextView faqQuestion8 = view.findViewById(R.id.faqQuestion8);
        TextView faqAnswer8 = view.findViewById(R.id.faqAnswer8);
        faqQuestion8.setOnClickListener(v -> {
            displayRetract(faqAnswer8);
        });

        TextView faqQuestion9 = view.findViewById(R.id.faqQuestion9);
        TextView faqAnswer9 = view.findViewById(R.id.faqAnswer9);
        faqQuestion9.setOnClickListener(v -> {
            displayRetract(faqAnswer9);
        });

        TextView faqQuestion10 = view.findViewById(R.id.faqQuestion10);
        TextView faqAnswer10 = view.findViewById(R.id.faqAnswer10);
        faqQuestion10.setOnClickListener(v -> {
            displayRetract(faqAnswer10);
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void displayRetract(TextView tv){
        if (tv.getVisibility() == View.GONE) {
            // Make the view visible and run a fade-in animation
            tv.setVisibility(View.VISIBLE);
            tv.setAlpha(0f); // Start with fully transparent
            tv.animate()
                    .alpha(1f) // Fade in to fully visible
                    .setDuration(200) // Animation duration in milliseconds
                    .setListener(null);
        } else {
            // Run a fade-out animation, then hide the view
            tv.animate()
                    .alpha(0f) // Fade out to fully transparent
                    .setDuration(150) // Animation duration in milliseconds
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            tv.setVisibility(View.GONE); // Set visibility to GONE after fade-out
                        }
                    });
        }
    }
}