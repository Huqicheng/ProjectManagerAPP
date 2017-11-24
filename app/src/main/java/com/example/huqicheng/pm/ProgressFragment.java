package com.example.huqicheng.pm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
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
import com.example.huqicheng.adapter.GroupProgressAdapter;
import com.example.huqicheng.bll.GroupBiz;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.dao.dbHandler;
import com.example.huqicheng.dao.dbHandler2;
import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.EventStat;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.message.MsgCache;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressFragment extends Fragment {

    private ListView groups;
    public List<Group> groupList;

    private GroupProgressAdapter adapter;
    private GroupBiz groupBiz;
    private Map<Long,EventStat> event_stats = new HashMap<Long,EventStat>();

    private  Handler handler = null;
    private User user = null;

    private TextView progress_text;
    private TextView group_deadline;
    private ProgressBar bar;
    public long groupSelected;

    int progress_Max = 100;
    int totalEvents = 0;
    int complete_count = 0;
    Intent intent;
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
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        //init event listview
        final View view = inflater.inflate(R.layout.group_progress, container, false);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                groups = view.findViewById(R.id.group_progress_list);
                //View view = inflater.inflate(R.layout.group_progress_list_row, container, false);
//
//                progress_text = (TextView) view.findViewById(R.id.group_progress_text);
//                bar = (ProgressBar) view.findViewById(R.id.group_bar);
//                bar.setMax(progress_Max);
//                bar.setProgress(0);
//                group_deadline = (TextView) view.findViewById(R.id.group_deadline);

            }
        });

        this.groups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToSelectedGroupProgress(i);
            }
        });

        groupBiz = new GroupBiz();

        user = new UserBiz(this.getActivity().getApplicationContext()).readUser();
        handler = new Handler(){
            @Override
            public void handleMessage(final Message msg) {
                switch (msg.what){
                    case 0:
                        //Toast.makeText(getApplicationContext(), "login failed" ,Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        groupList = (List<Group>) msg.obj;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new GroupProgressAdapter(getActivity(),null,null);
                                groups.setAdapter(adapter);
                                adapter.add(groupList,event_stats);
                                for (int i  =0;i<groupList.size();i++){
                                    Long gid =  groupList.get(i).getGroupId();
                                    if (event_stats.containsKey(gid)){
                                        //bar.setProgress(100);
                                    Log.d("lol",""+event_stats.get(gid));
                                    }
                                }
                                adapter.notifyDataSetChanged();

                            }
                        });

                        break;

                }
            }
        };

        return view;

    }

    private void ToSelectedGroupProgress(int i) {
        Group group = adapter.getItem(i);
       // Log.d("selected group id is ","" + group.getGroupId());

        intent = new Intent(getActivity(), GroupProgressSelected.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("group", group);//serializable

        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void toEditActivity(Event e){
        Intent intent = new Intent();
        //TODO jump to event edit activity


        //intent.setClass(getActivity(),DateSelected.class);
        //startActivityForResult(intent,1);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        //this.loadGroups(user.getUserId());

    }

    @Override
    public void onStart(){
        super.onStart();
        /** get data from the database**/
        new Thread(){
            @Override
            public void run() {
                loadGroups(user.getUserId());
                //loadStats(user.getUserId());
            }
        }.start();
    }

    private void loadStats(final long userId) {
        Map<Integer,EventStat> estat=  new GroupBiz().loadGropStats(userId);
        Message msg = Message.obtain();
        msg.what = 2;
        msg.obj = estat;
        handler.handleMessage(msg);

    }

    public void loadGroups(final long user_id){
       // event_stats = new Mp;

        new Thread(){
            @Override
            public void run() {
                List<Group> groupList = new GroupBiz().loadGroupinProgress(user_id);
                Map<Integer,EventStat> eventStatMap = new GroupBiz().loadGropStats(user_id);
                //event_stats =  ;
                for (Integer key : eventStatMap.keySet()){
                    Log.d("key",""+Long.valueOf(key) );
                    Log.d("val", ""+eventStatMap.get(key));
                    event_stats.put(Long.valueOf(key),eventStatMap.get(key));
                }
                if(groupList == null){
                    Message msg = Message.obtain();
                    msg.what = 0;
                    handler.handleMessage(msg);

                }
                else{
                    Message msg = Message.obtain();
                    msg.what = 1;

                    msg.obj = groupList;
                    handler.handleMessage(msg);
                }


            }
        }.start();
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
