package com.example.huqicheng.pm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.huqicheng.bll.GroupBiz;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.entity.User;

import java.util.ArrayList;
import java.util.List;

public class Member_addition extends AppCompatActivity {
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
        new Thread(){
            @Override
            public void run() {
                groupList = groupBiz.loadGroups(user.getUserId());
                if(groupList.size()>0)
                {
                    Log.d(TAG,"group list size"+groupList.size());

                    arraylist=new ArrayList<String>();
                    for(int i=0;i<groupList.size();i++){
                        arraylist.add(groupList.get(i).getGroupName());
                    }

                    adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arraylist);

                    lvGroups.setAdapter(adapter);
                    lvGroups.setOnItemClickListener(new ListItemClicker());
                }
            }
        }.start();








        //getting date from mainactivity

        //load user





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
                Intent i = new Intent(Member_addition.this, ChatActivity.class);
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