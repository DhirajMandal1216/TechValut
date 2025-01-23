package com.example.techvalutproject14_12;

import android.content.Intent;
import android.os.Bundle;

import com.example.techvalutproject14_12.Helper_Class.HelperTeacherClass;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.techvalutproject14_12.Helper_Class.HelperTeacherClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration_Teacher extends AppCompatActivity {
    EditText RTdepartment, RTemail, RTpassword, RTconform_pass;
    TextView RTname, RTenrollment;
    Button RTsubmit_btn;
    //    FirebaseDatabase database;
//    DatabaseReference reference;
    boolean passwordResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration_teacher);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        RTname = findViewById(R.id.Teacher_Fullname);
        RTenrollment = findViewById(R.id.Teacher_Enrolment_no);
        RTdepartment = findViewById(R.id.NameOfDepartment);
        RTemail = findViewById(R.id.RTEmail);
        RTpassword = findViewById(R.id.RTPassword);
        RTconform_pass = findViewById(R.id.RTConformPass);
        RTsubmit_btn = findViewById(R.id.RTSubmit_btn);


        Intent intent = getIntent();
        String FullName = intent.getStringExtra("Full_Name");
        String EnrolmentId = intent.getStringExtra("Enrolment_id");

        RTname.setText(FullName);
        RTenrollment.setText(EnrolmentId);

        //EyeButton on passWord and conform Password

        RTpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= RTpassword.getRight() - RTpassword.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = RTpassword.getSelectionEnd();
                        if (passwordResult) {
                            //set drawable image
                            RTpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0);
                            //for hide password
                            RTpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordResult = false;
                        } else {
                            //set drawable image
                            RTpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0);
                            //for show password
                            RTpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordResult = true;
                        }
                        RTpassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        RTconform_pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= RTconform_pass.getRight() - RTconform_pass.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = RTpassword.getSelectionEnd();
                        if (passwordResult) {
                            //set drawable image
                            RTconform_pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0);
                            //for hide password
                            RTconform_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordResult = false;
                        } else {
                            //set drawable image
                            RTconform_pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0);
                            //for show password
                            RTconform_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordResult = true;
                        }
                        RTconform_pass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        RTsubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String T_name = RTname.getText().toString();
                String T_enrolment = RTenrollment.getText().toString();
                String T_department = RTdepartment.getText().toString();
                String T_email = RTemail.getText().toString();
                String T_password = RTpassword.getText().toString();
                String T_conformPass = RTconform_pass.getText().toString();
                HelperTeacherClass helperTeacherClass = new HelperTeacherClass(T_name, T_enrolment, T_department, T_email, T_password, T_conformPass);
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://techvalut-project-default-rtdb.firebaseio.com/");


//
                if (T_department.length()==0 || T_email.length()==0 || T_password.length()==0 || T_conformPass.length()==0){
                    Toast.makeText(getApplicationContext(), "Please fill all details ", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!Patterns.EMAIL_ADDRESS.matcher(T_email).matches()){
                    Toast.makeText(getApplicationContext(), "Invalid Email ", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!(T_password.compareTo(T_conformPass) == 0)) {
                    Toast.makeText(getApplicationContext(), "Password and Confirm password didn't match", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isvalid(T_password)) {
                    Toast.makeText(getApplicationContext(), "Password must contain 8 characters, having letter, digit and Special symbol ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // database code here
                    databaseReference.child("Teacher").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(T_enrolment)){
                                Toast.makeText(Registration_Teacher.this,"Student Enrolment Already Exist",Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("Teacher").child(T_enrolment).setValue(helperTeacherClass);
                                Toast.makeText(getApplicationContext(), "Sign Up Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Registration_Teacher.this,LoginActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


    }

    public static boolean isvalid(String passwordhers) {
        int f1 = 0, f2 = 0, f3 = 0;
        if (passwordhers.length() < 8) {
            return false;
        } else {
            for (int p = 0; p < passwordhers.length(); p++) {
                if ((Character.isLetter(passwordhers.charAt(p)))) {
                    f1 = 1;
                }
            }
            for (int r = 0; r < passwordhers.length(); r++) {
                if ((Character.isDigit(passwordhers.charAt(r)))) {
                    f2 = 1;
                }
            }
            for (int s = 0; s < passwordhers.length(); s++) {
                char c = passwordhers.charAt(s);
                if (c >= 33 && c <= 46 || c == 64) {
                    f3 = 1;
                }
            }
            if ((f1 == 1 && f2 == 1 && f3 == 1)) {
                return true;
            }
            return false;
        }
    }

}