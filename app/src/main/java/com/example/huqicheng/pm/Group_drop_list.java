package com.example.huqicheng.pm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.huqicheng.adapter.GroupAdapter;
import com.example.huqicheng.bll.GroupBiz;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.entity.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Group_drop_list extends AppCompatActivity {
    Intent intent;
    private EditText groupName,groupDescription,projectName;
    private Button save;

    static final String TAG="Group_drop_list";
    public String assignresult = "";
    List<Group> groupList;

    private Intent INTENT;
    public Context context;
    private User user;
    private UserBiz userBiz;
    private ListView lvGroups;
    private ArrayAdapter<String> adapter;
    private GroupBiz groupBiz;
    private ArrayList<String> arraylist;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);
        userBiz = new UserBiz(this);
        user = userBiz.readUser();
        Log.d(TAG,"user_id"+user.getUserId());
        //Initializing the EditTexts
        groupBiz = new GroupBiz();
        groupList = new ArrayList<Group>();
        lvGroups = (ListView)findViewById(R.id.lvGroups);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 1:
                        arraylist = (ArrayList<String>) msg.obj;
                        adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arraylist);
                        lvGroups.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        break;

                }
            }
        };
        arraylist = new ArrayList<>();
        adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arraylist);

        lvGroups.setAdapter(adapter);
        lvGroups.setOnItemClickListener(new ListItemClicker());





        //getting date from mainactivity

        //load user

        new Thread(){
            @Override
            public void run() {
                groupList = groupBiz.loadGroups(user.getUserId());
                if(groupList.size()>0)
                {
                    Log.d(TAG,"group list size"+groupList.size());

                    ArrayList<String> arr=new ArrayList<String>();
                    for(int i=0;i<groupList.size();i++){
                        arr.add(groupList.get(i).getGroupName());
                    }

                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = arr;
                    handler.handleMessage(msg);


                }
            }
        }.start();



    }

    private class ListItemClicker implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position,view);
        }
    }
    private void selectItem(final int position,View view) {


         new Thread() {
            @Override
            public void run() {
                long group_id=groupList.get(position).getGroupId();
                long user_id=user.getUserId();
                Log.e(TAG, "user_id: " + user_id);
                Log.e(TAG, "group_id: " + group_id);
                assignresult = groupBiz.dropGroup(user_id, group_id);
                finish();
                Intent i = new Intent(Group_drop_list.this, ChatActivity.class);
                startActivity(i);
            }
        }.start();

        /**
        Log.d("ChatActivity","position"+position);
        if(position==0)
        {
            Intent intent=new Intent(this,GroupCreation.class);
            startActivity(intent);
        }
        else if(position==1)
        {
            Intent intent=new Intent(this,Group_drop_list.class);
            startActivity(intent);
            PopupMenu popup = new PopupMenu(ChatActivity.this, view);
            groupBiz = new GroupBiz();
            groupList = new ArrayList<Group>();
            groupList = groupBiz.loadGroups(user.getUserId());
            String[] limits=new String[]{"G8"};
            for (String s : limits) {
                popup.getMenu().add(s);
            }
            popup.show();
            new Thread(){
                @Override
                public void run() {
                    assignresult = groupBiz.dropGroup(2,2);
                }
            }.start();
            Log.e(TAG, "assignresult: " + assignresult);
            if (assignresult == "success"){
                finish();
                Intent i = new Intent(ChatActivity.this, ChatActivity.class);
                startActivity(i);
            }else if(assignresult == null) {
                Log.e(TAG, "assignresult: " + assignresult);
            }
        }
        else if(position==2){
            Intent intent=new Intent(this,Group_drop_list.class);
            startActivity(intent);
        }
        else{

        }
         **/
    }


}