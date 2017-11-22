package com.example.huqicheng.pm;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huqicheng.adapter.EventListAdapter;
import com.example.huqicheng.entity.Event;

import java.util.ArrayList;


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
    private ListView events;
    public ArrayList<Event> eventList;
    private EventListAdapter adapter;
    private  Handler handler = null;
    TextView progress_text;
    ProgressBar bar;
    int progress_Max = 100;
    int totalEvents = 0;
    int complete_count = 0;
    Intent intent;
    //dbHandler2 myDb2;
    int Date;

    private OnFragmentInteractionListener mListener;

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

        //init event listview
        setContentView(R.layout.single_group_events);
        bar = (ProgressBar) findViewById(R.id.bar);
        bar.setProgress(0);
        progress_text = (TextView) findViewById(R.id.progress_text);



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
        //ListView listView = (ListView) view.findViewById(R.id.eventlist);
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event event = adapter.getEventList().get(i);
                //intent = new Intent(getActivity(), DateSelected.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("event", event);//serializable
                bundle.putSerializable("flag", DateSelected.EDIT);//indicating EDIT event or INIT event
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });*/
        //adapter = new EventListAdapter(getActivity(),null);
        //listView.setAdapter(adapter);
        //adapter.add(eventList);
        //progress_text = (TextView) view.findViewById(R.id.progress_text);
        //bar = (ProgressBar) view.findViewById(R.id.bar);

        progress_text.setText(complete_count + " of " + eventList.size() + " completed ");
        //ProgressButtonClick(view);

        //return view;

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
                //Log.d("esize", ""+eventList.size());
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


    private void eventAdd(final ImageButton add_btn) {
        //attempt 1
        add_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                /*getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("test", "success");
                        Event event =  new Event();
                        //intent = new Intent(getActivity(), DateSelected.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("event", event);//serializable
                        bundle.putSerializable("flag", DateSelected.INIT);//indicating EDIT event or INIT event
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });*/



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
            //Toast.makeText(getActivity(), adapter.selectedEvents.size()+" event " +s, Toast.LENGTH_SHORT).show();

        }
        else if (adapter.selectedEvents.size() >1){
            //Toast.makeText(getActivity(), adapter.selectedEvents.size()+" events "+s, Toast.LENGTH_SHORT).show();

        }
        else{
            //Toast.makeText(getActivity(), "Please make a selection.", Toast.LENGTH_SHORT).show();

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
