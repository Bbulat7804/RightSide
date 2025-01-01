package com.example.rightside;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

public class DatabaseConnection {
    private final FirebaseFirestore db;
    private final FirebaseStorage storage;
    public DatabaseConnection() {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    // =================== Firestore Methods ===================

    //method utk refer nama collection
    public CollectionReference getCollection(String collectionName) {
        return db.collection(collectionName);
    }
    
    //method utk add document ke collection
    public Task<DocumentReference> addDocument(String collectionName, Map<String, String> data) {
        return getCollection(collectionName).add(data);
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
        Log.d("SINI SINI SINI", "Error sini 1");
        this.getDownloadUrl(storagePath)
                .addOnSuccessListener(uri -> {
                    Log.d("SINI SINI SINI", "Error sini 2");
                    // Use Glide to load the image
                    Glide.with(context)
                            .load(uri)
                            .into(imageView);
                })
                .addOnFailureListener(e -> Log.e("Firebase", "Failed to load image", e));
    }
}
