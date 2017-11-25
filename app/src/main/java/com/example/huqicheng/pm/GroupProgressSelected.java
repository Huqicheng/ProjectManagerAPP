package com.example.huqicheng.pm;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huqicheng.adapter.EventListAdapter;
import com.example.huqicheng.bll.EventBiz;
import com.example.huqicheng.bll.GroupBiz;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.EventStat;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupProgressSelected.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupProgressSelected#} factory method to
 * create an instance of this fragment.
 */
public class GroupProgressSelected extends AppCompatActivity {

    //dbHandler myDb;
    private ListView event_list;
    private UserBiz userBiz;
    private User user;
    private ArrayList<Event> events = new ArrayList<>();
    private  ArrayList<Event> started_events = new ArrayList<>();
    private ArrayList<Event> finished_events = new ArrayList<>();
    private ArrayList<Event> dropped_events = new ArrayList<>();

    private ImageButton complete_btn;
    private ImageButton dropped_btn;
    private ImageButton add_btn;

    private Group selected_group = new Group();
    public ArrayList<Event> eventList;
    private EventListAdapter adapter;
    private  Handler handler = null;
    TextView progress_text;
    ProgressBar bar;
    public EventBiz eventBiz = new EventBiz();
    public String updateResult = "";
    int progress_Max = 100;
    int totalEvents = 0;
    int complete_count = 0;
    Intent intent;


    private OnFragmentInteractionListener mListener;
    private Map<Long, EventStat> event_stats =  new HashMap<>();
    private int started_num = 0;
    private int finished_num = 0;
    private int total_num = 0;


 /*   public GroupProgressSelected() {
        // Required empty public constructor
    }*/

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
/*    public static GroupProgressSelected newInstance() {
        GroupProgressSelected fragment = new GroupProgressSelected();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent groupintent = this.getIntent();
        //get event selected
        selected_group = (Group) groupintent.getSerializableExtra("group");
        Log.d("selected eid",""+selected_group.getGroupId());

        //get current user_id
        userBiz = new UserBiz(this);
        user = userBiz.readUser();
        new Thread(){
            @Override
            public void run() {
                loadEvents(selected_group.getGroupId(),user.getUserId());
            }
        }.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
               ArrayList<Event> tmp = (ArrayList<Event>) msg.obj;
                sortEventByStatus(tmp);

                GroupProgressSelected.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setContentView(R.layout.single_group_events);
                        bar = (ProgressBar) findViewById(R.id.bar);
                        if(event_stats.containsKey(selected_group.getGroupId())){
                            started_num = event_stats.get(selected_group.getGroupId()).getStarted();
                            finished_num = event_stats.get(selected_group.getGroupId()).getFinished();
                        }

                        total_num = started_num +finished_num;
                        bar.setMax(total_num);
                        bar.setProgress(finished_num);
                        progress_text = (TextView) findViewById(R.id.progress_text);
                        progress_text.setText(finished_num + " of " + total_num + " completed");
                        complete_btn = (ImageButton) findViewById(R.id.complete_btn);
                        eventComplete(complete_btn);
                        dropped_btn = (ImageButton) findViewById(R.id.delete_btn);
                        eventDrop(dropped_btn);
                        add_btn = (ImageButton) findViewById(R.id.add_btn);
                        if(selected_group.getGroupId() != 1){
                            eventAdd(add_btn);
                        }
                        else {
                            add_btn.setVisibility(View.INVISIBLE);
                        }


                        event_list = (ListView) findViewById(R.id.eventlist);

                        adapter = new EventListAdapter(GroupProgressSelected.this,null);
                        event_list.setAdapter(adapter);
                        adapter.add(events);
                        event_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Event event = adapter.getEventList().get(i);
                                intent = new Intent(GroupProgressSelected.this, DateSelected.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("event", event);
                                bundle.putSerializable("flag", DateSelected.EDIT);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            }
                        });
                        adapter.notifyDataSetChanged();

                    }
                });

            }
        };





        //init event listview

        //Log.d("groupselected",""+ProgressFragment.newInstance().groupSelected);


/*
        eventList = new ArrayList<>();
        for(int i = 0;i<10;i++){

            Event e = new Event();
            long id = Integer.valueOf(String.valueOf(20)+String.valueOf(i));
            e.setEventID(id);
            e.setEventTitle("Debug " + i);
            e.setDescription("woa " + i);
            eventList.add(e);
        }
        totalEvents = eventList.size();*/

        //adapter = new EventListAdapter(getActivity(),null);
        //listView.setAdapter(adapter);
        //adapter.add(eventList);
        //progress_text = (TextView) view.findViewById(R.id.progress_text);
        //bar = (ProgressBar) view.findViewById(R.id.bar);

        //progress_text.setText(complete_count + " of " + eventList.size() + " completed ");
        //ProgressButtonClick(view);

        //return view;

    }

    private void sortEventByStatus(ArrayList<Event> tmp) {
        for (Event e : tmp){
            if (e.getEventStatus().equals("started"))
                started_events.add(e);
            else if( e.getEventStatus().equals("finished"))
                finished_events.add(e);
            else if(e.getEventStatus().equals("dropped"))
                dropped_events.add(e);
        }
        events.addAll(started_events);
        events.addAll(finished_events);
        events.addAll(dropped_events);
    }

    private void loadEvents(final long group_id, final long user_id) {
        List<Event> eventList =  new EventBiz().loadEventsByGroup(group_id,user_id);
        Map<Integer,EventStat> eventStatMap = new GroupBiz().loadGropStats(user_id);
        //event_stats =  ;
        for (Integer key : eventStatMap.keySet()){
            Log.d("key",""+Long.valueOf(key) );
            Log.d("val", ""+eventStatMap.get(key));
            event_stats.put(Long.valueOf(key),eventStatMap.get(key));
        }
        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = eventList;
        handler.handleMessage(msg);

    }

    public void ProgressButtonClick(View view) {
        //mark selected events as complete
        ImageButton complete_btn = (ImageButton) view.findViewById(R.id.complete_btn);
        eventComplete(complete_btn);

        //delete selected events
        ImageButton del_btn = (ImageButton) view.findViewById(R.id.delete_btn);
        eventDrop(del_btn);

        //add new event
        ImageButton add_btn = (ImageButton) view.findViewById(R.id.add_btn);
        eventAdd(add_btn);
    }



    public void eventComplete(ImageButton complete_btn) {
        complete_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                
                new Thread() {
                    @Override
                    public void run() {
                        updateResult = eventBiz.updateEventStatus(adapter.selectedEvents,"finished");

                    }
                }.start();
                GroupProgressSelected.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (updateResult == "success") {
                            Toast.makeText(GroupProgressSelected.this, adapter.selectedEvents.size()+"event(s) completed!", Toast.LENGTH_SHORT).show();

                            intent = new Intent(getApplicationContext(), GroupProgressSelected.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("group", selected_group);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else if (updateResult == null) {
                            Toast.makeText(GroupProgressSelected.this, "Failed to update event status", Toast.LENGTH_SHORT).show();
                        }
                        adapter.selectedEvents.clear();
                    }
                });
            }

        });
    }

    public void eventDrop(ImageButton del_btn) {
        del_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {

                new Thread() {
                    @Override
                    public void run() {
                        updateResult = eventBiz.updateEventStatus(adapter.selectedEvents,"dropped");

                    }
                }.start();
                GroupProgressSelected.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (updateResult == "success") {
                            Toast.makeText(GroupProgressSelected.this, adapter.selectedEvents.size()+"event(s) dropped!", Toast.LENGTH_SHORT).show();
                            //bar.setMax(finished_num+started_num-adapter.selectedEvents.size());
                            intent = new Intent(getApplicationContext(), GroupProgressSelected.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("group", selected_group);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else if (updateResult == null) {
                            Toast.makeText(GroupProgressSelected.this, "Failed to update event status", Toast.LENGTH_SHORT).show();
                        }
                        adapter.selectedEvents.clear();
                    }
                });
            }

        });
    }


    private void eventAdd(final ImageButton add_btn) {
        //attempt 1
        add_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                GroupProgressSelected.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("test", "success");
                        Event event =  new Event();
                        intent = new Intent(GroupProgressSelected.this, DateSelected.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("event", event);//serializable
                        bundle.putSerializable("group",selected_group);
                        bundle.putSerializable("flag", DateSelected.INIT);//indicating EDIT event or INIT event
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });



            }

        });


        //TODO: jump to event add activity
        //attempt 2
/*        final Handler handler=new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                add_btn.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v)
                    {
                        Log.d("test", "success");
                        Event event =  new Event();
                        intent = new Intent(getActivity(), DateSelected.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("event", event);//serializable
                        bundle.putSerializable("flag", DateSelected.INIT);//indicating EDIT event or INIT event
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }

                });
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        add_btn.invalidate();
                    }
                });
            }
        }).start();*/


// attempt 3

 /*       handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Event event =  new Event();
                        intent = new Intent(getActivity(), DateSelected.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("event", event);//serializable
                        bundle.putSerializable("flag", DateSelected.INIT);//indicating EDIT event or INIT event
                        intent.putExtras(bundle);
                        startActivity(intent);


                    }
                });
            }

        };

        add_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {  Message msg = Message.obtain();
               handler.handleMessage(msg);

            }

        });
*/




    }



    public void MsgDisplay(String s) {
        //display opertaion messages

        if(adapter.selectedEvents.size() == 1){
            Toast.makeText(GroupProgressSelected.this, adapter.selectedEvents.size()+" event " +s, Toast.LENGTH_SHORT).show();

        }
        else if (adapter.selectedEvents.size() >1){
            Toast.makeText(GroupProgressSelected.this, adapter.selectedEvents.size()+" events "+s, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(GroupProgressSelected.this, "Please make a selection.", Toast.LENGTH_SHORT).show();

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

   /* @Override
    public void onAttach(Context context) {
        //super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        //super.onDetach();
        mListener = null;
    }*/

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
