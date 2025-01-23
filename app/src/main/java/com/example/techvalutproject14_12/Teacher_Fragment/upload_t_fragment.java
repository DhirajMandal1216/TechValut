package com.example.techvalutproject14_12.Teacher_Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.techvalutproject14_12.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;


public class upload_t_fragment extends Fragment {
    private static final int PICK_PDF_REQUEST = 1;
    private Uri pdfUri;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_t_fragment, container, false);
        Button uploadButton = view.findViewById(R.id.upload_t_button);
        progressBar = view.findViewById(R.id.progress_t_bar);

        // Initially hide the progress bar
        progressBar.setVisibility(View.GONE);

        // Set up the button listener
        uploadButton.setOnClickListener(v -> openFilePicker());

        return view;
    }// Opens a file picker to select a PDF file
    private void openFilePicker() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            pdfUri = data.getData();
            uploadPdfToFirebase();
        }
    }

    // Uploads the selected PDF to Firebase Storage
    private void uploadPdfToFirebase() {
        if (pdfUri != null) {
            // Show progress bar
            progressBar.setVisibility(View.VISIBLE);

            // Reference to Firebase Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference pdfReference = storageReference.child("pdfs/" + System.currentTimeMillis() + ".pdf");

            // Log the path and URI
            Log.d("Firebase Storage", "Storage Path: " + pdfReference.getPath());
            Log.d("PDF Upload", "Selected PDF URI: " + pdfUri);

            // Start the upload task
            pdfReference.putFile(pdfUri)
                    .addOnSuccessListener(taskSnapshot -> pdfReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                // Save metadata to Firestore
                                savePdfInfoToFirestore(uri.toString());
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                progressBar.setVisibility(View.GONE);
                                Log.e("Upload Error", "Failed to retrieve download URL: ", e);
                                Toast.makeText(getContext(), "Failed to retrieve download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }))
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Log.e("Upload Error", "Upload Failed: ", e);
                        Toast.makeText(getContext(), "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


    // Saves PDF metadata (download URL) to Firestore
    private void savePdfInfoToFirestore(String downloadUrl) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Map<String, Object> pdfData = new HashMap<>();
        pdfData.put("url", downloadUrl);
        pdfData.put("timestamp", System.currentTimeMillis()); // Add a timestamp for sorting purposes

        firestore.collection("pdfs").add(pdfData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Data saved successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Data save failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
