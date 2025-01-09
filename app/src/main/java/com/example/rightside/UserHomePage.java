package com.example.rightside;

import static com.example.rightside.Manager.*;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHomePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserHomePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_home.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHomePage newInstance(String param1, String param2) {
        UserHomePage fragment = new UserHomePage();
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
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout eventImageContainer = view.findViewById(R.id.EventImageContainer);
        LinearLayout articleImageContainer = view.findViewById(R.id.ArticleImageContainer);

        //Declare data display view
        TextView nameTV = view.findViewById(R.id.HomeNameTV);
        nameTV.setText(currentUser.name);

        //Declare Buttons
        LinearLayout liveDataButton = view.findViewById(R.id.LiveDataButton);
        LinearLayout eventsButton = view.findViewById(R.id.UserEventsButton);
        LinearLayout manageRequestButton = view.findViewById(R.id.ManageRequestButton);
        LinearLayout articlesButton = view.findViewById(R.id.ArticlesButton);
        LinearLayout userConsultationButton = view.findViewById(R.id.UserConsultationButton);
        LinearLayout userSupportGroupButton = view.findViewById(R.id.UserSupportGroupsButton);
        LinearLayout userReportButton = view.findViewById(R.id.UserReportButton);
        LinearLayout stressManagementButton = view.findViewById(R.id.StressManagementButton);
        LinearLayout eventCalendarButton = view.findViewById(R.id.EventCalendarButton);
        LinearLayout reportHistoryButton = view.findViewById(R.id.ReportHistoryButton);
        LinearLayout quizButton = view.findViewById(R.id.QuizButton);
        LinearLayout documentTemplateButton = view.findViewById(R.id.DocumentTemplateButton);
        TextView mentalConsultationButton = view.findViewById(R.id.MentalConsultationButton);
        TextView legalConsultationButton = view.findViewById(R.id.LegalConsultationButton);

        userReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(blankPage, getParentFragmentManager());
                stack.clear();
                Intent intent = new Intent(getActivity(), IncidentReporting1.class);
                System.out.println(getActivity());
                System.out.println(IncidentReporting1.class);
                startActivity(intent);
            }
        });
        stressManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(stressAssessmentPage,getParentFragmentManager());
            }
        });
        liveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(dataInsigtsPage,getParentFragmentManager());
            }
        });

        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(eventsPage,getParentFragmentManager());
            }
        });

        manageRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(userRequestPage,getParentFragmentManager());
            }
        });

        articlesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(articlePage,getParentFragmentManager());
            }
        });

        userConsultationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                legalConsultationButton.setVisibility(legalConsultationButton.getVisibility()==View.GONE ? View.VISIBLE : View.GONE);
                mentalConsultationButton.setVisibility(mentalConsultationButton.getVisibility()==View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        legalConsultationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(legalConsultationForm,getParentFragmentManager());
            }
        });

        mentalConsultationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(mentalConsultationForm,getParentFragmentManager());
            }
        });

        documentTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(legalDocTemplate,getParentFragmentManager());
            }
        });

        userSupportGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(anonymousSupportGroupPage,getParentFragmentManager());
            }
        });
        for(int i=0; i<10 ; i++){
            addImage(eventImageContainer,R.drawable.sample_event_image);
        }
        for(int i=0; i<10 ; i++){
            addImage(articleImageContainer,R.drawable.sample_article_image);
        }
    }

    public void addImage(LinearLayout container, int imageResource){
        ImageView imageView = new ImageView(container.getContext());
        imageView.setImageResource(imageResource);

        // Set layout parameters for proper sizing
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1000, LinearLayout.LayoutParams.MATCH_PARENT);

        params.setMargins(20, 0, 0, 10); // Optional: Add spacing between images
        imageView.setLayoutParams(params);

        container.addView(imageView);
    }


}