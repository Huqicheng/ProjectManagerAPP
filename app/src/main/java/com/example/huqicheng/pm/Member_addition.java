package com.example.huqicheng.pm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.huqicheng.bll.EventBiz;
import com.example.huqicheng.bll.GroupBiz;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.utils.DateUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Member_addition extends AppCompatActivity {
    Intent intent;
    private Handler handler = null;
    private Handler handler1 = null;
    private GroupBiz groupBiz;
    private GroupBiz groupBiz1;
    private UserBiz userBiz;
    private List<Group> groupList;
    private List<User> userList;
    public  List<String> assignToList;
    public  List<String> assignToList1;
    private Map<String,Long> userNameIdMap;

    private Button save;
    private Button time_picker, date_picker;
    private TextView tvU,tvG;
    private Spinner spinnerU,spinnerG;
    static final String TAG="TAG";
    public String assignresult = "";
    public int month_x;
    public EventBiz eventBiz = new EventBiz();
    public Context context = this;
    private User user;
    public Event event;
    public Event event_save;
    private Long deadline;
    private String str_date;
    private long user_id;
    private long group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_add);

        //Initializing the EditTexts

        //Initializing the buttons
        save = (Button) findViewById(R.id.btnSave);



        //creating the instance of the calander
        final Calendar calendar = Calendar.getInstance();
        month_x = calendar.get(Calendar.MONTH)+1;

        //load user
        userBiz = new UserBiz(this);
        user = userBiz.readUser();



        /**  test spinner **/
        //Initializing textview textAssignto
        tvG = (TextView) findViewById(R.id.tvGroup);
        //Initializing the Assign-to Spinner
        spinnerG = (Spinner) findViewById(R.id.spinnerGroup);
        tvU = (TextView) findViewById(R.id.tvUser);
        //Initializing the Assign-to Spinner
        spinnerU = (Spinner) findViewById(R.id.spinnerUser);
        new Thread(){
            @Override
            public void run() {
                //CalendarView.addDecorator(decorator);
                loadAssignToListForGroup();
                loadAssignToListForUser();
            }
        }.start();


        handler = new Handler() {
            @Override
            public void handleMessage(final Message msg){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        assignToList = (ArrayList<String>)msg.obj;
                        for (int k = 0; k < assignToList.size(); k++) {
                            Log.d("spinner list in DS", "" + assignToList.get(k));
                            //cityList.add(assignToList.get(k));
                        }
                        //getApplicationContext()
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getApplicationContext(), R.layout.spinner_style,
                                assignToList);
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
                        spinnerU.setAdapter(adapter);
                        spinnerU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                       int arg2, long arg3) {
                                String str = arg0.getItemAtPosition(arg2).toString();
                                //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                                for(int i=0;i<userList.size();i++)
                                {
                                    if(userList.get(i).getUsername().equals(str))
                                        user_id=userList.get(i).getUserId();
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub

                            }
                        });
                    }
                });



            }
        };
        handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg){
                assignToList1 = (ArrayList<String>)msg.obj;
                for (int k = 0; k < assignToList1.size(); k++) {
                    Log.d("spinner list in DS", "" + assignToList1.get(k));
                    //cityList.add(assignToList.get(k));
                }
                //getApplicationContext()
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getApplicationContext(), R.layout.spinner_style,
                        assignToList1);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
                spinnerG.setAdapter(adapter);
                spinnerG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        String str = arg0.getItemAtPosition(arg2).toString();
                        //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                        for(int i=0;i<groupList.size();i++)
                        {
                            if(groupList.get(i).getGroupName().equals(str))
                                group_id=groupList.get(i).getGroupId();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });


            }
        };
        //get flag that decide whether it is an edit event of init event







        //get timestamp and string date


        //save button clicked
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    new Thread() {
                        @Override
                        public void run() {
                            groupBiz = new GroupBiz();
                            assignresult = groupBiz.joinGroup(user_id,group_id);
                            Log.e(TAG, "assignresult: " + assignresult);
                            if (assignresult == "success") {
                                //Toast.makeText(Member_addition.this, "member saved", Toast.LENGTH_LONG).show();
                                finish();
                                Intent intent=new Intent(Member_addition.this,ChatActivity.class);
                                startActivity(intent);
                            } else  {
                               // Toast.makeText(Member_addition.this, "Failed to save member", Toast.LENGTH_LONG).show();
                                finish();
                                Intent intent=new Intent(Member_addition.this,ChatActivity.class);
                                startActivity(intent);
                            }
                        }
                    }.start();

                }


        });



    }


    void loadAssignToListForUser(){
        groupBiz = new GroupBiz();

        userList = new ArrayList<User>();
        assignToList = new ArrayList<String>();
        Log.d("spinner list in DS", "" + "test spinner");

        userList = groupBiz.getAllUser("");
        for (int j = 0; j < userList.size(); j++) {
            Log.d("spinner list in DS", "" + j);
            assignToList.add(userList.get(j).getUsername());
        }

        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = assignToList;
        handler.handleMessage(msg);
    }
    void loadAssignToListForGroup(){
        groupBiz1 = new GroupBiz();
        groupList = new ArrayList<Group>();

        assignToList1 = new ArrayList<String>();
        Log.d("spinner list in DS", "" + "test spinner1");
        groupList = groupBiz1.loadGroups(user.getUserId());
        for (int i = 0; i < groupList.size(); i++) {

                assignToList1.add(groupList.get(i).getGroupName());


        }
        Message msg1 = Message.obtain();
        msg1.what = 1;
        msg1.obj = assignToList1;
        handler1.handleMessage(msg1);
    }
}