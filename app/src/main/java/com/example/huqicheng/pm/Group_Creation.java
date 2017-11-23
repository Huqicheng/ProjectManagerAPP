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

public class Group_Creation extends AppCompatActivity {
    Intent intent;
    private Handler handler = null;

    private UserBiz userBiz;
    private List<Group> groupList;
    private List<User> userList;
    public  List<String> assignToList;
    private Map<String,Long> userNameIdMap;
    private EditText projectName,projectDescription;
    private Button save;
    private Button time_picker, date_picker;
    private TextView textClock,textDate, textAssignto;
    private Spinner spinner;
    static final String TAG="TAG";
    public String assignresult = "";
    public int month_x;
    public GroupBiz groupBiz = new GroupBiz();
    public Context context = this;
    private User user;
    public Group group_local;
    public Group group_save;
    private Long deadline;
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
        setContentView(R.layout.activity_group_creation);

        //Initializing the EditTexts
        projectName = (EditText) findViewById(R.id.etProjectName);
        projectDescription = (EditText) findViewById(R.id.etProjectDiscription);

        //Initializing the buttons
        save = (Button) findViewById(R.id.btnSave);
        time_picker = (Button) findViewById(R.id.btnTimepicker);
        date_picker=(Button) findViewById(R.id.btnDatepicker);

        //Initializing the TextViews of the Activity
        textClock = (TextView) findViewById(R.id.tvDisplaytime);
        textDate = (TextView) findViewById(R.id.tvCurrentdate);


        //creating the instance of the calander
        final Calendar calendar = Calendar.getInstance();
        month_x = calendar.get(Calendar.MONTH)+1;
        textClock.setText(calendar.get(Calendar.HOUR) + " : " + calendar.get(Calendar.MINUTE));
        textDate.setText(calendar.get(Calendar.YEAR)+" / "+month_x+" / "+calendar.get(Calendar.DATE));

        //load user
        userBiz = new UserBiz(this);
        user = userBiz.readUser();





        //getting intent


        //get flag that decide whether it is an edit event of init event


        save.setText("Create");


        //save button clicked
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    group_save= new Group();

                    group_save.setDeadline(CalendarDay.from(2017, 11, 20).getDate().getTime());
                    group_save.setGroupName(projectName.getText().toString());
                    group_save.setGroupDescription(projectDescription.getText().toString());
                    group_save.setGroupId(1);

                    new Thread() {
                        @Override
                        public void run() {
                            assignresult = groupBiz.createGroup(group_save.getGroupName(),group_save.getGroupDescription(),group_save.getDeadline(),user.getUserId());
                            Log.e(TAG, "assignresult: " + assignresult);
                            if (assignresult == "success") {
                               // Toast.makeText(Group_Creation.this, "Event saved", Toast.LENGTH_LONG).show();
                                finish();
                                Intent intent=new Intent(Group_Creation.this,ChatActivity.class);
                                startActivity(intent);
                            } else  {
                                //Toast.makeText(Group_Creation.this, "Failed to save event", Toast.LENGTH_LONG).show();
                                finish();
                                Intent intent=new Intent(Group_Creation.this,ChatActivity.class);
                                startActivity(intent);
                            }
                        }
                    }.start();

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
        textClock.setText("Event DeadLine :  " + s);
    }


}