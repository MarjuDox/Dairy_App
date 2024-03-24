package com.example.Dairy_App.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Dairy_App.BuildConfig;
import com.example.Dairy_App.R;
import com.example.Dairy_App.SharedPreferences.DataLocalManager;

public class About extends AppCompatActivity {
    private LinearLayout lnRateApp;
    private TextView tvVersion;
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
        setContentView(R.layout.activity_about);
        init();
        lnRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=co.kitetech.diary")));
            }
        });
        tvVersion.setText(BuildConfig.VERSION_NAME);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void init(){
        lnRateApp = findViewById(R.id.lnRateApp);
        tvVersion = findViewById(R.id.tvVersion);
        Toolbar toolbar = findViewById(R.id.toolbarAbout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}