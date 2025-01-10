package com.example.rightside;

import static com.example.rightside.Manager.*;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewRequestPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewRequestPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static int requestId;
    TextView reasonTV;
    TextView outcomeTV;
    TextView consultationMethodTV;
    TextView urgencyTV;
    TextView dateTV;
    TextView timeTV;
    TextView descriptionTV;
    ImageButton editButton;

    LinearLayout attachmentContainer;

    public ViewRequestPage() {
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
    public static ViewRequestPage newInstance(String param1, String param2) {
        ViewRequestPage fragment = new ViewRequestPage();
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
        return inflater.inflate(R.layout.fragment_view_request, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button contactAdminButton = view.findViewById(R.id.buttonChat);
        reasonTV = view.findViewById(R.id.TVViewRequestconsultationReason);
        outcomeTV = view.findViewById(R.id.TVViewRequestconsultationOutcome);
        consultationMethodTV = view.findViewById(R.id.TVViewConsultationType);
        urgencyTV = view.findViewById(R.id.TVurgency);
        dateTV = view.findViewById(R.id.ETpreferredDateLegal);
        timeTV = view.findViewById(R.id.ETpreferredTimeLegal);
        descriptionTV = view.findViewById(R.id.TVdescribeRequest);
        editButton = view.findViewById(R.id.editButton);
        attachmentContainer = view.findViewById(R.id.AttachmentContainer);

        contactAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSiblingPage(contactAdminPage,getParentFragmentManager());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Request request = null;

        for (int i=0; i<requests.size(); i++) {
            if (requests.get(i).requestId == requestId) {
                request = requests.get(i);
            }
        }
        reasonTV.setText(request.reason);
        outcomeTV.setText(request.desiredOutcome);
        consultationMethodTV.setText(request.method);
        urgencyTV.setText(request.urgency);
        dateTV.setText(request.date);
        timeTV.setText(request.time);
        descriptionTV.setText(request.description);
        String type = request.type;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("Mental"))
                    goToPage(modifyMentalConsultationPage,getParentFragmentManager());
                else
                    goToPage(modifyLegalConsultationPage,getParentFragmentManager());
            }
        });
        attachmentContainer.removeAllViews();
        for(String path : request.attachmentPaths){
            db.addAttachmentCard(attachmentContainer,path.split("/")[0] + "/" + path.split("/")[1] + "/", path.split("/")[2],getActivity());
        }

    }
}