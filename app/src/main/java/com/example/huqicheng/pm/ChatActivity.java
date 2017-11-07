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
import android.util.Log;
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

import com.example.huqicheng.service.MyService;

import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements ServiceConnection,
        CalendarFragment.OnFragmentInteractionListener ,ChatFragment.OnFragmentInteractionListener,
        ProgressFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener
{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;

    private TextView mTextMessage;
    private Intent intent;
    private FragmentTransaction ft;

    //declare static fragments for MainActivity
    private static CalendarFragment calendarFragment = null;
    private static ChatFragment chatFragment = null;
    private static ProgressFragment progressFragment = null;
    private static SettingFragment settingFragment = null;


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

        if (savedInstanceState == null) {
            selectItem(0);
        }
        this.calendarFragment = CalendarFragment.newInstance();
        this.settingFragment = SettingFragment.newInstance();
        this.chatFragment = ChatFragment.newInstance();
        this.progressFragment = ProgressFragment.newInstance();
        this.ft = getFragmentManager().beginTransaction();

        //add fragments to transaction
        FragmentTransaction calendarFrament = ft.add(R.id.content, this.chatFragment, "calendarFrament");


        ft.commit();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        intent = new Intent(ChatActivity.this, MyService.class);
        startService(intent);
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
        return super.onOptionsItemSelected(item);
    }
    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    private void selectItem(int position) {
//        if(position==1)
//        {
//            Intent intent=new Intent(this,AndroidMeActivity.class);
//            startActivity(intent);
//        }


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

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.planets_array)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                    "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(planet);
            return rootView;
        }
    }
    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.d("debug:", "onServiceDisconnected: ");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
