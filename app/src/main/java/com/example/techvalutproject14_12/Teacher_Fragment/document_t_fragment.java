package com.example.techvalutproject14_12.Teacher_Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.techvalutproject14_12.R;
import com.example.techvalutproject14_12.Teacher_Fragment.Adepter.PdfList__T_Adapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class document_t_fragment extends Fragment {
    private ProgressBar progressBar;
    private ListView listView;
    private PdfList__T_Adapter adapter;
    private ArrayList<String> pdfUrls;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_document_t_fragment, container, false);
        listView = view.findViewById(R.id.pdf_t_list_view);
        progressBar = view.findViewById(R.id.progress_t_bar);
        pdfUrls = new ArrayList<>();
        adapter = new PdfList__T_Adapter(getContext(), pdfUrls);
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