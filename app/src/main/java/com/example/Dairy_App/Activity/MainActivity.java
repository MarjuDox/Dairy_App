package com.example.Dairy_App.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Dairy_App.R;
import com.example.Dairy_App.SharedPreferences.DataLocalManager;
import com.example.Dairy_App.SignInFacebook;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 101;
    ImageButton signInButtn,signInFB;
    GoogleSignInClient googleSignInClient;

    EditText email,password;
    RelativeLayout login,gotosignup;
    TextView forgotpassword;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DataLocalManager.getPasscode() != "" && !DataLocalManager.getCheckLockScreen() ){
            //DataLocalManager.setCheckLockScreen(true);
            finish();
            Intent intent = new Intent(MainActivity.this, PassCode.class);
            startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        init();

        //This take you to SignUp page
        gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
            }
        });

        //This take you to forgotpassword page
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Forgotpassword.class);
                startActivity(intent);
            }
        });

        //When you click Login and it will check your account exist or not
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if(mail.isEmpty() || pass.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All Field Are Required", Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                checkmailverfication();
                            }else{
                                Toast.makeText(getApplicationContext(), "Account Doesn't Exist OR Password is Wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        //LoginWithGoogle
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this,gso);

        signInButtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //LoginWithFB
        signInFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this, SignInFacebook.class));
            }
        });
    }

    void init(){
        email = findViewById(R.id.loginemail);
        password = findViewById(R.id.loginpassword);
        login = findViewById(R.id.login);
        gotosignup = findViewById(R.id.gotosignup);
        forgotpassword = findViewById(R.id.forgotpassword);
        signInButtn = findViewById(R.id.SignInWithGG);
        signInFB = findViewById(R.id.SignInWithFB);
    }

    private void checkmailverfication(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()==true){
            Toast.makeText(getApplicationContext(),"Logged In",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, Control_fragment.class));
        }else{
            Toast.makeText(getApplicationContext(),"Verify Your Email First",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            firebaseUser = firebaseAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"Sign In Success",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,Control_fragment.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(),"Fail to Sign In by Google",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

}