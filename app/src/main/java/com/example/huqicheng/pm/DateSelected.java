package com.example.huqicheng.pm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class DateSelected extends AppCompatActivity {
    Intent intent;
    private Handler handler = null;
    private GroupBiz groupBiz;
    private UserBiz userBiz;
    private List<Group> groupList;
    private List<User> userList;
    public  List<User> assignToList;
    public List<String> assignToStringList;
    private EditText eventName,eventDiscription;
    private Button save;
    private Button time_picker, date_picker;
    private TextView textClock,textDate, textAssignto, textAssignedbyName;
    private Spinner spinner;
    static final String TAG="TAG";
    public String assignresult = "";
    public int month_x;
    public EventBiz eventBiz = new EventBiz();
    public Context context = this;
    private User user;
    public Event event;
    private Group group;
    public Event event_save;
    private Long deadline;
    private long assignToId;
    private String str_date;
    private int sYear = 0;
    private int sMonth = 0;
    private int sDay = 0;
    private int sHour = 0;
    private int sMinute = 0;
    private int sSecond = 0;
    public static final int INIT = 1;
    public static final int EDIT = 2;
    public int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selected);

        //Initializing the EditTexts
        eventName = (EditText) findViewById(R.id.etEventname);
        eventDiscription = (EditText) findViewById(R.id.etEventDiscription);

        //Initializing the buttons
        save = (Button) findViewById(R.id.btnSave);
        time_picker = (Button) findViewById(R.id.btnTimepicker);
        date_picker=(Button) findViewById(R.id.btnDatepicker);

        //Initializing the TextViews of the Activity
        textClock = (TextView) findViewById(R.id.tvDisplaytime);
        textDate = (TextView) findViewById(R.id.tvCurrentdate);
        textAssignedbyName = (TextView) findViewById(R.id.tvAssignbyName);


        //creating the instance of the calander
        final Calendar calendar = Calendar.getInstance();
        month_x = calendar.get(Calendar.MONTH)+1;
        textClock.setText(calendar.get(Calendar.HOUR) + " : " + calendar.get(Calendar.MINUTE));
        textDate.setText(calendar.get(Calendar.YEAR)+" / "+month_x+" / "+calendar.get(Calendar.DATE));

        spinner = (Spinner) findViewById(R.id.spinnerAssignto);

        //load user
        userBiz = new UserBiz(this);
        user = userBiz.readUser();

        //getting intent
        Intent eventintent = this.getIntent();

        //getting event
        event = (Event)eventintent.getSerializableExtra("event");
        assignToId = event.getAssignedTo();
        textAssignedbyName.setText(event.getAssignByName());

                //Log.d("event description DS",""+event.getDescription());
        //get flag that decide whether it is an edit event of init event
        flag = (int)eventintent.getSerializableExtra("flag");
        group = (Group)eventintent.getSerializableExtra("group");

        if (flag == INIT){
            //Initializing textview textAssignto
            textAssignto = (TextView) findViewById(R.id.tvAssignto);
            //Initializing the Assign-to Spinner

            save.setText("save");
            new Thread(){
                @Override
                public void run() {
                    //CalendarView.addDecorator(decorator);
                    loadAssignToList(flag);
                }
            }.start();
        }
        if (flag == EDIT) {
            Date date = new Date(event.getDeadLine());

            //Timestamp timestamp = new Timestamp(event.getDeadLine());
            CalendarDay calendarDay = CalendarDay.from(date);
            sYear = calendarDay.getYear();
            sMonth = calendarDay.getMonth()+1;
            sDay = calendarDay.getDay();
            sHour = date.getHours();
            sMinute = date.getMinutes();
            //Log.d("Time in DS",""+sYear+sMonth+sDay+sHour+sMinute);
            //Log.d("Date in DS",""+date.toString());
            eventName.setText(event.getEventTitle());
            eventDiscription.setText(event.getDescription());
            save.setText("update");
            if (!event.getEventStatus().equals("started") || event.getAssignedTo() != user.getUserId()){
                save.setVisibility(View.INVISIBLE);
                //save.setOnClickListener(null);
            }
            new Thread(){
                @Override
                public void run() {
                    loadAssignToList(flag);
                }
            }.start();


        }


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 1:
                        assignToList = (ArrayList<User>)msg.obj;
                        /*for (int k = 0; k < assignToList.size(); k++) {
                            //Log.d("spinner list in DS", "" + assignToList.get(k));
                            //cityList.add(assignToList.get(k));
                        }*/
                        assignToStringList.clear();
                        for (int m = 0; m < assignToList.size(); m++){
                            assignToStringList.add(assignToList.get(m).getUsername());
                            //Log.d("spinner list in DS", "" + assignToList.get(m).getUsername());
                        }
                        //getApplicationContext()


                        DateSelected.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                        getApplicationContext(), R.layout.spinner_style,
                                        assignToStringList);
                                adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
                                spinner.setAdapter(adapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                               int arg2, long arg3) {
                                        //User tempuser = (User)arg0.getItemAtPosition(arg2);

                                        String str = arg0.getItemAtPosition(arg2).toString();
                                        //Log.d("spinner list in DS", "" + str);
                                        //String str = arg0.getItemAtPosition(arg2).toString();
                                        Toast.makeText(getApplicationContext(), "Assign to "+str, Toast.LENGTH_SHORT).show();
                                        assignToId = assignToList.get(arg2).getUserId();
                                        //Log.d("assignToId in DS", "" + assignToId);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                        //// TODO Auto-generated method stub

                                    }
                                });
                            }
                        });

                        break;
                    case 2:
                        assignToList = (ArrayList<User>)msg.obj;
                        Log.d("assign2",assignToId+"");
                        DateSelected.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                        getApplicationContext(), R.layout.spinner_style,
                                        assignToStringList);
                                adapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
                                spinner.setAdapter(adapter);
                                for(int j = 0;j < assignToList.size();j++){
                                    if(assignToList.get(j).getUserId() == assignToId){
                                        spinner.setSelection(j);
                                    }
                                }
                                spinner.setEnabled(false);
                            }
                        });
                        break;
                }


            }
        };



        //get timestamp and string date
        deadline = event.getDeadLine();
        displayPickedTime(deadline);

        //save button clicked
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == INIT) {

                    event_save = new Event();
                    event_save.setAssignedBy(user.getUserId());
                    event_save.setAssignedTo(assignToId);
                    event_save.setDeadLine(deadline);
                    event_save.setEventTitle(eventName.getText().toString());
                    event_save.setDescription(eventDiscription.getText().toString());
                    //event_save.setGroupId(1);
                    event_save.setGroupId(group.getGroupId());

                    new Thread() {
                        @Override
                        public void run() {
                            assignresult = eventBiz.assignEventBiz(event_save);
                        }
                    }.start();
                    DateSelected.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (assignresult == "success") {
                                Toast.makeText(DateSelected.this, "Event saved", Toast.LENGTH_SHORT).show();
                                Intent saveintent = new Intent(getApplicationContext(), ProgressActivity.class);
                                startActivity(saveintent);
                                //returnToEventListView();
                            } else if (assignresult == null) {
                                Toast.makeText(DateSelected.this, "Failed to save event", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
                if (flag == EDIT) {
                    new Thread() {
                        @Override
                        public void run() {
                            assignresult = eventBiz.updateEvent(event.getEventID(), assignToId,eventName.getText().toString(),eventDiscription.getText().toString(),deadline);
                        }
                    }.start();
                    //Log.d(TAG, "assignresult: " + assignresult);
                    DateSelected.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (assignresult == "success") {
                                Toast.makeText(DateSelected.this, "Event updated", Toast.LENGTH_SHORT).show();
                                Intent editintent = new Intent(getApplicationContext(), ProgressActivity.class);
                                startActivity(editintent);

                            } else if (assignresult == null) {
                                Toast.makeText(DateSelected.this, "Failed to update event", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        sYear = year;
                        sMonth = monthOfYear+1;
                        sDay = dayOfMonth;
                        str_date = sYear+"-"+sMonth+"-"+sDay+" "+sHour+":"+sMinute+":"+sSecond;
                        Toast.makeText(context, year+" / "+(monthOfYear+1)+" / "+dayOfMonth, Toast.LENGTH_SHORT).show();
                        Date date = DateUtils.parseStrToDate(str_date,"yyyy-MM-dd hh:mm:ss");
                        displayPickedTime(date.getTime());
                        deadline = date.getTime();
                    }
                }, 2017, 10,16);
                datePicker.show();
            }
        });
        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog time = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sHour = hourOfDay;
                        sMinute = minute;
                        str_date = sYear+"-"+sMonth+"-"+sDay+" "+sHour+":"+sMinute+":"+sSecond;
                        Toast.makeText(context, hourOfDay +" : "+ minute, Toast.LENGTH_SHORT).show();
                        Date date = DateUtils.parseStrToDate(str_date,"yyyy-MM-dd hh:mm:ss");
                        displayPickedTime(date.getTime());
                        deadline = date.getTime();
                    }
                }, 13,15, true);
                time.show();
            }
        });

    }



    private void displayPickedTime(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // 格式化日期返回 String 类型，format 中传入 Date 类型或者其子类（例如Timestamp 类）
        String s = sdf.format(new Timestamp(time));
        if (sMinute < 10){
            textClock.setText("Task DeadLine :  " + sYear+"-"+sMonth+"-"+sDay+" "+sHour+":"+ "0"+sMinute+":"+"00");
        } else {
            textClock.setText("Task DeadLine :  " + sYear+"-"+sMonth+"-"+sDay+" "+sHour+":"+sMinute+":"+"00");
        }

    }

    void loadAssignToList(int flag){
        groupBiz = new GroupBiz();
        groupList = new ArrayList<Group>();
        userList = new ArrayList<User>();
        assignToList = new ArrayList<User>();
        assignToStringList = new ArrayList<String>();
        //Log.d("spinner list in DS", "" + "test spinner");
        groupList = groupBiz.loadGroups(user.getUserId());
        for (int i = 0; i < groupList.size(); i++) {
            //Log.d("spinner list in DS", "" + i);
            userList = groupBiz.loadUsersofSpecificGroup(groupList.get(i).getGroupId());
            for (int j = 0; j < userList.size(); j++) {
                //Log.d("spinner list in DS", "" + j);
                if (!assignToStringList.contains(userList.get(j).getUsername())){
                    assignToStringList.add(userList.get(j).getUsername());
                    assignToList.add(userList.get(j));
                }
            }
            userList.clear();
        }
        Message msg = Message.obtain();
        if(flag == INIT)
            msg.what = 1;
        else if(flag == EDIT)
            msg.what = 2;

        msg.obj = assignToList;
        handler.handleMessage(msg);
    }

}