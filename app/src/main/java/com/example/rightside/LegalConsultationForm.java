
package com.example.rightside;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LegalConsultationForm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LegalConsultationForm extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LegalConsultationForm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_legal_consultation_form.
     */
    // TODO: Rename and change types and number of parameters
    public static LegalConsultationForm newInstance(String param1, String param2) {
        LegalConsultationForm fragment = new LegalConsultationForm();
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

        return inflater.inflate(R.layout.fragment_legal_consultation_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinnerReasonConsultation = view.findViewById(R.id.LegalConsultationReasonSpinner);
        ArrayAdapter<CharSequence> reasonConsultationAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.LegalConsultationReason, R.layout.layout_spinner);
        spinnerReasonConsultation.setAdapter(reasonConsultationAdapter);

        Spinner spinnerDesiredOutcome = view.findViewById(R.id.LegalDesiredOutcomeSpinner);
        ArrayAdapter<CharSequence> desiredOutcomeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.LegalDesiredOutcome, R.layout.layout_spinner);
        spinnerDesiredOutcome.setAdapter(desiredOutcomeAdapter);

    }
}