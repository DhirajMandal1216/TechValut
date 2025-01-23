package com.example.techvalutproject14_12.Stud_Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.techvalutproject14_12.*;
import com.example.techvalutproject14_12.Stud_Fragments.Adepter.PdfListAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;



public class document_s_fragment extends Fragment {
    private ProgressBar progressBar;
    private ListView listView;
    private PdfListAdapter adapter;
    private ArrayList<String> pdfUrls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_document_s_fragment, container, false);
        listView = view.findViewById(R.id.pdf_list_view);
        progressBar = view.findViewById(R.id.progress_bar);
        pdfUrls = new ArrayList<>();

        adapter = new PdfListAdapter(getContext(), pdfUrls);
        listView.setAdapter(adapter);

        fetchPdfUrls();

        return view;
    }

    private void fetchPdfUrls() {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("pdfs").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            pdfUrls.add(document.getString("url"));
                        }
                        adapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.GONE);
                });
    }
}