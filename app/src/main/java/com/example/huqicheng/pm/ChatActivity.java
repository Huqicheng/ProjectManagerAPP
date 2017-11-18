package com.example.huqicheng.pm;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huqicheng.bll.GroupBiz;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.service.MyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import com.example.huqicheng.bll.GroupBiz;
public class ChatActivity extends AppCompatActivity implements ServiceConnection,
        CalendarFragment.OnFragmentInteractionListener ,ChatFragment.OnFragmentInteractionListener,
        ProgressFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener
{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    public String assignresult = "";
    private UserBiz userBiz;
    public String TAG = "ChatActivity";
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private List<Group> groupList;
    private TextView mTextMessage;
    private Intent intent;
    private FragmentTransaction ft;
    private GroupBiz groupBiz;
    //declare static fragments for CalendarActivity
    private User user;
    private static ChatFragment chatFragment = null;


    private Intent progressIntent=null;
    private Intent settingIntent=null;
    private Intent calendarIntent=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.container);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer

        userBiz = new UserBiz(this);
        user = userBiz.readUser();
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        this.progressIntent=new Intent(this,ProgressActivity.class);
        this.calendarIntent=new Intent(this,CalendarActivity.class);
        this.settingIntent=new Intent(this,SettingActivity.class);
        this.chatFragment = ChatFragment.newInstance();

        this.ft = getFragmentManager().beginTransaction();

        //add fragments to transaction
        FragmentTransaction calendarFrament = ft.add(R.id.content, this.chatFragment, "calendarFrament");


        ft.commit();



        intent = new Intent(ChatActivity.this, MyService.class);
       // startService(intent);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Log.d("debug:", "onServiceconnected: ");
        System.out.println("Service connected");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.navigation,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        ft=getFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.navigation_calendar:
                //show calendarFragment and hide the others
                startActivity(calendarIntent);


                return true;
            case R.id.navigation_chat:


                ft.replace(R.id.content, chatFragment);
                ft.commit();


                return true;
            case R.id.navigation_progress:
                startActivity(progressIntent);

                return true;
            case R.id.navigation_setting:
                startActivity(settingIntent);


                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position,view);
        }
    }
    private void selectItem(int position,View view) {
        Log.d("ChatActivity","position"+position);
        if(position==0)
        {
            Intent intent=new Intent(this,GroupCreation.class);
            startActivity(intent);
        }
        else if(position==1)
        {
            Intent intent=new Intent(this,Group_drop_list.class);
            startActivity(intent);
            PopupMenu popup = new PopupMenu(ChatActivity.this, view);
            groupBiz = new GroupBiz();
            groupList = new ArrayList<Group>();
            groupList = groupBiz.loadGroups(user.getUserId());
            String[] limits=new String[]{"G8"};
            for (String s : limits) {
                popup.getMenu().add(s);
            }
            popup.show();
            new Thread(){
                @Override
                public void run() {
                    assignresult = groupBiz.dropGroup(2,2);
                }
            }.start();
            Log.e(TAG, "assignresult: " + assignresult);
            if (assignresult == "success"){
                finish();
                Intent i = new Intent(ChatActivity.this, ChatActivity.class);
                startActivity(i);
            }else if(assignresult == null) {
                Log.e(TAG, "assignresult: " + assignresult);
            }
        }
        else if(position==2){
            Intent intent=new Intent(this,Group_drop_list.class);
            startActivity(intent);
        }
        else{

        }



        // update the main content by replacing fragments
//        Fragment fragment = new PlanetFragment();
//        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();
//
//        // update selected item and title, then close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
       // getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d("debug:", "onServiceDisconnected: ");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this, CalendarActivity.class);
        Bundle bundle = new Bundle();

        backIntent.putExtras(bundle);
        this.setResult(1, backIntent);
        this.finish();
        startActivity(backIntent);
        //super.onBackPressed();
    }
}
