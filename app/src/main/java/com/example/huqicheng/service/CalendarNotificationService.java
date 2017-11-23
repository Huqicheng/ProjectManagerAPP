package com.example.huqicheng.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.huqicheng.bll.EventBiz;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.pm.AlarmReceiver;
import com.example.huqicheng.pm.ProgressActivity;
import com.example.huqicheng.pm.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Mark on 2017/11/22.
 */

public class CalendarNotificationService extends Service {
    EventBiz eventBiz;
    Handler handler;
    public List<Event> eventList;
    public List<Long> stampList;
    public List<CalendarDay> datesList;
    public Long user_id;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("LongRunningService", "executed at " + new Date().
                        toString());
            }
        }).start();



        sendAlarm();

        return super.onStartCommand(intent, flags, startId);
    }


    public void sendAlarm(){
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 30 * 1000; // number of milli second for 30 seconds
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
    }
}