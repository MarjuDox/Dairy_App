package com.example.Dairy_App;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.Dairy_App.Activity.Control_fragment;
import com.example.Dairy_App.Activity.MainActivity;
import com.example.Dairy_App.Activity.PassCode;
import com.example.Dairy_App.Fragment.Notes;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class SignInFacebook extends MainActivity {

    CallbackManager callbackManager;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        firebaseAuth =FirebaseAuth.getInstance();


        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(SignInFacebook.this,"Login attempt canceled.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            firebaseUser = firebaseAuth.getCurrentUser();
                            Toast.makeText(SignInFacebook.this, "Log In Success", Toast.LENGTH_SHORT).show();
                            updateUI(firebaseUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInFacebook.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser user){
        Intent intent = new Intent(SignInFacebook.this, Control_fragment.class);
        finish();
        startActivity(intent);
    }

}
