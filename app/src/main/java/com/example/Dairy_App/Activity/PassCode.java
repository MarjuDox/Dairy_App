package com.example.Dairy_App.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.Dairy_App.R;
import com.example.Dairy_App.SharedPreferences.DataLocalManager;
import com.hanks.passcodeview.PasscodeView;

public class PassCode extends AppCompatActivity {
    PasscodeView passcodeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code);
        passcodeView = findViewById(R.id.passcodeView);
        passcodeView.setPasscodeLength(5).setLocalPasscode(DataLocalManager.getPasscode()).setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {
            }

            @Override
            public void onSuccess(String number) {
                DataLocalManager.setCheckLockScreen(true);
                finish();
                startActivity(new Intent(PassCode.this, MainActivity.class));
            }
        });
    }
}