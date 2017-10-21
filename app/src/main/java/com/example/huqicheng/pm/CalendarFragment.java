package com.example.huqicheng.pm;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.huqicheng.dao.dbHandler;
import com.example.huqicheng.dao.dbHandler2;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalendarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    CalendarView calendarView;
    Intent intent;
    String date;
    int Date;
    dbHandler myDb;
    dbHandler2 myDb2;
    dateSelected dateselected;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Inflate the layout for this fragment
        myDb=new dbHandler(this.getActivity());
        myDb2=new dbHandler2(this.getActivity());
        calendarView=(CalendarView)v.findViewById(R.id.calendarView);
        dateselected = new dateSelected();


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                date = i2 + "" + i1 + "" + i;
                Date = Integer.parseInt(date);
                Log.e(TAG, "int date=" + Date + "");
                Log.e(TAG, "string date=" + date + "");

                //firstly check if an entry exists for the current date.
                Cursor res = myDb.getAllData();

                while (res.moveToNext()) {
                    if (res.getInt(0) == Date) {    //only runs if it find the id for this date.
                        StringBuffer buffer = new StringBuffer();

                        buffer.append("Id :" + res.getString(0) + "\n");
                        buffer.append("Event Name :" + res.getString(1) + "\n");
                        buffer.append("Event Location :" + res.getString(2) + "\n");
                        buffer.append("Event Discription :" + res.getString(3) + "\n\n");

                        //when an id is present for event, than there must be an entry for attendees, So search by date(id) for attendees.
                        Cursor cursor = myDb2.getAllData();
                        while (cursor.moveToNext()) {
                            String test = cursor.getInt(0) + "";
                            if (test.contains(date)) {
                                Log.e(TAG, "it reached here");
                                buffer.append("Event Attendee :" + cursor.getString(1) + "\n\n");
                            }
                        }
                        // show message
                        showMessage("Data", buffer.toString());
                        return;
                    }
                }
                //if no entry is found then pass the id as Date and shift to activity_date_selected activity ).
                intent = new Intent(getActivity(), dateSelected.class);
                intent.putExtra("date message", Date);
                intent.putExtra("day message", i2);
                intent.putExtra("month message", i1);
                intent.putExtra("year message", i);
                startActivity(intent);
                /****            ****/
            }
        });
        return v;
    }
    /***  show message ***/
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
