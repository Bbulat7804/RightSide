
package com.example.rightside;

import static com.example.rightside.Manager.currentUser;
import static com.example.rightside.Manager.db;
import static com.example.rightside.Manager.goToPage;
import static com.example.rightside.Manager.latestRequestIndex;
import static com.example.rightside.Manager.requests;
import static com.example.rightside.Manager.stack;
import static com.example.rightside.Manager.userRequestPage;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LegalConsultationForm newInstance} factory method to
 * create an instance of this fragment.
 */
public class LegalConsultationForm extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner spinnerReasonConsultation;
    Spinner spinnerDesiredOutcome;
    String selectedMethod;
    String selectedUrgency;
    RadioButton inPerson;
    RadioButton phoneCall;
    RadioButton videoCall;
    RadioButton textChat;
    RadioGroup preferredConsultationRG;
    RadioGroup urgencyRG;
    RadioButton urgent;
    RadioButton nonUrgent;
    TextView dateTV;
    TextView timeTV;
    TextView descriptionTV;
    Button submitButton;




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
    public static LegalConsultationForm newInstance(String param1, String param2) {
        System.out.println("hye");

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

        spinnerReasonConsultation = view.findViewById(R.id.LegalConsultationReasonSpinner);
        ArrayAdapter<CharSequence> reasonConsultationAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.LegalConsultationReason, R.layout.layout_spinner);
        spinnerReasonConsultation.setAdapter(reasonConsultationAdapter);

        spinnerDesiredOutcome = view.findViewById(R.id.LegalDesiredOutcomeSpinner);
        ArrayAdapter<CharSequence> desiredOutcomeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.LegalDesiredOutcome, R.layout.layout_spinner);
        spinnerDesiredOutcome.setAdapter(desiredOutcomeAdapter);

        preferredConsultationRG = view.findViewById(R.id.RadioGroupPreferredConsultation);
        urgencyRG = view.findViewById(R.id.RadioGroupUrgency);
        dateTV = view.findViewById(R.id.ETpreferredDateLegal);
        timeTV = view.findViewById(R.id.ETpreferredTimeLegal);
        DatePickerDialog datePickerDialog = initializeDatePicker(dateTV);
        descriptionTV = view.findViewById(R.id.editTextDescribeLegal);
        submitButton = view.findViewById(R.id.buttonSubmitLegalForm);


        //////////////////////////////////////////////////////////////////////////////////////////////

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dateChosen = dateTV.getText().toString();
                String timeChosen = timeTV.getText().toString();
                String reason = spinnerReasonConsultation.getSelectedItem().toString();
                String desiredOutcome = spinnerDesiredOutcome.getSelectedItem().toString();
                String description = descriptionTV.getText().toString().trim();
                int checkedRadioButtonId2 = urgencyRG.getCheckedRadioButtonId();
                if (checkedRadioButtonId2 != -1) {
                    RadioButton checkedRadioButton2 = view.findViewById(checkedRadioButtonId2);
                    selectedUrgency = checkedRadioButton2.getText().toString();
                }
                int checkedRadioButtonId = preferredConsultationRG.getCheckedRadioButtonId();
                if (checkedRadioButtonId != -1) {
                    RadioButton checkedRadioButton = view.findViewById(checkedRadioButtonId);
                    selectedMethod = checkedRadioButton.getText().toString();
                }

                Request request = new Request(reason,desiredOutcome,selectedMethod,selectedUrgency,dateChosen,timeChosen,description,"Pending", currentUser.adminId, currentUser.userId,++latestRequestIndex,"Legal");
                uploadRequest(request);
                Toast.makeText(requireContext(), "Your form has been submitted!", Toast.LENGTH_SHORT).show();


                goToPage(userRequestPage, getParentFragmentManager());
                stack.removeFirst();
            }
        });
        dateTV.setOnClickListener(new View.OnClickListener() {
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
    }

    public void uploadRequest(Request request) {
        //upload into fireStore wajib guna HashMap
        HashMap<String, Object> data = new HashMap<>();
        data.put("user_id", Integer.toString(request.userId));
        data.put("admin_id", Integer.toString(request.adminId));
        data.put("date", request.date);
        data.put("description", request.description);
        data.put("desired_outcome", request.desiredOutcome);
        data.put("method", request.method);
        data.put("reason", request.reason);
        data.put("status", request.status);
        data.put("time", request.time);
        data.put("urgency", request.urgency);
        data.put("type", request.type);

        db.addDocument("Requests",data, Integer.toString(request.requestId ));
        //upload into ArrayList
        requests.add(request);
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

    public static void openDatePicker(DatePickerDialog datePickerDialog) {
        datePickerDialog.show();
    }


}

