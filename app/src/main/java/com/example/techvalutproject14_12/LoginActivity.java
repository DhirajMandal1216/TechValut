package com.example.techvalutproject14_12;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button login_submit_btn;
    TextView loginRedirectText;
    boolean passwordResult;
    //Database reference
    // DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://edudocpp-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginUsername = findViewById(R.id.Login_Username);
        loginPassword = findViewById(R.id.Login_Password);
        login_submit_btn = findViewById(R.id.Login_Submit_btn);
        loginRedirectText = findViewById(R.id.Login_Redirect_text);

        //EyeButton
        loginPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= loginPassword.getRight() - loginPassword.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = loginPassword.getSelectionEnd();
                        if (passwordResult) {
                            //set drawable image
                            loginPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0);
                            //for hide password
                            loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordResult = false;
                        } else {
                            //set drawable image
                            loginPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0);
                            //for show password
                            loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordResult = true;
                        }
                        loginPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        //Submit Button
        login_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateUsername() | !validatePassword()) {
                } else {
                    checkUser();
                    checkTeacher();
                }
            }
        });

        //Sign in text to Registration page
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Registration_Main.class);
                startActivity(intent);
                finish();
            }
        });
    }


    //All method are started here
    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }


    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();
        //Student Table Database reference
        DatabaseReference databaseReferenceS = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techvalut-project-default-rtdb.firebaseio.com/");
        databaseReferenceS.child("Student").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // boolean userExists = false;
                if (snapshot.hasChild(userUsername)) {
                    //loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("s_password").getValue(String.class);
                    if (passwordFromDB.equals(userPassword)) {
                        //loginUsername.setError(null);
/*                       String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("enrollment1").getValue(String.class);

                        Intent intent = new Intent(LoginActivity.this, S_HomeActivity.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("enrollment1", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent);*/

                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        start_S_HomeActivity();
                        finish();

                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                        //Toast.makeText(LoginActivity.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                    //Toast.makeText(LoginActivity.this,"Wrong Enrolment Number",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
    //Teacher Table Database reference

    public void checkTeacher(){
        String userUsername_T = loginUsername.getText().toString().trim();
        String userPassword_T = loginPassword.getText().toString().trim();
        DatabaseReference databaseReferenceT = FirebaseDatabase.getInstance().getReferenceFromUrl("https://techvalut-project-default-rtdb.firebaseio.com/");
        databaseReferenceT.child("Teacher").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userUsername_T)) {
                    //loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername_T).child("t_password").getValue(String.class);
                    if (passwordFromDB.equals(userPassword_T)) {
                        //loginUsername.setError(null);
/*                       String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("enrollment1").getValue(String.class);

                        Intent intent = new Intent(LoginActivity.this, S_HomeActivity.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("enrollment1", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent);*/

                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        start_T_HomeActivity();
                        finish();

                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                        //Toast.makeText(LoginActivity.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void start_S_HomeActivity() {
        Intent intent = new Intent(LoginActivity.this, Student_Home.class);
        startActivity(intent);
    }
    private void start_T_HomeActivity() {
        Intent intent = new Intent(LoginActivity.this, Teacher_Home.class);
        startActivity(intent);
    }


    //method ended

}