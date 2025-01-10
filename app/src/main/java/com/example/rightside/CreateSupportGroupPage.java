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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateSupportGroupPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateSupportGroupPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText groupNameInput;
    EditText groupDescriptionInput;
    Button createButton;

    ImageView groupIconButton;
    public CreateSupportGroupPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateSupportGroup.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateSupportGroupPage newInstance(String param1, String param2) {
        CreateSupportGroupPage fragment = new CreateSupportGroupPage();
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
        return inflater.inflate(R.layout.fragment_create_support_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupIconButton = view.findViewById(R.id.GroupIconButton);
        groupNameInput = view.findViewById(R.id.GroupNameInput);
        groupDescriptionInput = view.findViewById(R.id.GroupDescriptionInput);
        createButton = view.findViewById(R.id.CreateButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> list = new ArrayList();
                SupportGroup group = new SupportGroup(groupNameInput.getText().toString().trim(),groupDescriptionInput.getText().toString().trim(),(latestSupportGroupIndex+1),getIconPath(),list);
                latestSupportGroupIndex++;
                supportGroups.add(group);
                uploadGroupToFirestore(group);
            }
        });

        groupIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_GROUP_ICON_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_GROUP_ICON_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                db.uploadImageToFirebase(imageUri,getIconPath(),groupIconButton,getContext());  // Call the upload function
            }
        }
    }

    public String getIconPath(){
        return "GroupsIcon/" + Integer.toString(latestSupportGroupIndex+1) + ".jpg";
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void uploadGroupToFirestore(SupportGroup group){
        HashMap<String,Object> data = new HashMap<>();
        data.put("name", group.name);
        data.put("description", group.description);
        data.put("icon_url", group.iconUrl);
        ArrayList<String> temp = new ArrayList();
        data.put("participants_id", temp);
        db.addDocument("Support Groups", data, Integer.toString(group.id));
    }
}