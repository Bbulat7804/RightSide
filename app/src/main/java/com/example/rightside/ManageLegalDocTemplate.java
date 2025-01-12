package com.example.rightside;

import static android.app.Activity.RESULT_OK;
import static com.example.rightside.Manager.db;
import static com.example.rightside.Manager.documentPaths;
import static com.example.rightside.Manager.latestRequestIndex;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageLegalDocTemplate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageLegalDocTemplate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_FILE_REQUEST = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout documentContainer;



    public ManageLegalDocTemplate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_manage_legal_doc_template.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageLegalDocTemplate newInstance(String param1, String param2) {
        ManageLegalDocTemplate fragment = new ManageLegalDocTemplate();
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
        return inflater.inflate(R.layout.fragment_manage_legal_doc_template, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button add = view.findViewById(R.id.buttonAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        documentContainer = view.findViewById(R.id.legalDocumentContainer);
    }

    @Override
    public void onResume() {
        super.onResume();
        documentContainer.removeAllViews();
        for (String path : documentPaths) {
            addAttachmentCard(documentContainer,path.split("/")[0]+"/",path.split("/")[1], getActivity());
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                uploadFileToDatabase(fileUri,"LegalDocumentTemplate/" + getFileName(fileUri,getActivity()),documentContainer,getActivity(),documentPaths);
            }
        }
    }

    public String getFileName(Uri uri, Context context) {
        String fileName = null;

        // For content URI (e.g., from file picker)
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                fileName = cursor.getString(nameIndex);
                cursor.close();
            }
        }
        return fileName;
    }

    public void uploadFileToDatabase(Uri fileUri, String path, LinearLayout container, Context context, ArrayList<String> paths){
        StorageReference fileReference = db.storageReference.child(path);
        // Upload the file to Firebase Storage
        fileReference.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL after successful upload
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        paths.add(path);
                        String name = path.split("/")[1];
                        addAttachmentCard(container,path.split("/")[0] + "/" , name, context);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("paths", paths);
                        System.out.println(paths);
                        db.updateDocument("Document Template","1", map);
                    });
                })
                .addOnFailureListener(e -> {
                    System.out.println("Upload failed: " + e.getMessage());
                });
    }
    public void addAttachmentCard(LinearLayout container, String path, String name, Context context){
        View card = LayoutInflater.from(context).inflate(R.layout.card_legal_document,container, false);
        TextView textView = card.findViewById(R.id.document_name);
        textView.setText(name);
        File localFile = new File(context.getFilesDir(), name);
        StorageReference fileReference = FirebaseStorage.getInstance().getReference().child(path + name);
        card.setOnClickListener(view -> {
            // Start the download process
            fileReference.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Download successful, the file is saved in 'localFile'
                        Log.d("Download", "File downloaded successfully");
                        // Optionally, you can now open the downloaded file (e.g., an image or PDF) using an Intent
                        Uri fileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", localFile);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(fileUri, getMimeTypeFromFile(name));  // Change MIME type based on file type\
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        // Download failed
                        System.out.println("fail");
                    });
        });

        container.addView(card);
    }

    public String getMimeTypeFromFile(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "ppt":
                return "application/vnd.ms-powerpoint";
            case "pptx":
                return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "mp3":
                return "audio/mpeg";
            case "wav":
                return "audio/wav";
            case "mp4":
                return "video/mp4";
            case "mov":
                return "video/quicktime";
            case "zip":
                return "application/zip";
            case "rar":
                return "application/x-rar-compressed";
            case "txt":
                return "text/plain";
            default:
                return "*/*"; // Generic MIME type for unknown files
        }
    }
}