package com.example.rightside;

import static android.app.Activity.RESULT_OK;
import static com.example.rightside.Manager.*;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadArticlePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadArticlePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    DatePickerDialog datePickerDialog;
    TextView dateButton;
    ImageButton uploadButton;
    EditText captionInput;
    EditText urlInput;
    EditText authorInput;
    ImageView imageUploadButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UploadArticlePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_upload_article.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadArticlePage newInstance(String param1, String param2) {
        UploadArticlePage fragment = new UploadArticlePage();
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
        return inflater.inflate(R.layout.fragment_upload_article, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DatePickerDialog datePickerDialog;
        dateButton = view.findViewById(R.id.DateButton);
        uploadButton = view.findViewById(R.id.UploadArticleButton);
        captionInput = view.findViewById(R.id.CaptionTextInput);
        urlInput = view.findViewById(R.id.URLTextInput);
        authorInput = view.findViewById(R.id.AuthorNameInput);
        imageUploadButton = view.findViewById(R.id.ImageUploadButton);

        imageUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article article = new Article(latestArticleIndex+1, urlInput.getText().toString().trim(), captionInput.getText().toString().trim(),getIconPath(), authorInput.getText().toString().trim(), dateButton.getText().toString().trim());
                latestArticleIndex++;
                articles.add(article);
                uploadArticleToFirestore(article);
                for(int i=0 ; i<2 ; i++){
                    stack.removeFirst();
                }
                goToPage(articleAdminPage,getParentFragmentManager());
            }
        });

        datePickerDialog = initializeDatePicker(dateButton);
        dateButton.setText(getTodaysDate());
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker(datePickerDialog);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                db.uploadImageToFirebase(imageUri,getIconPath(),imageUploadButton,getContext());  // Call the upload function
            }
        }
    }

    public String getIconPath(){
        return "ArticlesIcon/" + Integer.toString(latestArticleIndex+1) + ".jpg";
    }
    public DatePickerDialog initializeDatePicker(TextView dateButton){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day ,month,year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        return new DatePickerDialog(getActivity(),style,dateSetListener,year,month,day);
    }

    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
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
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day,month,year);
    }
    public void openDatePicker(DatePickerDialog datePickerDialog){
        datePickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        captionInput.setText("");
        urlInput.setText("");
        authorInput.setText("");
    }

    public void uploadArticleToFirestore(Article article){
        HashMap<String,String> data = new HashMap();
        data.put("article_url", article.url);
        data.put("author", article.author);
        data.put("caption", article.caption);
        data.put("date", article.date);
        data.put("image_url",article.imageURL);

        db.addDocument("Articles",data,Integer.toString(article.id));
    }
}