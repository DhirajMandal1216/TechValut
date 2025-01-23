package com.example.techvalutproject14_12;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration_Main extends AppCompatActivity {
    private EditText fullnameEditText;
    private EditText enrolmentIdEditText;
    private Button nextButton;
    private TextView signInText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fullnameEditText = findViewById(R.id.Fullname);
        enrolmentIdEditText = findViewById(R.id.Enrolment_no);
        nextButton = findViewById(R.id.Next);
        signInText = findViewById(R.id.SignInText);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FullName = fullnameEditText.getText().toString();
                String EnrolmentId = enrolmentIdEditText.getText().toString();

                if (!FullName.isEmpty() &&!EnrolmentId.isEmpty()) {

                    //student pattern
                    String patternS = "^GHRUAS\\d{8}$";
                    Pattern compiled_S_Pattern = Pattern.compile(patternS);
                    String inputT = EnrolmentId;
                    Matcher S_matcher = compiled_S_Pattern.matcher(inputT);

                    //Teacher Pattern

                    String patternT = "^GHRUAT\\d{8}$";
                    Pattern compiled_T_Pattern = Pattern.compile(patternT);
                    String inputS = EnrolmentId;
                    Matcher T_matcher = compiled_T_Pattern.matcher(inputS);

                    boolean S_matches = S_matcher.matches();
                    boolean T_matches = T_matcher.matches();

                    if (S_matches) {  //set pattern for student login
                        Intent intent = new Intent(Registration_Main.this, Registration_Student.class);
                        intent.putExtra("SFull_Name", FullName);
                        intent.putExtra("SEnrolment_id", EnrolmentId);
                        startActivity(intent);
                    } else if (T_matches) {  //set pattern for teacher login
                        Intent intent = new Intent(Registration_Main.this, Registration_Teacher.class);
                        intent.putExtra("Full_Name", FullName);
                        intent.putExtra("Enrolment_id", EnrolmentId);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid enrolment ID", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration_Main.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}