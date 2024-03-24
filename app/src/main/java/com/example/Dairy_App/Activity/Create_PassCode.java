package com.example.Dairy_App.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.Dairy_App.R;
import com.example.Dairy_App.SharedPreferences.DataLocalManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.hanks.passcodeview.PasscodeView;

public class Create_PassCode extends AppCompatActivity {
    private Toolbar tbPasscodeCreate;
    private PasscodeView passcodeViewCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DataLocalManager.getCheckSwitch() == 2){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //setTheme(R.style.Theme_Dairy_AppDark);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //setTheme(R.style.Theme_Dairy_App);
        }
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dairy_AppDark);
        } else {
            setTheme(R.style.Theme_Dairy_App);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pass_code);
        init();
        if (DataLocalManager.getPasscode() == "")
        {
            passcodeViewCreate.setListener(new PasscodeView.PasscodeViewListener() {
                @Override
                public void onFail() {
                }

                @Override
                public void onSuccess(String number) {
                    passcodeViewCreate.setLocalPasscode(number);
                    DataLocalManager.setPasscode(number);
                    DataLocalManager.setCheckLockScreen(false);
                    finish();
                }
            });
        }
    }

    public void init(){
        tbPasscodeCreate = findViewById(R.id.tbPasscodeCreate);
        passcodeViewCreate = findViewById(R.id.passcodeViewCreate);
        setSupportActionBar(tbPasscodeCreate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}