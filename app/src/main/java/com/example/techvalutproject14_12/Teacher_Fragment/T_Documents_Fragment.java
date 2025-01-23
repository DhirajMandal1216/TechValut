package com.example.techvalutproject14_12.Teacher_Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.techvalutproject14_12.R;
import com.example.techvalutproject14_12.Stud_Fragments.document_s_fragment;
import com.example.techvalutproject14_12.Stud_Fragments.upload_s_fragment;
import com.google.android.material.tabs.TabLayout;

public class T_Documents_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_t__documents_, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tab_T_Layout);
        FrameLayout fragmentContainer = view.findViewById(R.id.fragment_T_Container);

        // Add tabs to the TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Document"));
        tabLayout.addTab(tabLayout.newTab().setText("Upload"));

        // Initially load the UploadFragment when the fragment is first created
        loadFragment(new document_t_fragment());

        // Set a listener for tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Switch to the corresponding fragment when a tab is selected
                if (tab.getPosition() == 0) {
                    loadFragment(new document_t_fragment()); // "Upload" tab
                } else {
                    loadFragment(new upload_t_fragment()); // "Now" tab
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
        transaction.replace(R.id.fragment_T_Container, fragment);
        transaction.addToBackStack(null); // Optional, if you want to keep fragments in the back stack
        transaction.commit();
    }
}