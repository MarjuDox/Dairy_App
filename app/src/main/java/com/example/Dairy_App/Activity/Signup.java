package com.example.Dairy_App.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Dairy_App.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {
    EditText password,email,confirmpassword;
    Button signup;
    TextView gotolg;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        init();

        //gotologin button
        gotolg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //click signup
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String cfpass = confirmpassword.getText().toString().trim();

                if(mail.isEmpty() || pass.isEmpty() || cfpass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"All Fields Are Required",Toast.LENGTH_SHORT).show();
                }else if (pass.length() < 7)
                {
                    Toast.makeText(getApplicationContext(),"Password Should Longer than 7 Digits",Toast.LENGTH_SHORT).show();
                }else if(!pass.equals(cfpass))
                {
                    Toast.makeText(getApplicationContext(),"Confirm password is wrong",Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                                sendemailverification();
                            }else{
                                Toast.makeText(getApplicationContext(),"Fail To Register",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    void init(){
        password = findViewById(R.id.signuppassword);
        email = findViewById(R.id.signupemail);
        signup = findViewById(R.id.signup);
        gotolg = findViewById(R.id.gotologin);
        confirmpassword = findViewById(R.id.confirmpassword);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    void sendemailverification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Verification email is sent,Verify and Log In Again",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(Signup.this,MainActivity.class));
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"Fail To Send Verification Mail",Toast.LENGTH_SHORT).show();
        }
    }
}