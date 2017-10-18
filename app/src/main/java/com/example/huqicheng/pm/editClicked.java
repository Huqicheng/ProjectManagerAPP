package com.example.huqicheng.pm;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.huqicheng.dao.dbHandler;
import com.example.huqicheng.dao.dbHandler2;

import java.util.Calendar;

public class editClicked extends AppCompatActivity {

    private Intent intent;
    private EditText eventName, eventLocation, eventDiscription, Attendees;
    private Button update, add ,setTime,UpdateTime;
    private TextView textClock,textDate;
    private int Date,day,month,year,hour_x,minute_x;
    private AlarmManager alarmManager;
    private dbHandler db;
    private dbHandler2 db2;
    private Context context;
    private Intent INTENT;
    private int uniqueID2=69;
    private static int incrementer=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clicked);

        //Initializing the EditTexts
        eventName = (EditText) findViewById(R.id.editText6);
        eventLocation = (EditText) findViewById(R.id.editText7);
        eventDiscription = (EditText) findViewById(R.id.editText8);
        Attendees = (EditText) findViewById(R.id.editText9);

        //initializing the button
        update = (Button) findViewById(R.id.button3);
        add = (Button) findViewById(R.id.button4);
        UpdateTime=(Button) findViewById(R.id.button9);
        setTime=(Button) findViewById(R.id.button10);

        //Initializing the TextViews
        textDate=(TextView)findViewById(R.id.textView3);
        textClock = (TextView) findViewById(R.id.textView10);

        //Instance of a calander
        final Calendar calendar = Calendar.getInstance();
        hour_x = calendar.get(Calendar.HOUR);
        minute_x = calendar.get(Calendar.MINUTE);
        textClock.setText(hour_x + " : " + minute_x);

        //callin constructor from the two databases
        db = new dbHandler(this);
        db2 = new dbHandler2(this);

        // receiving the data from the CalendarFragment
        Bundle dateRecieved = getIntent().getExtras();
        Date = dateRecieved.getInt("date message");
        day = dateRecieved.getInt("day message");
        month = dateRecieved.getInt("month message");
        year = dateRecieved.getInt("year message");
        int currMonth=month+1;
        textDate.setText(currMonth+" / "+day+" / "+year);

        alarmManager= (AlarmManager)getSystemService(ALARM_SERVICE);
        this.context=this;

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking of data was updated or not.
                boolean result = db.updateData(Date, eventName.getText().toString(), eventLocation.getText().toString(), eventDiscription.getText().toString());
                if (result == false)
                    Toast.makeText(editClicked.this, "Failed to Update Data", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(editClicked.this, "Data Update", Toast.LENGTH_LONG).show();
                intent = new Intent(editClicked.this, MainActivity.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checing if Attendee was updated or not.
                String d= Date+""+incrementer;
                incrementer++;
                int changeDateId=Integer.parseInt(d);
                boolean result = db2.updateAttendee(changeDateId, Attendees.getText().toString());
                if (result == false)
                    Toast.makeText(editClicked.this, "Failed to Update Attendee", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(editClicked.this, "Attendee Update", Toast.LENGTH_LONG).show();

                Attendees.setText("");
            }
        });

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //giving new time to an instance of Calender.
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, day);
                cal.set(Calendar.HOUR_OF_DAY, hour_x);
                cal.set(Calendar.MINUTE, minute_x);
                cal.set(Calendar.SECOND, 0);

                //return calender time in milliseconds
                long mills = cal.getTimeInMillis();

                INTENT = new Intent(editClicked.this,AlarmReceiver.class);
                String event=eventName.getText().toString();
                String Discri=eventDiscription.getText().toString();

                //sending the data to Alarm Manager class
                INTENT.putExtra("Event name message",event);
                INTENT.putExtra("Event Discription message",Discri);

                //Panding Intent to tell the Alarm Receiver to wait till the time in Millis.
                PendingIntent pendingIntent = PendingIntent.getBroadcast(editClicked.this, 0, INTENT, PendingIntent.FLAG_UPDATE_CURRENT);
                //calling Alarm Manager
                alarmManager.set(AlarmManager.RTC_WAKEUP, mills, pendingIntent);
            }
        });

        //Updateing the time for notifications
        UpdateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(uniqueID2);
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        if (id == uniqueID2)
            return new TimePickerDialog(editClicked.this, kTimePickerListener, hour_x, minute_x, false);
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
