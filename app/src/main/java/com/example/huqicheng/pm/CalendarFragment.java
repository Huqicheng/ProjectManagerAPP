package com.example.huqicheng.pm;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.huqicheng.adapter.CalendarEventListAdapter;
import com.example.huqicheng.bll.EventBiz;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.decorator.HighlightCurrentdayDecorator;
import com.example.huqicheng.decorator.HighlightDeadlineDecorator;
import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.service.CalendarNotificationService;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class CalendarFragment extends Fragment {
    //NotificationManager mNotificationManager;
    MaterialCalendarView CalendarView;
    ListView listView;
    Intent intent;
    private EventBiz eventBiz;
    private CalendarEventListAdapter adapter;
    private User user;
    private Handler handler = null;
    public List<Event> eventList;
    public List<Long> stampList;
    public List<CalendarDay> datesList = new ArrayList<>();
    public HighlightDeadlineDecorator deadlineDecorator;
    public HighlightCurrentdayDecorator currentdayDecorator;
    static final String TAG="CalenderFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
        //Bundle args = new Bundle();
        System.out.println("this is calendar fragment");
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_calendar, container, false);
        // Inflate the layout for this fragment
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CalendarView = (MaterialCalendarView) v.findViewById(R.id.calendarView);
                listView = (ListView) v.findViewById(R.id.eventlist);
                HighlightCurrentdayDecorator currentdayDecorator = new HighlightCurrentdayDecorator();
                CalendarView.addDecorator(currentdayDecorator);
                //send notification of today's event
                //sendNotification();
            }
        });

        //eventBiz
        eventBiz = new EventBiz();
        //load user
        user = new UserBiz(getActivity()).readUser();
        Log.d(TAG,user.toString());
        Log.d(TAG,user.getUsername());
        //listView listener: show DateSelected activity and edit event
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d("clicked item: ", i + "at pos " + l);
                Event event = adapter.getEventList().get(i);
                Log.d("events in CF",""+event.getDescription());
                intent = new Intent(getActivity(), DateSelected.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("event", event);//serializable
                bundle.putSerializable("flag", DateSelected.EDIT);//indicating EDIT event or INIT event
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //CalendarView listener: show events on a specific date
        CalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull com.prolificinteractive.materialcalendarview.MaterialCalendarView widget, @NonNull final com.prolificinteractive.materialcalendarview.CalendarDay date, boolean selected) {
                //load events to listView
                new Thread(){
                    @Override
                    public void run() {
                        loadEvents(user.getUserId(), date.getDate().getTime());
                    }
                }.start();
                //if there is no event on that day, create new event
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    //get dates having events
                    case 1:
                        ArrayList<Long> dates = (ArrayList<Long>)msg.obj;
                        datesList = new ArrayList<>();
                        for(int i=0;i<dates.size();i++){
                            //Log.d("dates",""+dates.get(i));
                            Date date = new Date(dates.get(i));
                            CalendarDay day = CalendarDay.from(date);
                            Log.d(TAG, "timestamp in CF=" + date.toString());
                            datesList.add(day);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                deadlineDecorator = new HighlightDeadlineDecorator(Color.parseColor("#FF4081"), datesList);
                                CalendarView.addDecorators(deadlineDecorator);
                                //send notification
                                if ( datesList.contains(CalendarDay.from(new Date() ) ) ){
                                    intent = new Intent(getActivity(), CalendarNotificationService.class);
                                    getActivity().startService(intent);
                                }
                            }
                        });
                        break;
                    //get events on specific day
                    case 2:
                        eventList = (ArrayList<Event>)msg.obj;

                        for (int i = 0; i < eventList.size();i++){
                            //Log.d("events",""+eventList.get(i).getDescription());
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new CalendarEventListAdapter(getActivity(),eventList,user);
                                listView.setAdapter(adapter);
                                //adapter.add(eventList,user);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        break;
                }
            }
        };
        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        /** get data from the database**/
        new Thread(){
            @Override
            public void run() {
                //CalendarView.addDecorator(decorator);
                loadDates(user.getUserId());
            }
        }.start();
    }
    @Override
    public void onResume(){
        super.onResume();
        CalendarView.removeDecorator(deadlineDecorator);
    }
    @Override
    public void onStop(){
        super.onStop();
        //mNotificationManager.cancel(3);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    void loadDates(long user_id){
        eventBiz = new EventBiz();
        stampList = new ArrayList<>();
        try {
            List<Long> templist = eventBiz.loadDatesHavingEvents(user_id, "started");
            for (int i = 0; i < templist.size();i++){
                //Log.e(TAG, "timestamp=" + new Date(stampList.get(i)).toString());
                stampList.add(templist.get(i));
            }
            templist = eventBiz.loadDatesHavingEvents(user_id, "finished");
            for (int i = 0; i < templist.size();i++){
                //Log.e(TAG, "timestamp=" + new Date(stampList.get(i)).toString());
                stampList.add(templist.get(i));
            }
            templist = eventBiz.loadDatesHavingEvents(user_id, "dropped");
            for (int i = 0; i < templist.size();i++){
                //Log.e(TAG, "timestamp=" + new Date(stampList.get(i)).toString());
                stampList.add(templist.get(i));
            }
            if(stampList == null){
                return;
            }

            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = stampList;
            handler.handleMessage(msg);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    void loadEvents(long user_id, long time_stamp){
        /** add decorator **/
        eventBiz = new EventBiz();
        eventList = new ArrayList<>();
        try {
            eventList = eventBiz.loadEventsOfOneDate(user_id, time_stamp);
            if(eventList == null){
                return;
            }
            for (int i = 0; i < eventList.size();i++){
                //Log.e(TAG, "event=" + eventList.get(i));
            }

            Message msg = Message.obtain();
            msg.what = 2;
            msg.obj = eventList;
            handler.handleMessage(msg);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
