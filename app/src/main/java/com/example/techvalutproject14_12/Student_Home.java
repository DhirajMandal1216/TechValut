package com.example.techvalutproject14_12;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.techvalutproject14_12.Stud_Fragments.*;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;



import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Stack;

public class Student_Home extends AppCompatActivity {

    private FrameLayout frameLayout;
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Stack<Fragment> fragmentStack;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        bottomNavigationView = findViewById(R.id.bottomNavView);
        frameLayout = findViewById(R.id.frameLayout);

        fragmentManager = getSupportFragmentManager();
        fragmentStack = new Stack<>();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Fragment fragment = null;
                if (itemId == R.id.nav_home_item) {
                    fragment = new HomeFragment();
                } else if (itemId == R.id.nav_document_item) {
                    fragment = new DocumentsFragment();
                } else if (itemId == R.id.nav_notes_item) {
                    fragment = new NotesFragment();
                } else {
                    fragment = new ProfileFragment();
                }
                loadFragment(fragment, false);
                return true;
            }
        });
        // Load the home fragment by default
        loadFragment(new HomeFragment(), true);

        // Handle back press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!fragmentStack.isEmpty()) {
                    Fragment fragment = fragmentStack.pop();
                    fragmentManager.beginTransaction().remove(fragment).commit();
                } else {
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialize) {
        fragmentTransaction = fragmentManager.beginTransaction();
        if (isAppInitialize) {
            fragmentTransaction.add(R.id.frameLayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        fragmentTransaction.setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out);
        fragmentTransaction.commit();
        fragmentStack.push(fragment);
    }
}