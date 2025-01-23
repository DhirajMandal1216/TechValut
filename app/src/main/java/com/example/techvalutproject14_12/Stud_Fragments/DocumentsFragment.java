package com.example.techvalutproject14_12.Stud_Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.techvalutproject14_12.R;
import com.google.android.material.tabs.TabLayout;

public class DocumentsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_documents, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        FrameLayout fragmentContainer = view.findViewById(R.id.fragmentContainer);

        // Add tabs to the TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Document"));
        tabLayout.addTab(tabLayout.newTab().setText("Upload"));

        // Initially load the UploadFragment when the fragment is first created
        loadFragment(new document_s_fragment());

        // Set a listener for tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Switch to the corresponding fragment when a tab is selected
                if (tab.getPosition() == 0) {
                    loadFragment(new document_s_fragment()); // "Upload" tab
                } else {
                    loadFragment(new upload_s_fragment()); // "Now" tab
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselected if needed (optional)
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselection if needed (optional)
            }
        });
        return view;
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null); // Optional, if you want to keep fragments in the back stack
        transaction.commit();
    }
}