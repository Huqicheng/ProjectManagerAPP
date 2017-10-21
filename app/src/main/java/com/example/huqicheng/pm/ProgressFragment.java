package com.example.huqicheng.pm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

import com.example.huqicheng.adapter.EventListAdapter;
import com.example.huqicheng.dao.dbHandler;
import com.example.huqicheng.dao.dbHandler2;
import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.Group;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressFragment extends Fragment {

    dbHandler myDb;
    private ListView events;
    private EventListAdapter adapter;
    dbHandler2 myDb2;
    int Date;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressFragment newInstance() {
        ProgressFragment fragment = new ProgressFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
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
        // Inflate the layout for this fragment
        //myDb = new dbHandler(getActivity());
        //Cursor res = myDb.getAllData();
        //while (res.moveToNext()) {
        //    if (res.getInt(0) != 0) {
//                StringBuffer buffer = new StringBuffer();
//
//                buffer.append("Id :" + res.getString(0) + "\n");
//                buffer.append("Event Name :" + res.getString(1) + "\n");
//                buffer.append("Event Location :" + res.getString(2) + "\n");
//                buffer.append("Event Discription :" + res.getString(3) + "\n\n");
//                System.out.print(res.getString(1));

           // }

//        }
       View view = inflater.inflate(R.layout.fragment_progress, container, false);
        ArrayList<Event> eventList = new ArrayList<>();



        for(int i = 0;i<10;i++){

            Event e = new Event();
            e.setEventID(i);
            e.setEventTitle("Debug " + i);
            e.setEventDescription("woa " + i);
            eventList.add(e);
        }
        ListView listView = (ListView) view.findViewById(R.id.eventlist);
        EventListAdapter ela = new EventListAdapter(getActivity(),null);
        listView.setAdapter(ela);
        ela.add(eventList);





        return view;

    }

    private void toEditActivity(Event e){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("event",e);
        intent.putExtras(bundle);
        intent.setClass(getActivity(),editClicked.class);
        startActivityForResult(intent,1);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
