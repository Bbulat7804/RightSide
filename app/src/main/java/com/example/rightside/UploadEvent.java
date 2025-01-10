package com.example.rightside;

import static android.app.Activity.RESULT_OK;
import static com.example.rightside.Manager.PICK_IMAGE_EVENT_REQUEST;
import static com.example.rightside.Manager.PICK_IMAGE_REQUEST;
import static com.example.rightside.Manager.db;
import static com.example.rightside.Manager.events;
import static com.example.rightside.Manager.eventsPage;
import static com.example.rightside.Manager.goToPage;
import static com.example.rightside.Manager.latestArticleIndex;
import static com.example.rightside.Manager.latestEventIndex;
import static com.example.rightside.Manager.stack;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadEvent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ImageView uploadEventImageButton;
    EditText uploadEventTitleTextInput;
    EditText uploadEventDescTextInput;
    EditText uploadEventURLTextInput;
    EditText uploadEventAuthorNameInput;
    TextView uploadEventDateButton;
    ImageButton uploadEventButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UploadEvent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadEvent.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadEvent newInstance(String param1, String param2) {
        UploadEvent fragment = new UploadEvent();
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
        return inflater.inflate(R.layout.fragment_upload_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uploadEventImageButton = view.findViewById(R.id.uploadEventImage);
        uploadEventTitleTextInput = view.findViewById(R.id.uploadEventTitleTextInput);
        uploadEventDescTextInput = view.findViewById(R.id.uploadEventDescTextInput);
        uploadEventURLTextInput = view.findViewById(R.id.uploadEventURLTextInput);
        uploadEventAuthorNameInput = view.findViewById(R.id.uploadEventAuthorNameInput);
        uploadEventDateButton = view.findViewById(R.id.uploadEventDateButton);
        uploadEventButton = view.findViewById(R.id.UploadEventButton);

        uploadEventImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        DatePickerDialog datePickerDialog = initializeDatePicker(uploadEventDateButton);
        uploadEventDateButton.setText(UploadArticlePage.getTodaysDate());
        uploadEventDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadArticlePage.openDatePicker(datePickerDialog);
            }
        });

        uploadEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmptyFields())
                    return;

                Event event = new Event(latestEventIndex,
                        uploadEventURLTextInput.getText().toString().trim(),
                        uploadEventTitleTextInput.getText().toString().trim(),
                        uploadEventDescTextInput.getText().toString().trim(),
                        getIconPath(),
                        uploadEventAuthorNameInput.getText().toString().trim(),
                        uploadEventDateButton.getText().toString().trim());

                events.add(event);
                uploadArticleToFirestore(event);
                for (int i = 0; i < 2; i++) {
                    stack.removeFirst();
                }
                goToPage(eventsPage, getParentFragmentManager());
                Toast.makeText(getContext(), "Event Uploaded: "+event.title, Toast.LENGTH_SHORT).show();
                uploadEventTitleTextInput.setText("");
                uploadEventDescTextInput.setText("");
                uploadEventURLTextInput.setText("");
                uploadEventAuthorNameInput.setText("");
            }
        });
    }

    private boolean checkEmptyFields() {
        boolean flag = false;
        if(uploadEventURLTextInput.getText().toString().trim().isEmpty()){
            uploadEventURLTextInput.setError("URL cannot be empty");
            flag = true;
        }
        if(uploadEventTitleTextInput.getText().toString().trim().isEmpty()){
            uploadEventTitleTextInput.setError("Title cannot be empty");
            flag = true;
        }
        if(uploadEventDescTextInput.getText().toString().trim().isEmpty()){
            uploadEventDescTextInput.setError("Description cannot be empty");
            flag = true;
        }
        if(uploadEventAuthorNameInput.getText().toString().trim().isEmpty()){
            uploadEventAuthorNameInput.setError("Author Name cannot be empty");
            flag = true;
        }
        return flag;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_EVENT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_EVENT_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                latestEventIndex++;
                db.uploadImageToFirebase(imageUri,getIconPath(),uploadEventImageButton,getContext());  // Call the upload function
            }
        }
    }

    private String getIconPath() {
        return "EventsIcon/" + Integer.toString(latestEventIndex) + ".jpg";

    }

    public DatePickerDialog initializeDatePicker(TextView UploadEventDateButton){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = UploadArticlePage.makeDateString(day ,month,year);
                UploadEventDateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        return new DatePickerDialog(getActivity(),style,dateSetListener,year,month,day);
    }


    public void uploadArticleToFirestore(Event event){
        HashMap<String,String> data = new HashMap();
        data.put("event_url", event.url);
        data.put("title", event.title);
        data.put("description", event.description);
        data.put("date", event.date);
        data.put("image_url",event.imageURL);
        data.put("organizer",event.organizer);

        db.addDocument("Events", data, Integer.toString(event.id));
    }
}