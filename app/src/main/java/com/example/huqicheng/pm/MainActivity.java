package com.example.huqicheng.pm;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.huqicheng.service.MyService;

public class MainActivity extends AppCompatActivity implements ServiceConnection,
        CalendarFragment.OnFragmentInteractionListener ,ChatFragment.OnFragmentInteractionListener,
        ProgressFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener
{


    private TextView mTextMessage;
    private Intent intent;
    private FragmentTransaction ft;

    //declare static fragments for MainActivity
    private static CalendarFragment calendarFragment = null;
    private static ChatFragment chatFragment = null;
    private static ProgressFragment progressFragment = null;
    private static SettingFragment settingFragment = null;

    Fragment fragment = null;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ft=getFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_calendar:
                    //show calendarFragment and hide the others
                    System.out.print("calendar");
                    ft.replace(R.id.content, calendarFragment);
                    ft.commit();


                    return true;
                case R.id.navigation_chat:
                    System.out.print("chat");
                    ft.replace(R.id.content, chatFragment);
                    ft.commit();


                    return true;
                case R.id.navigation_progress:
                    ft.replace(R.id.content,progressFragment);
                    ft.commit();



                    return true;
                case R.id.navigation_setting:
                    ft.replace(R.id.content,settingFragment);
                    ft.commit();


                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.calendarFragment = CalendarFragment.newInstance();
        this.settingFragment = SettingFragment.newInstance();
        this.chatFragment = ChatFragment.newInstance();
        this.progressFragment = ProgressFragment.newInstance();
        this.ft = getFragmentManager().beginTransaction();
        fragment = calendarFragment;
        //add fragments to transaction
        FragmentTransaction calendarFrament = ft.add(R.id.content, this.calendarFragment, "calendarFrament");
        ft.add(R.id.content, this.settingFragment, "settingFragment");
        ft.add(R.id.content, this.chatFragment, "chatFragment");
        ft.add(R.id.content, this.progressFragment, "progressFragment");
        ft.hide(progressFragment);
        ft.hide(chatFragment);
        ft.hide(settingFragment);
        ft.commit();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        intent = new Intent(MainActivity.this, MyService.class);
        startService(intent);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d("debug:", "onServiceconnected: ");
        System.out.println("Service connected");
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d("debug:", "onServiceDisconnected: ");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
