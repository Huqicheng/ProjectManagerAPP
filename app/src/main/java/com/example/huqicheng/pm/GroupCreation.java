package com.example.huqicheng.pm;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.huqicheng.bll.GroupBiz;
import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.entity.Group;
import com.example.huqicheng.entity.User;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GroupCreation extends AppCompatActivity {
    Intent intent;
    private EditText groupName,groupDescription,projectName;
    private Button save;

    static final String TAG="GroupCreation";
    public String assignresult = "";
    public int hour_x,minute_x;
    public GroupBiz groupBiz = new GroupBiz();
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
        setContentView(R.layout.activity_group_creation);

        //Initializing the EditTexts
        groupName = (EditText) findViewById(R.id.GroupName);
        groupDescription = (EditText) findViewById(R.id.GroupDescription);
        projectName = (EditText) findViewById(R.id.ProjectName);

        //Initializing the buttons
        save = (Button) findViewById(R.id.btnSave);





        //getting date from mainactivity

        //load user



        this.context=this;

        //save button clicked
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }


}