package com.example.huqicheng.pm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.huqicheng.adapter.GroupAdapter;
import com.example.huqicheng.bll.GroupBiz;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.message.BaseMsg;
import com.example.huqicheng.message.MsgType;
import com.example.huqicheng.service.OnChatMsgRecievedListener;
import com.example.huqicheng.utils.ClientUtils;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    private ListView lvGroups;
    private GroupAdapter adapter;
    private GroupBiz groupBiz;

    User user = null;
    Handler handler = null;
    private OnFragmentInteractionListener mListener;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        this.lvGroups = v.findViewById(R.id.lvGroups);

        this.lvGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toChatActivity(adapter.getItem(i));
            }
        });

        this.adapter = new GroupAdapter(this.getActivity(),null,lvGroups);
        groupBiz = new GroupBiz();

        this.lvGroups.setAdapter(adapter);
        user = new UserBiz(this.getActivity().getApplicationContext()).readUser();

        handler = new Handler(){
            @Override
            public void handleMessage(final Message msg) {
                switch (msg.what){
                    case 0:
                        //Toast.makeText(getApplicationContext(), "login failed" ,Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<Group> groups = (List<Group>) msg.obj;
                                Log.d("debug:",groups.size()+"");
                                adapter.add(groups);
                                adapter.notifyDataSetChanged();
                            }
                        });
                        break;
                    case 2:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Long gid = (Long)msg.obj;

                                updateListWhenMsgComes(gid);
                            }
                        });
                        break;

                }
            }
        };

        ClientUtils.setListenerForGroupList(new OnChatMsgRecievedListener() {
            @Override
            public void onChatMsgRecieved(BaseMsg msg) {
                if(msg == null || msg.getType() == null){
                    return;
                }
                if(!msg.getType().equals(MsgType.ReplyForChatMsg)||!msg.getType().equals(MsgType.ChatMsg)){
                    return;
                }

                String groupId = msg.getGroupId();

                try{
                    Long gId = Long.parseLong(groupId);

                    Message message = Message.obtain();
                    message.what = 2;
                    message.obj = gId;

                    handler.handleMessage(message);

                }catch(Exception e){
                    Log.d("msg recieved", "parse error");
                }

            }

            @Override
            public long getId() {
                return user.getUserId();
            }
        });
        return v;
    }

    // TODO a new msg come to group identified by  gid
    public void updateListWhenMsgComes(long gid){

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        this.loadGroups(user.getUserId());

    }

    public void loadGroups(final long user_id){

        new Thread(){
            @Override
            public void run() {
                List<Group> groupList = new GroupBiz().loadGroups(user_id);

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


    private void toChatActivity(Group group){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("group",group);
        intent.putExtras(bundle);
        intent.setClass(getActivity(),WeChatActivity.class);
        startActivityForResult(intent,1);

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ClientUtils.setListenerForGroupList(null);
    }
}
