package com.example.rightside;

import static com.example.rightside.Manager.dateFormat;
import static com.example.rightside.Manager.reports;

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
 * Use the {@link ViewReportPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewReportPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static int reportId;
    private TextView discriminationTypeTV, reportNameTV, locationTV, dateTV, descriptionTV, peopleInvolvedTV, injuryTV, witnessTV, extraInfoTV, impactsTV, actionsTV;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ViewReportPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewReport.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewReportPage newInstance(String param1, String param2) {
        ViewReportPage fragment = new ViewReportPage();
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
        return inflater.inflate(R.layout.fragment_view_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        discriminationTypeTV = view.findViewById(R.id.discriminationTypeTV);
        reportNameTV = view.findViewById(R.id.ReportNameTV);
        locationTV = view.findViewById(R.id.LocationTV);
        dateTV = view.findViewById(R.id.DateTV);
        descriptionTV = view.findViewById(R.id.DescriptionTV);
        peopleInvolvedTV = view.findViewById(R.id.PeopleInvolvedTV);
        injuryTV = view.findViewById(R.id.InjuryTV);
        witnessTV = view.findViewById(R.id.WitnessTV);
        extraInfoTV = view.findViewById(R.id.ExtraInfoTV);
        impactsTV = view.findViewById(R.id.ImpactsTV);
        actionsTV = view.findViewById(R.id.ActionsTV);
    }

    @Override
    public void onResume() {
        super.onResume();
        String impactsString = "";
        String actionsString = "";
        Report report = null;
        for(int i=0 ; i<reports.size() ; i++){
            if(reports.get(i).id == reportId)
                report = reports.get(i);
        }
        discriminationTypeTV.setText(report.discriminationType + " Discrimination");
        reportNameTV.setText(report.name);
        locationTV.setText(report.location);
        dateTV.setText(dateFormat.format(report.date));
        descriptionTV.setText(report.description);
        peopleInvolvedTV.setText(report.personInvolved);
        injuryTV.setText(report.injury);
        witnessTV.setText(report.witness);
        extraInfoTV.setText(report.extraInfo);
        for(int i=0 ; i<report.impacts.size() ; i++){
            impactsString += (i+1) + ". " + report.impacts.get(i) + "\n";
        }
        for(int i=0 ; i<report.actions.size() ; i++){
            actionsString += (i+1) + ". " + report.actions.get(i) + "\n";
        }
        impactsTV.setText(impactsString);
        actionsTV.setText(actionsString);
    }
}