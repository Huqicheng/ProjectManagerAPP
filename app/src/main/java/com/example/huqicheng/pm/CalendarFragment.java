package com.example.huqicheng.pm;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.huqicheng.adapter.EventListAdapter;
import com.example.huqicheng.bll.EventBiz;
import com.example.huqicheng.entity.Event;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {
    private EventBiz eventBiz;
    MaterialCalendarView CalendarView;
    private EventListAdapter adapter;
    public ArrayList<Event> eventList;
    Intent intent;
    static int incrementer;
    static final String TAG="TAG";

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
        Bundle args = new Bundle();
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

    /**
     * Decorate several days with a dot
     */
    public class HighlightDecorator implements DayViewDecorator {

        private int color;
        private HashSet<CalendarDay> dates;

        public HighlightDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(6, color));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Inflate the layout for this fragment
        CalendarView=(MaterialCalendarView)v.findViewById(R.id.calendarView);
        /** add decorator **/
        EventBiz eventBiz = new EventBiz();
        List<Long> stampList = new ArrayList<>();
        String status = "started";

        /** get data from the database**/
        try {
            //stampList = eventBiz.loadDatesHavingEvents(2, status);
            for (int i = 0; i < stampList.size();i++){
                Log.e(TAG, "timestamp=" + stampList.get(i));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Event>eventList = new ArrayList<>();
        long time_stamp = 1509854400488L;
       try{
           eventList = eventBiz.loadEventsOfOneDate(2,time_stamp);
           for (int i = 0; i < eventList.size();i++){
               Log.e(TAG, "eventList=" + eventList.toString());
           }
       }catch(Exception e){
           e.printStackTrace();
       }


        final List<CalendarDay> datesList = new ArrayList<>();
        datesList.add(CalendarDay.from(2017,10,15));//actually it's 11.15,cause month value need to add 1
        datesList.add(CalendarDay.from(2017,10,21));//11.21
        datesList.add(CalendarDay.from(2017,11,10));//12.10
        CalendarView.addDecorators(
                new HighlightDecorator(Color.parseColor("#FF4081"),datesList )
        );
        /*
        CalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)   //设置每周开始的第一天
                .setMinimumDate(CalendarDay.from(2015, 4, 3))  //设置可以显示的最早时间
                .setMaximumDate(CalendarDay.from(2018, 5, 12))//设置可以显示的最晚时间
                .setCalendarDisplayMode(CalendarMode.MONTHS)//设置显示模式，可以显示月的模式，也可以显示周的模式
                .commit();// 返回对象并保存
*/
        //event list
        eventList = new ArrayList<>();
        for(int i = 0;i<10;i++){

            Event e = new Event();
            e.setEventID(i);
            e.setEventTitle("Debug " + i);
            e.setEventDescription("woa " + i);
            eventList.add(e);
        }

        ListView listView = (ListView) v.findViewById(R.id.eventlist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d("clicked item: ", i + "at pos " + l);
            }
        });
        adapter = new EventListAdapter(getActivity(),null);
        listView.setAdapter(adapter);
        adapter.add(eventList);

        CalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull com.prolificinteractive.materialcalendarview.MaterialCalendarView widget, @NonNull com.prolificinteractive.materialcalendarview.CalendarDay date, boolean selected) {

                Event event = new Event();
                event.setEventDescription("discuss ece650 assignment LOL");
                event.setEventTitle("meeting");
                event.setEventLocation("E3");
                //intent = new Intent(getActivity(), DateSelected.class);
                Bundle bundle = new Bundle();
                event.setEventID(date.hashCode());
                event.setEventDescription("date message");
                long stamp = date.getCalendar().getTimeInMillis();
                event.setCreatedAt(stamp);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String s = sdf.format(new Timestamp(stamp));
                Log.e(TAG, "string date=" + s);
                Log.e(TAG, "timestamp=" + stamp);

                if (datesList.contains(date)){
                    //get evetn from database
                    // get_event_by_date(CalendarDay.from(2015.4.3))

                    event.setEventStatus("started");
                    bundle.putSerializable("event", event);//serializable
                    intent.putExtras(bundle);//send data
                    startActivity(intent);
                    return;
                }

                //intent to dateselected activity

                event.setEventStatus("initiated");
                bundle.putSerializable("event", event);//serializable
                intent.putExtras(bundle);//send data
                startActivity(intent);
            }
        });
        return v;
    }

    public void showMessage(String title,String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(content);
        AlertDialog alert= builder.create();
        alert.show();
    }
    /***  show message ***/
    /*
    // TODO: Rename method, update argument and hook method into UI event
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        // set three button on the dialog box ( Edit, Delete , Cancel)
        builder.setMessage(Message).setPositiveButton("Edit Event", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                intent = new Intent(getActivity(), editClicked.class);
                Cursor cursor = myDb2.getAllData();
                while(cursor.moveToNext()) {
                    String test = cursor.getInt(0) + "";
                    if (test.contains(date)) {
                        int changedDateId = Integer.parseInt(test);
                        myDb2.deleteEvent(changedDateId);
                    }
                }
                intent.putExtra("date message", Date);
                startActivity(intent);
            }
        }).setNeutralButton("Delete Event", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int result  =myDb.deleteEvent(Date);
                Cursor cursor = myDb2.getAllData();
                while(cursor.moveToNext()) {
                    String test = cursor.getInt(0) + "";
                    if (test.contains(date)) {
                        int changedDateId = Integer.parseInt(test);
                        myDb2.deleteEvent(changedDateId);
                    }
                }
                // checking if data is deleted or not.
                if(result == 1 )
                    Toast.makeText(getActivity(),"Data deleted",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(),"Failed to delete Data",Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("Cancel",null);
        //builder.show();
        AlertDialog alert= builder.create();
        alert.show();
    }

    */



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
