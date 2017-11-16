package com.example.huqicheng.pm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huqicheng.bll.EventBiz;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.entity.Event;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.utils.DateUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateSelected extends AppCompatActivity {
    Intent intent;
    private EditText eventName,eventDiscription;
    private Button save;
    private Button time_picker, date_picker;
    private TextView textClock,textDate;
    static final String TAG="TAG";
    public String assignresult = "";
    public int hour_x,minute_x,month_x;
    public EventBiz eventBiz = new EventBiz();
    public Context context = this;
    private User user;
    public Event event;
    public Event event_save;
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

        //creating the instance of the calander
        final Calendar calendar = Calendar.getInstance();
        month_x = calendar.get(Calendar.MONTH)+1;
        textClock.setText(calendar.get(Calendar.HOUR) + " : " + calendar.get(Calendar.MINUTE));
        textDate.setText(calendar.get(Calendar.YEAR)+" / "+month_x+" / "+calendar.get(Calendar.DATE));

        //load user
        user = new UserBiz(this).readUser();
        //getting intent
        Intent eventintent = this.getIntent();
        //getting event
        event = (Event)eventintent.getSerializableExtra("event");
        Log.d("events in DS",""+event.getDescription());
        //get flag that decide whether it is an edit event of init event
        flag = (int)eventintent.getSerializableExtra("flag");
        if (flag == EDIT) {
            Date date = new Date(event.getDeadLine());
            Timestamp timestamp = new Timestamp(event.getDeadLine());
            CalendarDay calendarDay = CalendarDay.from(date);
            sYear = calendarDay.getYear();
            sMonth = calendarDay.getMonth()+1;
            sDay = calendarDay.getDay();
            sHour = date.getHours();
            sMinute = date.getMinutes();
            Log.d("events in DS",""+sYear+sMonth+sDay+sHour+sMinute);
            Log.d("events in DS",""+date.toString());
            eventName.setText(event.getEventTitle());
            eventDiscription.setText(event.getDescription());
            save.setText("update");
        }
        if (flag == INIT) {
            save.setText("save");
        }




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
                    event_save.setAssignedTo(user.getUserId());
                    event_save.setDeadLine(CalendarDay.from(2017, 11, 20).getDate().getTime());
                    event_save.setEventTitle(eventName.getText().toString());
                    event_save.setDescription(eventDiscription.getText().toString());
                    event_save.setGroupId(1);

                    new Thread() {
                        @Override
                        public void run() {
                            assignresult = eventBiz.assignEventBiz(event_save);
                        }
                    }.start();
                    Log.e(TAG, "assignresult: " + assignresult);
                    if (assignresult == "success") {
                        Toast.makeText(DateSelected.this, "Event saved", Toast.LENGTH_LONG).show();
                    } else if (assignresult == null) {
                        Toast.makeText(DateSelected.this, "Failed to save event", Toast.LENGTH_LONG).show();
                    }
                }
                if (flag == EDIT) {
                    new Thread() {
                        @Override
                        public void run() {
                            assignresult = eventBiz.updateEvent(event.getEventID(), user.getUserId(),eventName.getText().toString(),eventDiscription.getText().toString(),deadline);
                        }
                    }.start();
                    Log.e(TAG, "assignresult: " + assignresult);
                    if (assignresult == "success") {
                        Toast.makeText(DateSelected.this, "Event updated", Toast.LENGTH_LONG).show();
                    } else if (assignresult == null) {
                        Toast.makeText(DateSelected.this, "Failed to update event", Toast.LENGTH_LONG).show();
                    }
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
        textClock.setText("Event DeadLine :  " + s);
    }
}