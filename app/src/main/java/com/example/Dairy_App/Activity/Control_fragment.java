package com.example.Dairy_App.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.Dairy_App.Fragment.Notes;
import com.example.Dairy_App.R;
import com.example.Dairy_App.Fragment.RecycleBin;
import com.example.Dairy_App.SharedPreferences.DataLocalManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class Control_fragment extends AppCompatActivity{
    Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.N)
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
        setContentView(R.layout.activity_control_fragment);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Dairy App");
        if (DataLocalManager.getTheme() == ""){
            int themeColor = Color.parseColor("#6e079f");
            toolbar.setBackgroundColor(themeColor);
        } else {
            int themeColor = Color.parseColor(DataLocalManager.getTheme());
            toolbar.setBackgroundColor(themeColor);
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flContent, new Notes());
        fragmentTransaction.commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            displayView(item);

            DrawerLayout drawer1 = findViewById(R.id.drawer_layout);
            drawer1.closeDrawer(GravityCompat.START);
            return false;
        });
    }

    private void displayView(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        switch (id){
            case R.id.home:
                fragment = new Notes();
                break;
            case R.id.setting:
                startActivity(new Intent(Control_fragment.this, Setting.class));
                break;
            case R.id.colorPicker:
                final ColorPicker colorPicker = new ColorPicker(Control_fragment.this);
                ArrayList<String> colors = new ArrayList<>();
                colors.add("#6e079f");
                colors.add("#f0b227");
                colors.add("#964548");
                colors.add("#f46849");
                colors.add("#ffd1dc");
                colors.add("#05bd05");
                colorPicker.setColors(colors).setColumns(3).setRoundColorButton(true).setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int color) {
                        if (position != -1){
                            toolbar.setBackgroundColor(color);
                            DataLocalManager.setTheme(colors.get(position));
                        } else {
                            toolbar.setBackgroundColor(Color.parseColor("#6e079f"));
                            DataLocalManager.setTheme("#6e079f");
                        }
                    }

                    @Override
                    public void onCancel(){
                        // put code
                    }
                }).show();
                break;
            case R.id.recycle_bin:
                fragment = new RecycleBin();
                break;
            case R.id.about:
                startActivity(new Intent(Control_fragment.this, About.class));
                break;
            case R.id.logout:
                signOut();
                finish();
                startActivity(new Intent(Control_fragment.this, MainActivity.class));
                break;
            default:
                break;
        }
        if(fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flContent, fragment);
            fragmentTransaction.commit();
        }
    }

    public void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(this,gso);
        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseAuth.getInstance().signOut(); // very important if you are using firebase.
                    Intent login_intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(login_intent);
                }
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        DataLocalManager.setCheckLockScreen(false);
    }
}
