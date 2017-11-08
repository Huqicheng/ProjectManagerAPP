package com.example.huqicheng.pm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {

    public final String TAG="TAG";
    private dateSelected date;
    int uniqueID=0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String title ;
        title=bundle.getString("Event name message");
        String discription;
        discription= bundle.getString("Event Discription message");

        long setVibrate[]={0,150,300,0};
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setContentTitle(title);
        notification.setTicker("You saved an event");
        notification.setContentText(discription);
        notification.setVibrate(setVibrate);

        Intent tent = new Intent(context, CalendarActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,tent,0);
        notification.setContentIntent(pendingIntent);

        NotificationManager nm = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(uniqueID, notification.build());

    }
}