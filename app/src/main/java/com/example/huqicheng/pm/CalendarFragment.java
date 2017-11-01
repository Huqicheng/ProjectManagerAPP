package com.example.huqicheng.pm;

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
import android.widget.*;
import android.widget.Toast;

import com.example.huqicheng.entity.Event;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
    MaterialCalendarView CalendarView;
    //CalendarView calendarView;
    Intent intent;
    //dbHandler myDb;
    //dbHandler2 myDb2;
    //dateSelected dateselected;
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
        List<CalendarDay> datesList = new ArrayList<>();
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
        CalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull com.prolificinteractive.materialcalendarview.MaterialCalendarView widget, @NonNull com.prolificinteractive.materialcalendarview.CalendarDay date, boolean selected) {
                Log.e(TAG, "int date=" + date.getYear() + "" + date.getMonth() + "" + date.getDay());
                Log.e(TAG, "string date=" + date.toString() + "");
                Log.e(TAG, "int date=" + date.hashCode());

                intent = new Intent(getActivity(), dateSelected.class);

                Bundle bundle = new Bundle();
                Event event = new Event();
                event.setEventID(date.hashCode());
                event.setEventDescription("date message");
                event.setCreatedAt(date.getDate());

                bundle.putSerializable("event", event);//serializable
                intent.putExtras(bundle);//send data
                startActivity(intent);
                /****            ****/
            }
        });
        return v;
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
