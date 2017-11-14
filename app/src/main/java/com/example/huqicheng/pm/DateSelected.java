package com.example.huqicheng.pm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.huqicheng.bll.EventBiz;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.User;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateSelected extends AppCompatActivity {
    Intent intent;
    private EditText eventName,eventDiscription;
    private Button save;
    private Button time_picker, set_deadline;
    private TextView textClock,textDate;
    static final String TAG="TAG";
    public String assignresult = "";
    public int hour_x,minute_x;
    public EventBiz eventBiz = new EventBiz();
    private Intent INTENT;
    public Context context;
    private User user;
    private int sYear;
    private int sonth;
    private int sDay;
    private int sHour;
    private int sMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selected);

        //Initializing the EditTexts
        eventName = (EditText) findViewById(R.id.etEventname);
        eventDiscription = (EditText) findViewById(R.id.etEventDiscription);
        //Attendees = (EditText) findViewById(R.id.editText5);

        //Initializing the buttons
        save = (Button) findViewById(R.id.btnSave);
        time_picker = (Button) findViewById(R.id.btnTimepicker);
        set_deadline=(Button) findViewById(R.id.btnSettime);

        //Initializing the TextViews of the Activity
        textClock = (TextView) findViewById(R.id.tvDispaytime);
        textDate = (TextView) findViewById(R.id.tvCurrentdate);

        //creating the instance of the calander
        final Calendar calendar = Calendar.getInstance();
        hour_x = calendar.get(Calendar.HOUR);
        minute_x = calendar.get(Calendar.MINUTE);
        textClock.setText(hour_x + " : " + minute_x);

        //getting date from mainactivity
        Intent eventintent = this.getIntent();
        final Event event = (Event)eventintent.getSerializableExtra("event");
        Log.d("events in DS",""+event.getDescription());
        if (event.getEventStatus().equals("started")) {
            eventName.setText(event.getEventTitle());
            eventDiscription.setText(event.getDescription());
        }
        //load user
        user = new UserBiz(this).readUser();
        //get timestamp and string date
        Timestamp datetimestamp = new Timestamp(event.getCreatedAt());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 格式化日期返回 String 类型，format 中传入 Date 类型或者其子类（例如Timestamp 类）
        String s = sdf.format(datetimestamp);

        textDate.setText(s);
        this.context=this;

        //save button clicked
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Event event_save = new Event();
                event_save.setAssignedBy(user.getUserId());
                event_save.setAssignedTo(user.getUserId());
                event_save.setDeadLine(CalendarDay.from(2017,11,20).getDate().getTime());
                event_save.setEventTitle(eventName.getText().toString());
                event_save.setDescription(eventDiscription.getText().toString());
                event_save.setGroupId(1);

                new Thread(){
                    @Override
                    public void run() {
                        assignresult = eventBiz.assignEventBiz(event_save);
                    }
                }.start();
                Log.e(TAG, "assignresult: " + assignresult);
                if (assignresult == "success"){
                    Toast.makeText(DateSelected.this, "Event inserted", Toast.LENGTH_LONG).show();
                }else if(assignresult == null) {
                    Toast.makeText(DateSelected.this, "Failed to insert event", Toast.LENGTH_LONG).show();
                }

            }
        });

        set_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //setting time for calendar instance
                Calendar cal = Calendar.getInstance();
                //cal.set(Calendar.YEAR, year);
                //cal.set(Calendar.MONTH, month);
                //cal.set(Calendar.DAY_OF_MONTH, day);
                cal.set(Calendar.HOUR_OF_DAY, hour_x);
                cal.set(Calendar.MINUTE, minute_x);
                cal.set(Calendar.SECOND, 0);
                long mills = cal.getTimeInMillis();
            }
        });

        //setting up On click listener to open a dialog box.
        time_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(0);
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        if (id == 0)
            return new TimePickerDialog(DateSelected.this, kTimePickerListener, hour_x, minute_x, false);
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener kTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    hour_x = i;
                    minute_x = i1;
                    textClock.setText(hour_x +" : "+minute_x);
                }
            };
}