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
 * Use the {@link AdminHomePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHomePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminHomePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_admin_home.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminHomePage newInstance(String param1, String param2) {
        AdminHomePage fragment = new AdminHomePage();
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
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout eventImageContainer = view.findViewById(R.id.EventImageContainer);
        LinearLayout articleImageContainer = view.findViewById(R.id.ArticleImageContainer);

        //Declara data display view
        TextView nameTV = view.findViewById(R.id.HomeNameTV);
        nameTV.setText(currentAdmin.name);

        //Declare Buttons
        LinearLayout reportButton = view.findViewById(R.id.UserReportButton);
        LinearLayout manageRequestButton = view.findViewById(R.id.AdminManageRequestButton);
        LinearLayout supportGroupsButton = view.findViewById(R.id.AdminSupportGroupsButton);
        LinearLayout eventsButton = view.findViewById(R.id.AdminEventsButton);
        LinearLayout documentTemplateButton = view.findViewById(R.id.DocumentTemplateButton);
        LinearLayout liveDataButton = view.findViewById(R.id.LiveDataButton);
        LinearLayout quizButton = view.findViewById(R.id.QuizButton);
        LinearLayout reportHistoryButton = view.findViewById(R.id.ReportHistoryButton);
        LinearLayout stressManagementButton = view.findViewById(R.id.StressManagementButton);
        LinearLayout articleButton = view.findViewById(R.id.ArticlesButton);

        reportHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(reportPage,getParentFragmentManager());
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(blankPage, getParentFragmentManager());
                stack.clear();
                Intent intent = new Intent(getActivity(), IncidentReporting1.class);
                System.out.println(getActivity());
                System.out.println(IncidentReporting1.class);
                UserDataSingleton.getInstance().resetData();
                startActivity(intent);
            }
        });

        stressManagementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(stressAssessmentPage,getParentFragmentManager());
            }
        });

        supportGroupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(anonymousSupportGroupAdminPage,getParentFragmentManager());
            }
        });
        manageRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(adminRequestPage,getParentFragmentManager());
            }
        });

        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(eventsPage,getParentFragmentManager());
            }
        });

        documentTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(manageLegalDocTemplate,getParentFragmentManager());
            }
        });

        liveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(dataInsigtsPage,getParentFragmentManager());
            }
        });

        articleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(articleAdminPage,getParentFragmentManager());
            }
        });

        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { goToPage(quizIntroPage,getParentFragmentManager());}
        });


        for(int i=0; i<events.size() ; i++){
            addImage(eventImageContainer,events.get(i).imageURL);
        }
        for(int i=0; i<articles.size() ; i++){
            addImage(articleImageContainer,articles.get(i).imageURL);
        }
    }

    public void addImage(LinearLayout container, String imagePath){

        View card = LayoutInflater.from(getActivity()).inflate(R.layout.card_image_container,container,false);
        ImageView imageView = card.findViewById(R.id.imageContainer);
        db.loadImageFromStorage(getActivity(),imagePath,imageView);
        System.out.println(card);
        container.addView(card);
    }
}