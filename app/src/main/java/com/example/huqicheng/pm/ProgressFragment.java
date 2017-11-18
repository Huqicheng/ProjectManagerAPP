package com.example.huqicheng.pm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    //dbHandler myDb;
    private ListView events;
    public ArrayList<Event> eventList;
    private EventListAdapter adapter;
    TextView progress_text;
    ProgressBar bar;
    int progress_Max = 100;
    int totalEvents = 0;
    int complete_count = 0;
    //dbHandler2 myDb2;
    int Date;

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

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        //init event listview
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        eventList = new ArrayList<>();
        for(int i = 0;i<10;i++){

            Event e = new Event();
            long id = Integer.valueOf(String.valueOf(20)+String.valueOf(i));
            e.setEventID(id);
            e.setEventTitle("Debug " + i);
            e.setDescription("woa " + i);
            eventList.add(e);
        }
        totalEvents = eventList.size();
        ListView listView = (ListView) view.findViewById(R.id.eventlist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d("clicked item: ", i + "at pos " + l);
                //CheckBox cb = (CheckBox)view.getTag(R.id.chk_box);
                //Log.d("is ",ifChecked(cb));

            }
        });
        adapter = new EventListAdapter(getActivity(),null);
        listView.setAdapter(adapter);
        adapter.add(eventList);
        progress_text = (TextView) view.findViewById(R.id.progress_text);
        bar = (ProgressBar) view.findViewById(R.id.bar);
        bar.setProgress(0);
        progress_text.setText(complete_count + " of " + eventList.size() + " completed ");
        ProgressButtonClick(view);

        return view;

    }

    public void ProgressButtonClick(View view) {
        //mark selected events as complete
        ImageButton complete_btn = (ImageButton) view.findViewById(R.id.complete_btn);
        eventComplete(complete_btn);

        //delete selected events
        ImageButton del_btn = (ImageButton) view.findViewById(R.id.delete_btn);
        eventDelete(del_btn);

        //add new event
        ImageButton add_btn = (ImageButton) view.findViewById(R.id.add_btn);
        eventAdd(add_btn);
    }



    public void eventComplete(ImageButton complete_btn) {
        complete_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                complete_count += adapter.selectedEvents.size();
                progress_text.setText(complete_count+" of " + totalEvents + " completed ");
                bar.setProgress(complete_count*progress_Max/totalEvents);

                for (Long i : adapter.selectedEvents){
                    adapter.remove(i);
                    adapter.notifyDataSetChanged();
                }
                MsgDisplay("complete.");
                Log.d("esize", ""+eventList.size());
                adapter.selectedEvents.clear();

            }

        });
    }

    public void eventDelete(ImageButton del_btn) {
        del_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                totalEvents -= adapter.selectedEvents.size();
                progress_text.setText(complete_count+" of " + totalEvents + " completed ");
                bar.setProgress(complete_count*progress_Max/totalEvents);
                for (Long i : adapter.selectedEvents){
                    adapter.remove(i);
                    eventList.remove(i);
                    adapter.notifyDataSetChanged();
                }
                MsgDisplay("deleted.");
                adapter.selectedEvents.clear();
            }

        });
    }

    private void eventAdd(ImageButton add_btn) {
        //TODO: jump to event add activity
    }

    public void MsgDisplay(String s) {
        //display opertaion messages

        if(adapter.selectedEvents.size() == 1){
            Toast.makeText(getActivity(), adapter.selectedEvents.size()+" event " +s, Toast.LENGTH_SHORT).show();

        }
        else if (adapter.selectedEvents.size() >1){
            Toast.makeText(getActivity(), adapter.selectedEvents.size()+" events "+s, Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(getActivity(), "Please make a selection.", Toast.LENGTH_SHORT).show();

        }
    }
    private void toEditActivity(Event e){
        Intent intent = new Intent();
        //TODO jump to event edit activity


        //intent.setClass(getActivity(),DateSelected.class);
        //startActivityForResult(intent,1);

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
