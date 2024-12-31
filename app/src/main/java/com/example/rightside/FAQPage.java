package com.example.rightside;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
            faqAnswer1.setVisibility(faqAnswer1.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        TextView faqQuestion2 = view.findViewById(R.id.faqQuestion2);
        TextView faqAnswer2 = view.findViewById(R.id.faqAnswer2);
        faqQuestion2.setOnClickListener(v -> {
            faqAnswer2.setVisibility(faqAnswer2.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        TextView faqQuestion3 = view.findViewById(R.id.faqQuestion3);
        TextView faqAnswer3 = view.findViewById(R.id.faqAnswer3);
        faqQuestion3.setOnClickListener(v -> {
            faqAnswer3.setVisibility(faqAnswer3.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        TextView faqQuestion4 = view.findViewById(R.id.faqQuestion4);
        TextView faqAnswer4 = view.findViewById(R.id.faqAnswer4);
        faqQuestion4.setOnClickListener(v -> {
            faqAnswer4.setVisibility(faqAnswer4.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        TextView faqQuestion5 = view.findViewById(R.id.faqQuestion5);
        TextView faqAnswer5 = view.findViewById(R.id.faqAnswer5);
        faqQuestion5.setOnClickListener(v -> {
            faqAnswer5.setVisibility(faqAnswer5.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        TextView faqQuestion6 = view.findViewById(R.id.faqQuestion6);
        TextView faqAnswer6 = view.findViewById(R.id.faqAnswer6);
        faqQuestion6.setOnClickListener(v -> {
            faqAnswer6.setVisibility(faqAnswer6.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        TextView faqQuestion7 = view.findViewById(R.id.faqQuestion7);
        TextView faqAnswer7 = view.findViewById(R.id.faqAnswer7);
        faqQuestion7.setOnClickListener(v -> {
            faqAnswer7.setVisibility(faqAnswer7.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        TextView faqQuestion8 = view.findViewById(R.id.faqQuestion8);
        TextView faqAnswer8 = view.findViewById(R.id.faqAnswer8);
        faqQuestion8.setOnClickListener(v -> {
            faqAnswer8.setVisibility(faqAnswer8.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        TextView faqQuestion9 = view.findViewById(R.id.faqQuestion9);
        TextView faqAnswer9 = view.findViewById(R.id.faqAnswer9);
        faqQuestion9.setOnClickListener(v -> {
            faqAnswer9.setVisibility(faqAnswer9.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        TextView faqQuestion10 = view.findViewById(R.id.faqQuestion10);
        TextView faqAnswer10 = view.findViewById(R.id.faqAnswer10);
        faqQuestion10.setOnClickListener(v -> {
            faqAnswer10.setVisibility(faqAnswer10.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });
        super.onViewCreated(view, savedInstanceState);
    }
}