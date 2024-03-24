package com.example.Dairy_App;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.Dairy_App.Activity.MainActivity;

public class MemoBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent repeatIntent = new Intent(context, MainActivity.class);
        repeatIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,repeatIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"Notification").setContentIntent(pendingIntent).setSmallIcon(R.drawable.ic_circle_notifications).setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher),128,128,false)).setContentTitle("Notification").setContentText("Time to write some new!").setPriority(Notification.PRIORITY_DEFAULT).setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200,builder.build());
    }
}
