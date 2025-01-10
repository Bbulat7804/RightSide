package com.example.rightside;


import static android.app.Activity.RESULT_OK;
import static com.example.rightside.Manager.*;

import android.content.Intent;
import android.net.Uri;
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

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminProfilePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminProfilePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView profilePhoto;
    LinearLayout eventImageContainer;
    TextView usernameTV;
    TextView nameTV;
    TextView emailTV;
    TextView reportNoTV;
    TextView stressLevelTV;
    TextView eventNoTV;
    TextView requestManagedNoTV;
    DatabaseConnection db = new DatabaseConnection();

    public AdminProfilePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_admin_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminProfilePage newInstance(String param1, String param2) {
        AdminProfilePage fragment = new AdminProfilePage();
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
        return inflater.inflate(R.layout.fragment_admin_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventImageContainer = view.findViewById(R.id.JoinedEventImageContainer);
        usernameTV = view.findViewById(R.id.UsernameTV);
        nameTV = view.findViewById(R.id.NameTV);
        emailTV = view.findViewById(R.id.EmailTV);
        reportNoTV = view.findViewById(R.id.ReportNoTV);
        stressLevelTV = view.findViewById(R.id.StressLevelTV);
        eventNoTV = view.findViewById(R.id.EventNoTV);
        requestManagedNoTV = view.findViewById(R.id.RequestManagedNumber);
        profilePhoto = view.findViewById(R.id.ProfilePhoto);

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
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
                db.uploadImageToFirebase(imageUri,getProfilePhotoPath(),profilePhoto,getContext());  // Call the upload function
                HashMap<String,Object> map = new HashMap<>();
                map.put("profile_photo_url", getProfilePhotoPath());
                db.updateDocument("Users", Integer.toString(currentUser.userId), map);
            }
        }
    }

    public String getProfilePhotoPath(){
        return "ProfilePhoto/" + currentUser.userId + ".png";
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

    @Override
    public void onResume() {
        super.onResume();
        eventImageContainer.removeAllViews();
        db.loadImageFromStorage(getActivity(), getProfilePhotoPath(),profilePhoto);
        usernameTV.setText((currentAdmin.username + "'s Profile").toUpperCase());
        nameTV.setText(currentAdmin.name);
        emailTV.setText(currentAdmin.email);
        reportNoTV.setText(currentAdmin.reportNo + "");
        stressLevelTV.setText(currentAdmin.stressLevel);
        eventNoTV.setText(currentAdmin.eventsNo + "");
        requestManagedNoTV.setText(currentAdmin.requestManaged + "");

        for(int i=0; i<10 ; i++){
            addImage(eventImageContainer,R.drawable.sample_event_image);
        }
    }
}