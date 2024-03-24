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
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpassword extends AppCompatActivity {
    EditText forgotpassword;
    Button recoverbutton;
    TextView gobacklg;
    
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);


        firebaseAuth =FirebaseAuth.getInstance();

        init();

        //go back to Login layout
        gobacklg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forgotpassword.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //click button recover
        recoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = forgotpassword.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter your email first",Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Mail sent,You can recover your password using mail",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(Forgotpassword.this,MainActivity.class));
                            }else{
                                Toast.makeText(getApplicationContext(),"Email is wrong or Account Not Exist",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    void init(){
        forgotpassword = findViewById(R.id.forgotpassword);
        recoverbutton = findViewById(R.id.passwordrecoverbutton);
        gobacklg = findViewById(R.id.gobacktologin);
    }
}