package com.example.rightside;

import static com.example.rightside.Manager.*;

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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayRequestPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayRequestPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final ArrayList<Request> requests = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DisplayRequestPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_display_request.
     */
    // TODO: Rename and change types and number of parameters
    public static DisplayRequestPage newInstance(String param1, String param2) {
        DisplayRequestPage fragment = new DisplayRequestPage();
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
        return inflater.inflate(R.layout.fragment_display_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout cardContainer = view.findViewById(R.id.ViewRequestCardContainer);
        for (int i = 0; i < 20; i++) {
            requests.add(new Request("Tajuk", "Information and Guidance", "Completed",1000));
        }

        for(int i=0 ; i<requests.size() ; i++){
            addCard(cardContainer,requests.get(i));
        }
    }

    private void addCard(LinearLayout container, Request request){
        View card = LayoutInflater.from(getActivity()).inflate(R.layout.card_view_request,container,false);

        TextView cardTitle = card.findViewById(R.id.ButtonViewRequest);
        TextView cardDesiredOutcome = card.findViewById(R.id.ViewRequestDesiredOutcome);
        TextView cardUpdateStates = card.findViewById(R.id.ViewRequestStatusUpdate);
        TextView id = card.findViewById(R.id.userRequestId);

        cardTitle.setText(request.title);
        cardDesiredOutcome.setText(request.desiredOutcome);
        cardUpdateStates.setText(request.status);
        id.setText(request.id + "");

        initializeCardButton(card.findViewById(R.id.ButtonViewRequest),request.id);
        container.addView(card);
    }

    private void initializeCardButton(Button button, int id){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPage(viewRequestPage,getParentFragmentManager());
            }
        });

    }
}