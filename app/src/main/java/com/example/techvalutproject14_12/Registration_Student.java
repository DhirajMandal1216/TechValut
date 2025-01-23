package com.example.techvalutproject14_12;

import android.content.Intent;
import com.example.techvalutproject14_12.Helper_Class.HelperClass;
import android.os.Bundle;
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

import com.example.techvalutproject14_12.Helper_Class.HelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration_Student extends AppCompatActivity {

    EditText signupCourse,signupEmail,signupPassword,signupConform_pass;

    TextView signupName,signupEnrollment;
    Button signupSubmit_btn;

    //DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://edudocpp-default-rtdb.firebaseio.com/");
    boolean passwordResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        signupName=findViewById(R.id.Student_Fullname);
        signupEnrollment=findViewById(R.id.Student_Enrolment_no);

        signupCourse=findViewById(R.id.Course);
        signupEmail=findViewById(R.id.Email);
        signupPassword=findViewById(R.id.Password);
        signupConform_pass=findViewById(R.id.ConformPass);
        signupSubmit_btn=findViewById(R.id.Submit_btn);



        Intent intent = getIntent();
        String FullName = intent.getStringExtra("SFull_Name");
        String EnrolmentId = intent.getStringExtra("SEnrolment_id");

        signupName.setText(FullName);
        signupEnrollment.setText(EnrolmentId);

        //EyeButton on passWord and conform Password

        signupPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=signupPassword.getRight()-signupPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=signupPassword.getSelectionEnd();
                        if(passwordResult){
                            //set drawable image
                            signupPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            //for hide password
                            signupPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordResult=false;
                        }
                        else {
                            //set drawable image
                            signupPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            //for show password
                            signupPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordResult=true;
                        }
                        signupPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        signupConform_pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=signupConform_pass.getRight()-signupConform_pass.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=signupConform_pass.getSelectionEnd();
                        if(passwordResult){
                            //set drawable image
                            signupConform_pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_off_24,0);
                            //for hide password
                            signupConform_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordResult=false;
                        }
                        else {
                            //set drawable image
                            signupConform_pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.baseline_visibility_24,0);
                            //for show password
                            signupConform_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordResult=true;
                        }
                        signupConform_pass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


        signupSubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String S_name=signupName.getText().toString();
                String S_enrolment=signupEnrollment.getText().toString();

                String S_course=signupCourse.getText().toString();
                String S_email=signupEmail.getText().toString();
                String S_password=signupPassword.getText().toString();
                String S_conformPass=signupConform_pass.getText().toString();

                HelperClass helperClass = new HelperClass(S_name,S_enrolment,S_course,S_email,S_password,S_conformPass);
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://techvalut-project-default-rtdb.firebaseio.com/");


                if (S_course.length()==0 || S_email.length()==0 || S_password.length()==0 || S_conformPass.length()==0){
                    Toast.makeText(getApplicationContext(), "Please fill all details ", Toast.LENGTH_SHORT).show();
                    return;
                } else if(!Patterns.EMAIL_ADDRESS.matcher(S_email).matches()){
                    Toast.makeText(getApplicationContext(), "Invalid Email ", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!(S_password.compareTo(S_conformPass) == 0)) {
                    Toast.makeText(getApplicationContext(), "Password and Confirm password didn't match", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!isvalid(S_password)) {
                    Toast.makeText(getApplicationContext(), "Password must contain 8 characters, having letter, digit and Special symbol ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // database code here
                    databaseReference.child("Student").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(S_enrolment)){
                                Toast.makeText(Registration_Student.this,"Student Enrolment Already Exist",Toast.LENGTH_SHORT).show();
                            } else {
                                databaseReference.child("Student").child(S_enrolment).setValue(helperClass);
                                Toast.makeText(getApplicationContext(), "Sign Up Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Registration_Student.this,LoginActivity.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                //

            }
        });



    }

    public static boolean isvalid(String passwordhers){
        int f1=0,f2=0,f3=0;
        if(passwordhers.length()<8){
            return false;
        }else {
            for(int p=0;p<passwordhers.length();p++){
                if ((Character.isLetter(passwordhers.charAt(p)))){
                    f1=1;
                }
            }
            for(int r=0;r<passwordhers.length();r++){
                if ((Character.isDigit(passwordhers.charAt(r)))){
                    f2=1;
                }
            }
            for(int s=0;s<passwordhers.length();s++){
                char c=passwordhers.charAt(s);
                if (c>=33&&c<=46||c==64){
                    f3=1;
                }
            }
            if ((f1==1&&f2==1&&f3==1)){
                return true;
            }
            return false;
        }
    }


}