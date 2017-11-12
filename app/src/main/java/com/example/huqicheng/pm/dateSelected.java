package com.example.huqicheng.pm;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
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

import com.example.huqicheng.entity.Event;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateSelected extends AppCompatActivity {
    Intent intent;
    private EditText eventName,eventLocation,eventDiscription,Attendees;
    private Button save,add,setTime;
    private Button startTime;
    private TextView textClock,textDate;
    static final String TAG="TAG";
    private static final int uniqueID2=0;
    int hour_x,minute_x;
    private AlarmManager alarmManager;
    private Intent INTENT;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_date_selected);

        //Initializing the EditTexts
        eventName = (EditText) findViewById(R.id.etEventname);
        eventLocation = (EditText) findViewById(R.id.etEventlocation);
        eventDiscription = (EditText) findViewById(R.id.etEventDiscription);
        //Attendees = (EditText) findViewById(R.id.editText5);

        //Initializing the buttons
        save = (Button) findViewById(R.id.btnSave);
        //add = (Button) findViewById(R.id.button2);
        startTime = (Button) findViewById(R.id.btnTimepicker);
        setTime=(Button) findViewById(R.id.btnSettime);


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
        Event event = (Event)eventintent.getSerializableExtra("event");
        if (event.getEventStatus().equals("started")) {
            eventName.setText(event.getEventTitle());
            eventLocation.setText(event.getEventLocation());
            eventDiscription.setText(event.getEventDescription());
        }

        //get timestamp and string date
        Timestamp datetimestamp = new Timestamp(event.getCreatedAt());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 格式化日期返回 String 类型，format 中传入 Date 类型或者其子类（例如Timestamp 类）
        String s = sdf.format(datetimestamp);

        textDate.setText(s);
        //alarmManager= (AlarmManager)getSystemService(ALARM_SERVICE);
        this.context=this;

        //save button clicked
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                boolean result = db.insertData(date11, eventName.getText().toString(), eventLocation.getText().toString(), eventDiscription.getText().toString());
                //checking if data was inserted or not.
                if (result == false)
                    Toast.makeText(dateSelected.this, "Failed to Insert Data", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(dateSelected.this, "Data Inserted", Toast.LENGTH_LONG).show();
                    intent = new Intent(dateSelected.this, MainActivity.class);

                    startActivity(intent);
                }
                */
            }
        });

        setTime.setOnClickListener(new View.OnClickListener() {
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

                INTENT = new Intent(DateSelected.this,AlarmReceiver.class);


                String event=eventName.getText().toString();
                String Discri=eventDiscription.getText().toString();

                //sendind data to Alarm Manager class
                INTENT.putExtra("Event name message",event);
                INTENT.putExtra("Event Discription message",Discri);

                //Pending Intent to tell Alarm Manager to wait till the time in millis
                PendingIntent pendingIntent = PendingIntent.getBroadcast(DateSelected.this, 0, INTENT, PendingIntent.FLAG_UPDATE_CURRENT);
                //calling the onReceive in the Alarm Manager.
                alarmManager.set(AlarmManager.RTC_WAKEUP, mills, pendingIntent);
            }
        });

        //add button clicked
        /*
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String d= date11+""+incrementer;
                incrementer++;
                int changeDateId=Integer.parseInt(d);
                boolean result = db2.insertAttendees(changeDateId, Attendees.getText().toString());
                //checking if Attendees were inserted or not.
                if (result == false)
                    Toast.makeText(dateSelected.this, "Failed to add Attendee", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(dateSelected.this, "Attendee Inserted", Toast.LENGTH_LONG).show();

                Attendees.setText("");
            }
        });
        */
        //setting up On click listener to open a dialog box.
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(uniqueID2);
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        if (id == uniqueID2)
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
