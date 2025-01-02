package com.example.rightside;

import static android.app.Activity.RESULT_OK;
import static com.example.rightside.Manager.PICK_IMAGE_REQUEST;
import static com.example.rightside.Manager.currentUser;
import static com.example.rightside.Manager.db;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfilePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfilePage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView profilePhoto;
    public UserProfilePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_user_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfilePage newInstance(String param1, String param2) {
        UserProfilePage fragment = new UserProfilePage();
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
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout eventImageContainer = view.findViewById(R.id.JoinedEventImageContainer);
        TextView usernameTV = view.findViewById(R.id.UsernameTV);
        TextView nameTV = view.findViewById(R.id.NameTV);
        TextView emailTV = view.findViewById(R.id.EmailTV);
        TextView reportNoTV = view.findViewById(R.id.ReportNoTV);
        TextView stressLevelTV = view.findViewById(R.id.StressLevelTV);
        TextView eventNoTV = view.findViewById(R.id.EventNoTV);
        TextView supportGroupNoTV = view.findViewById(R.id.SupportGroupNoTV);
        profilePhoto = view.findViewById(R.id.ProfilePhoto);
        System.out.println("sini ke");
        usernameTV.setText((currentUser.username + "'s Profile").toUpperCase());
        nameTV.setText(currentUser.name);
        emailTV.setText(currentUser.email);
        reportNoTV.setText(currentUser.reportNo + "");
        stressLevelTV.setText(currentUser.stressLevel);
        eventNoTV.setText(currentUser.eventsNo + "");
        supportGroupNoTV.setText(currentUser.supportGroupNo + "");
        System.out.println("tak");
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        for(int i=0; i<10 ; i++){
            addImage(eventImageContainer,R.drawable.sample_event_image);
        }
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
        db.loadImageFromStorage(getActivity(), getProfilePhotoPath(),profilePhoto);
    }
}