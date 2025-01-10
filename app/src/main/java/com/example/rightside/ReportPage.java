package com.example.rightside;

import static com.example.rightside.Manager.*;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button allButton;
    Button racialButton;
    Button healthButton;
    Button genderButton;
    Button educationButton;
    Button incomeButton;

    int green;
    int white;
    int black;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout container;
    public ReportPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_report_page.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportPage newInstance(String param1, String param2) {
        ReportPage fragment = new ReportPage();
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
        return inflater.inflate(R.layout.fragment_report, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container = view.findViewById(R.id.ReportCardContainer);
        allButton = view.findViewById(R.id.AllButton);
        racialButton = view.findViewById(R.id.RacialButton);
        healthButton = view.findViewById(R.id.HealthButton);
        genderButton = view.findViewById(R.id.GenderButton);
        educationButton = view.findViewById(R.id.EducationButton);
        incomeButton = view.findViewById(R.id.IncomeButton);
        green = ContextCompat.getColor(getContext(),R.color.green);
        white = ContextCompat.getColor(getContext(),R.color.white);
        black = Color.BLACK;


        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonColor();
                setButtonColor(allButton);
                container.removeAllViews();
                for(int i=0 ; i<reports.size() ; i++){
                    if(isMyReport(reports.get(i)))
                        addCard(container, reports.get(i));
                }
            }
        });
        racialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonColor();
                setButtonColor(racialButton);
                container.removeAllViews();
                for(int i=0 ; i<reports.size() ; i++){
                    if(reports.get(i).discriminationType.equals("Racial") && isMyReport(reports.get(i)))
                        addCard(container, reports.get(i));
                }
            }
        });
        healthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonColor();
                setButtonColor(healthButton);
                container.removeAllViews();
                for(int i=0 ; i<reports.size() ; i++){
                    if(reports.get(i).discriminationType.equals("Health") && isMyReport(reports.get(i)))
                        addCard(container, reports.get(i));
                }
            }
        });
        genderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonColor();
                setButtonColor(genderButton);
                container.removeAllViews();
                for(int i=0 ; i<reports.size() ; i++){
                    if(reports.get(i).discriminationType.equals("Gender") && isMyReport(reports.get(i)))
                        addCard(container, reports.get(i));
                }
            }
        });
        educationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonColor();
                setButtonColor(educationButton);
                container.removeAllViews();
                for(int i=0 ; i<reports.size() ; i++){
                    if(reports.get(i).discriminationType.equals("Education") && isMyReport(reports.get(i)))
                        addCard(container, reports.get(i));
                }
            }
        });
        incomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonColor();
                setButtonColor(incomeButton);
                container.removeAllViews();
                for(int i=0 ; i<reports.size() ; i++){
                    if(reports.get(i).discriminationType.equals("Income") && isMyReport(reports.get(i)))
                        addCard(container, reports.get(i));
                }
            }
        });
    }

    public void onResume(){
        super.onResume();
        container.removeAllViews();
        for(int i=0 ; i<reports.size() ; i++){
            if(isMyReport(reports.get(i)))
                addCard(container, reports.get(i));
        }
    }

    private void addCard(LinearLayout container, Report report){
        View card = LayoutInflater.from(getActivity()).inflate(R.layout.card_report_page,container,false);

        TextView cardDiscriminationType = card.findViewById(R.id.DiscriminationType);
        TextView cardLocation = card.findViewById(R.id.Location);
        TextView cardAnonymity = card.findViewById(R.id.Anonymity);
        TextView viewButton = card.findViewById(R.id.ViewButton);

        cardDiscriminationType.setText(report.discriminationType + " Discrimination");
        cardLocation.setText(report.location);
        cardAnonymity.setText(report.isAnonymous ? "Anonymous" : report.name);


        initializeCardButton(card.findViewById(R.id.ViewButton),report.id);
        container.addView(card);
    }

    private void initializeCardButton(TextView view, int reportId) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewReportPage.reportId = reportId;
                goToPage(viewReportPage,getParentFragmentManager());
            }
        });
    }
    public void resetButtonColor(){
        allButton.setTextColor(black);
        allButton.setBackgroundColor(white);
        racialButton.setTextColor(black);
        racialButton.setBackgroundColor(white);
        healthButton.setTextColor(black);
        healthButton.setBackgroundColor(white);
        genderButton.setTextColor(black);
        genderButton.setBackgroundColor(white);
        educationButton.setTextColor(black);
        educationButton.setBackgroundColor(white);
        incomeButton.setTextColor(black);
        incomeButton.setBackgroundColor(white);
    }
    public void setButtonColor(Button button){
        button.setTextColor(white);
        button.setBackgroundColor(green);
    }
    public boolean isMyReport(Report report){;
        if(userType.equals(USER))
            return report.userId==currentUser.userId;
        else if(userType.equals(ADMIN))
            return report.adminId==currentAdmin.adminId;
        else
            return false;
    }
}