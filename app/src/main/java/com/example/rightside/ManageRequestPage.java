package com.example.rightside;

import static com.example.rightside.Manager.*;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageRequestPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageRequestPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button allRequest;
    Button mental;
    Button legal;

    int green;
    int white;
    int black;
    LinearLayout container;


    public ManageRequestPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_manage_request.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageRequestPage newInstance(String param1, String param2) {
        ManageRequestPage fragment = new ManageRequestPage();
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
        return inflater.inflate(R.layout.fragment_manage_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        container = view.findViewById(R.id.ManageRequestCardContainer);
        allRequest = view.findViewById(R.id.buttonAll);
        mental = view.findViewById(R.id.buttonMental);
        legal = view.findViewById(R.id.buttonLegal);

        green = ContextCompat.getColor(getContext(),R.color.green);
        white = ContextCompat.getColor(getContext(),R.color.white);
        black = Color.BLACK;

        allRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonColor();
                setButtonColor(allRequest);
                container.removeAllViews();
                for (int i = 0; i < requests.size(); i++) {
                    addCard(container, requests.get(i));
                }
            }
        });

        mental.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                resetButtonColor();
                setButtonColor(mental);
                container.removeAllViews();
                for (int i = 0; i < requests.size(); i++) {
                    if (requests.get(i).type.equals("Mental"))
                        addCard(container, requests.get(i));
                }
            }
        });

        legal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetButtonColor();
                setButtonColor(legal);
                container.removeAllViews();
                for(int i=0 ; i<requests.size() ; i++){
                    if(requests.get(i).type.equals("Legal"))
                        addCard(container, requests.get(i));
                }
            }
        });
    }

    private void addCard(LinearLayout container, Request request){
        View card = LayoutInflater.from(getActivity()).inflate(R.layout.card_manage_request,container,false);
        TextView cardTitle = card.findViewById(R.id.ManageRequestTitle);
        TextView cardDesiredOutcome = card.findViewById(R.id.ManageRequestDesiredOutcome);
        TextView cardUpdateStates = card.findViewById(R.id.ManageRequestStatusUpdate);
        TextView id = card.findViewById(R.id.adminRequestId);

        cardTitle.setText(request.reason);
        cardDesiredOutcome.setText(request.desiredOutcome);
        cardUpdateStates.setText(request.status);
        id.setText(request.requestId + "");

        initializeCardButton(card.findViewById(R.id.ManageRequestTitle),request.requestId);

        Spinner spinner = card.findViewById(R.id.ManageRequestSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String status = spinner.getSelectedItem().toString();
                request.status = status;
                HashMap<String, Object> data = new HashMap<>();
                data.put("status", request.status);
                db.updateDocument("Requests", Integer.toString(request.requestId), data);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.RequestUpdate,R.layout.layout_spinner);
        adapter.setDropDownViewResource(R.layout.layout_spinner);
        spinner.setAdapter(adapter);
        container.addView(card);

        SpinnerAdapter spinnerAdapter = spinner.getAdapter();
        for (int i = 0; i < spinnerAdapter.getCount(); i++) {
            if (spinnerAdapter.getItem(i).equals(request.status)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void initializeCardButton(Button button, int id){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewRequestAdminPage.requestId = id;
                goToPage(viewRequestAdminPage,getParentFragmentManager());
            }
        });
        }
        public void onResume(){
            super.onResume();
            Request r = null;
            container.removeAllViews();
            for(int i=0 ; i<requests.size() ; i++){
                addCard(container, requests.get(i));
                if (ViewRequestPage.requestId == requests.get(i).requestId) {
                    r = requests.get(i);
                }
            }
        }

        public void resetButtonColor(){
            allRequest.setTextColor(black);
            allRequest.setBackgroundColor(white);
            mental.setTextColor(black);
            mental.setBackgroundColor(white);
            legal.setTextColor(black);
            legal.setBackgroundColor(white);
        }
        public void setButtonColor(Button button){
            button.setTextColor(white);
            button.setBackgroundColor(green);
        }
    }