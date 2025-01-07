package com.example.rightside;

import static com.example.rightside.Manager.db;
import static com.example.rightside.Manager.goToPage;
import static com.example.rightside.Manager.requests;
import static com.example.rightside.Manager.stack;
import static com.example.rightside.Manager.viewRequestPage;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifyMentalConsultation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyMentalConsultation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView dateTV;
    TextView timeTV;
    TextView descriptionTV;
    ListView attachmentList;
    Spinner spinnerReasonConsultation;
    Spinner spinnerDesiredOutcome;
    RadioGroup preferredConsultationRB;
    RadioButton inPerson;
    RadioButton phoneCall;
    RadioButton videoCall;
    RadioButton textChat;
    RadioGroup urgencyRB;
    RadioButton urgent;
    RadioButton nonUrgent;
    Button submitButton;
    String selectedText;
    String selectedText2;
    View view;
    Button buttonDelete;

    public ModifyMentalConsultation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_modify_mental_consultation.
     */
    // TODO: Rename and change types and number of parameters
    public static ModifyMentalConsultation newInstance(String param1, String param2) {
        ModifyMentalConsultation fragment = new ModifyMentalConsultation();
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
        return inflater.inflate(R.layout.fragment_modify_mental_consultation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dateTV = view.findViewById(R.id.ETpreferredDateMental);
        timeTV = view.findViewById(R.id.ETpreferredTimeMental);
        descriptionTV = view.findViewById(R.id.editTextDescribeMental);
        this.view = view;
        preferredConsultationRB = view.findViewById(R.id.RadioGroupPreferredConsultation);
        urgencyRB = view.findViewById(R.id.RadioGroupUrgency);


        inPerson = view.findViewById(R.id.buttonInPersonMental);
        phoneCall = view.findViewById(R.id.buttonPhoneCallMental);
        videoCall = view.findViewById(R.id.buttonVideoCallMental);
        textChat = view.findViewById(R.id.buttonTextChatMental);
        urgent = view.findViewById(R.id.buttonUrgentMental);
        nonUrgent = view.findViewById(R.id.buttonNonUrgentMental);

        spinnerReasonConsultation = view.findViewById(R.id.MentalConsultationReasonSpinner);
        ArrayAdapter<CharSequence> reasonConsultationAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.MentalConsultationReason, R.layout.layout_spinner);
        spinnerReasonConsultation.setAdapter(reasonConsultationAdapter);
        spinnerDesiredOutcome = view.findViewById(R.id.MentalDesiredOutcomeSpinner);
        ArrayAdapter<CharSequence> desiredOutcomeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.MentalDesiredOutcome, R.layout.layout_spinner);
        spinnerDesiredOutcome.setAdapter(desiredOutcomeAdapter);

        TextView dateButton = view.findViewById(R.id.ETpreferredDateMental);
        DatePickerDialog datePickerDialog = initializeDatePicker(dateButton);

        submitButton = view.findViewById(R.id.buttonSubmitMentalForm);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(datePickerDialog);
            }
        });

        timeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(timeTV);
            }

        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < requests.size(); i++) {
                    if (ViewRequestPage.requestId == requests.get(i).requestId) {
                        requests.remove(i);
                        db.deleteDocument("Requests", Integer.toString(ViewRequestPage.requestId));
                    }
                }
            }
        });
    }


    private void showTimePicker(TextView timeTextView) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create and show TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        timeTextView.setText(selectedTime); // Update the TextView
                    }
                }, hour, minute, true); // true for 24-hour format
        timePickerDialog.show();
    }

    public DatePickerDialog initializeDatePicker(TextView dateButton) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        return new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "Jan";
        else if (month == 2)
            return "Feb";
        else if (month == 3)
            return "Mar";
        else if (month == 4)
            return "Apr";
        else if (month == 5)
            return "May";
        else if (month == 6)
            return "Jun";
        else if (month == 7)
            return "Jul";
        else if (month == 8)
            return "Aug";
        else if (month == 9)
            return "Sep";
        else if (month == 10)
            return "Oct";
        else if (month == 11)
            return "Nov";
        else if (month == 12)
            return "Dec";
        else
            return "Jan";
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }

    public void openDatePicker(DatePickerDialog datePickerDialog) {
        datePickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Request r = null;
        for (int i = 0; i < requests.size(); i++) {
            if (ViewRequestPage.requestId == requests.get(i).requestId) {
                r = requests.get(i);
            }
        }
        final Request request = r; //request is object
        dateTV.setText(request.date);
        timeTV.setText(request.time);
        descriptionTV.setText(request.description);
        String reason = request.reason;
        String outcome = request.desiredOutcome;
        String method = request.method;
        String urgency = request.urgency;


        SpinnerAdapter spinnerAdapter = spinnerReasonConsultation.getAdapter();
        for (int i = 0; i < spinnerAdapter.getCount(); i++) {
            if (spinnerAdapter.getItem(i).toString().equals(reason)) {
                spinnerReasonConsultation.setSelection(i); // Set the matched item as selected
                break;
            }
        }

        SpinnerAdapter spinnerAdapter1 = spinnerDesiredOutcome.getAdapter();
        for (int i=0; i<spinnerAdapter1.getCount(); i++) {
            if (spinnerAdapter1.getItem(i).toString().equals(outcome)) {
                spinnerDesiredOutcome.setSelection(i);
            }
        }
        switch (method){
            case "In-Person":
                inPerson.setChecked(true);
                break;
            case "Phone Call":
                phoneCall.setChecked(true);
                break;
            case "Video Call":
                videoCall.setChecked(true);
                break;
            case "Text Chat":
                textChat.setChecked(true);
                break;
            default:
                break;
        }

        if (urgency.equals("Urgent")) {
            urgent.setChecked(true);
        }
        else
            nonUrgent.setChecked(true);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.reason = spinnerReasonConsultation.getSelectedItem().toString();
                request.desiredOutcome = spinnerDesiredOutcome.getSelectedItem().toString();
                request.date = dateTV.getText().toString();
                request.time = timeTV.getText().toString();

                int checkedRadioButtonId = preferredConsultationRB.getCheckedRadioButtonId();
                if (checkedRadioButtonId != -1) {
                    RadioButton checkedRadioButton = view.findViewById(checkedRadioButtonId);
                    selectedText = checkedRadioButton.getText().toString();
                }

                int checkedRadioButtonId2 = urgencyRB.getCheckedRadioButtonId();
                if (checkedRadioButtonId2 != -1) {
                    RadioButton checkedRadioButton2 = view.findViewById(checkedRadioButtonId2);
                    selectedText2 = checkedRadioButton2.getText().toString();
                }
                request.method = selectedText;
                request.urgency = selectedText2;
                request.description = descriptionTV.getText().toString().trim();

                goToPage(viewRequestPage, getParentFragmentManager());
                stack.removeFirst();
                stack.removeFirst();

                updateRequest(request);
            }
        });
    }

    public void updateRequest (Request request) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("date", request.date);
        data.put("description", request.description);
        data.put("desired_outcome", request.desiredOutcome);
        data.put("method", request.method);
        data.put("reason", request.reason);
        data.put("status", request.status);
        data.put("time", request.time);
        data.put("urgency", request.urgency);
        db.updateDocument("Requests", Integer.toString(request.requestId), data);
    }
}

