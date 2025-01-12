package com.example.rightside;

import static android.content.ContentValues.TAG;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.rightside.Manager.USERLIBRARY;
import static com.example.rightside.Manager.db;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseConnection {
    public final FirebaseFirestore db;
    public final FirebaseStorage storage;
    public final StorageReference storageReference;
    public DatabaseConnection() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    // =================== Firestore Methods ===================

    //method utk refer nama collection
    public CollectionReference getCollection(String collectionName) {
        return db.collection(collectionName);
    }

    //method utk add document ke collection
    public void addDocument(String collectionName, Map<String, Object> data, String id) {
        DocumentReference doc = getCollection(collectionName).document(id);
        doc.set(data);
    }

    //method utk get document dari collection
    public Task<DocumentSnapshot> getDocument(String collectionName, String documentId) {
        return getCollection(collectionName).document(documentId).get();
    }

    //method utk update document dari collection
    public Task<Void> updateDocument(String collectionName, String documentId, Map<String, Object> data) {
        return getCollection(collectionName).document(documentId).update(data);
    }

    //method utk delete document dari collection (utk admin biasanya)
    public Task<Void> deleteDocument(String collectionName, String documentId) {
        return getCollection(collectionName).document(documentId).delete();
    }


    // =================== Firebase Storage Methods ===================

    //method utk refer ke storage
    public StorageReference getStorageReference(String path) {
        return storage.getReference(path);
    }

    //method utk upload file ke storage
    public UploadTask uploadFile(String path, Uri fileUri) {
        return getStorageReference(path).putFile(fileUri);
    }

    //method utk download url dari storage
    public Task<Uri> getDownloadUrl(String path) {
        return getStorageReference(path).getDownloadUrl();
    }

    //method utk delete file dari storage
    public Task<Void> deleteFile(String path) {
        return getStorageReference(path).delete();
    }


    //pakai ni utk load image to imageview
    public void loadImageFromStorage(Context context, String storagePath, ImageView imageView) {
        this.getDownloadUrl(storagePath)
                .addOnSuccessListener(uri -> {
                    // Use Glide to load the image
                    Glide.with(context)
                            .load(uri)
                            .into(imageView);
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to load image", e));
    }

    public void uploadImageToFirebase(Uri imageUri, String path, ImageView imageView, Context context) {
        StorageReference fileReference = storageReference.child(path);

        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        System.out.println("Upload successful: " + downloadUrl);
                        loadImageFromStorage(context, path, imageView);
                        // Save URL to Firestore if needed
                    });
                })
                .addOnFailureListener(e -> {
                    System.out.println("Upload failed: " + e.getMessage());
                });
    }

    public void deleteImageFromFirebase(String path){
        StorageReference sf = storageReference.child(path);
        sf.delete();
    }

    public void uploadFileToDatabase(Uri fileUri, String path, LinearLayout container, Context context, ArrayList<String> paths){
        StorageReference fileReference = storageReference.child(path);
        // Upload the file to Firebase Storage
        fileReference.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL after successful upload
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        paths.add(path);
                        String name = path.split("/")[2];
                        addAttachmentCard(container,path.split("/")[0] + "/" + path.split("/")[1] + "/", name, context);
                        for(int i=0 ; i<paths.size() ; i++){
                            System.out.println(paths.get(i));
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    System.out.println("Upload failed: " + e.getMessage());
                });
    }

    public void addAttachmentCard(LinearLayout container, String path, String name, Context context){
        View card = LayoutInflater.from(context).inflate(R.layout.card_display_upload,container, false);
        TextView textView = card.findViewById(R.id.attachment_name);
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
