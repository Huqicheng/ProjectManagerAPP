package com.example.huqicheng.pm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.huqicheng.bll.EventBiz;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.service.CalendarNotificationService;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    NotificationManager mNotificationManager;
    EventBiz eventBiz;
    Handler handler;
    public List<Event> eventList;
    public List<Long> stampList;
    public List<CalendarDay> datesList;
    public Long user_id;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, CalendarNotificationService.class);
        context.startService(i);
        /*
        final User user = new UserBiz(context).readUser();
        user_id = user.getUserId();
        eventBiz = new EventBiz();
        new Thread(){
            @Override
            public void run() {
                //Log.d("NotificationService", "executed at " + new Date().
                //        toString());
                stampList = new ArrayList<>();
                stampList = eventBiz.loadDatesHavingEvents(user_id, "started");
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = stampList;
                handler.handleMessage(msg);
            }
        }.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ArrayList<Long> dates = (ArrayList<Long>)msg.obj;
                for(int i=0;i<dates.size();i++){
                    Date date = new Date(dates.get(i));
                    CalendarDay day = CalendarDay.from(date);
                    Log.d("timestamp in CF=", ""+ date.toString());
                    datesList.add(day);
                }
                Date currentDate = new Date();
                CalendarDay currentDay = CalendarDay.from(currentDate);
                if (datesList.contains(currentDay)){

                }
            }
        };
        */
        sendNotification(context);
    }
    //get pendingintent
    public PendingIntent getDefalutIntent(int flags, Context context){
        PendingIntent pendingIntent= PendingIntent.getActivity(context, 1, new Intent(), flags);
        return pendingIntent;
    }
    //send notificaiton
    public void sendNotification(Context context){
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("You have deadline today!")//notification title
                .setContentText("Click to see what events you have") //notification content
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL,context)) //notification onclick intent
                .setAutoCancel(true)
                //  .setNumber(number) //number of notification
                .setTicker("Event Notification") //
                .setWhen(System.currentTimeMillis())//notification created time
                .setPriority(Notification.PRIORITY_DEFAULT) //priority
                //.setAutoCancel(true)//
                .setOngoing(false)//ture，
                .setDefaults(Notification.DEFAULT_VIBRATE)//add attribube to notification, such as virbate, sound....
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND // requires VIBRATE permission
                .setSmallIcon(R.drawable.ic_launcher);//notification ICON
        Intent intent = new Intent(context,ProgressActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(3, mBuilder.build());
    }


}