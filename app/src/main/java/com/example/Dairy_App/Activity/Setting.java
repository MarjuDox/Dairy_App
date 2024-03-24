package com.example.Dairy_App.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.Dairy_App.BuildConfig;
import com.example.Dairy_App.MemoBroadCast;
import com.example.Dairy_App.R;
import com.example.Dairy_App.SharedPreferences.DataLocalManager;

public class Setting extends AppCompatActivity {
    private LinearLayout lnNotifications,lnShare;
    int ctHour, ctMinute;
    private Switch aSwitch;
    private LinearLayout lnPassCode;
    private TextView tvNotifications;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dairy_AppDark);
        } else {
            setTheme(R.style.Theme_Dairy_App);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        init();
        if (DataLocalManager.getCheckSwitch() == 2){
            aSwitch.setChecked(true);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        DataLocalManager.setCheckSwitch(2);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        DataLocalManager.setCheckSwitch(1);
                    }
                    startActivity(getIntent());
                    finish();
                    overridePendingTransition(0, 0);
                }
            });
            lnNotifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            // Hour and minute
                            ctHour = hourOfDay;
                            ctMinute = minute;
                            String time = ctHour + ":" + ctMinute + " Everyday";
                            tvNotifications.setText(time);
                            NotificationChannel();
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR_OF_DAY,ctHour);
                            calendar.set(Calendar.MINUTE,ctMinute);
                            calendar.set(Calendar.SECOND, 0);
                            if (Calendar.getInstance().after(calendar)){
                                calendar.set(Calendar.DAY_OF_MONTH,1);
                            }

                            Intent intent = new Intent(Setting.this, MemoBroadCast.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                        }

                    }, 24,0,true );
                    timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    timePickerDialog.updateTime(ctHour,ctMinute);
                    timePickerDialog.show();
                }
            });

            lnPassCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DataLocalManager.getPasscode() == ""){
                        startActivity(new Intent(Setting.this, Create_PassCode.class));
                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                        alertDialog.setTitle("Warning");
                        alertDialog.setMessage("Are you sure to remove the lock screen");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataLocalManager.setPasscode("");
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        alertDialog.show();
                    }
                }
            });

            lnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT,"Dairy App");
                        String shareMessage = "https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID +"\n\n";
                        intent.putExtra(Intent.EXTRA_TEXT,shareMessage);
                        startActivity(Intent.createChooser(intent,"Share by"));
                    } catch (Exception e){
                        Toast.makeText(Setting.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void NotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Dairy App";
            String description = "App notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Notification",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void init(){
        lnNotifications = findViewById(R.id.lnNotifications);
        tvNotifications = findViewById(R.id.tvNotifications);
        lnShare = findViewById(R.id.lnShare);
        aSwitch = findViewById(R.id.switch_dark);
        lnPassCode = findViewById(R.id.lnPassCode);
        Toolbar toolbar = findViewById(R.id.toolbarsettings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}