package com.example.huqicheng.pm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.config.Config;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.nao.UserNao;
import com.example.huqicheng.service.CalendarNotificationService;
import com.example.huqicheng.utils.PersistentCookieStore;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;


public class LoginActivity extends AppCompatActivity {
    private TextView tvRegister;
    private TextView tvRestore;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private CheckBox checkboxRemember;
    private Button btnFacebook;
    private Button btnGoogle;
    private Intent intent = null;
    private Handler handler = null;

    //PersistentCookieStore store = PersistentCookieStore.getInstance(Config.SERVER_IP);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //doAutomaticLogin();

        setContentView(R.layout.activity_login);
        initViews();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        //TODO show a dialog to notify that login is rejected by the server
                        break;
                    case 1:
                        /** start notification service after user login**/
                        /*
                        intent = new Intent(getApplication(), CalendarNotificationService.class);
                        startService(intent);
                        */


                        intent = new Intent(LoginActivity.this, CalendarActivity.class);

                        startActivity(intent);

                        finish();
                        break;

                }
            }
        };

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLogin();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRegister();
            }
        });

        tvRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRestore();
            }
        });

    }

    private void doAutomaticLogin(){


    }



    private void onClickRestore(){
        intent = new Intent(this, RestorePasswordActivity.class);

        startActivity(intent);


    }

    private void onClickRegister(){
        intent = new Intent(this, RegisterActivity.class);

        startActivity(intent);


    }


    private void onClickLogin(){
        new Thread(){
            @Override
            public void run() {
                //TODO before login, check the login type is username or email and check whether input is legal or not

                UserBiz userBiz = new UserBiz(LoginActivity.this);
                User user = new User();
                user.setPassword(etPassword.getText().toString());
                user.setUsername(etEmail.getText().toString());
                User res = userBiz.doLogin(user,"username");

                if(res != null){
                    //writeUserInfoToFile();
                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.handleMessage(msg);

                }else{
                    Message msg = Message.obtain();
                    msg.what = 0;
                    handler.handleMessage(msg);
                }

            }
        }.start();






    }

    private void initViews() {
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvRestore = (TextView) findViewById(R.id.tvRestore);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        checkboxRemember = (CheckBox) findViewById(R.id.checkboxRemember);
        btnFacebook = (Button) findViewById(R.id.btnFacebook);
        btnGoogle = (Button) findViewById(R.id.btnGoogle);

        Intent intent = this.getIntent();
        etEmail.setText(intent.getStringExtra("username"));
        etPassword.setText(intent.getStringExtra("pwd"));

    }


}