package com.example.huqicheng.pm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.example.huqicheng.utils.FileUtils;
import com.example.huqicheng.utils.PersistentCookieStore;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;


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
                        //Toast.makeText(getApplicationContext(), "login failed" ,Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        intent = new Intent(LoginActivity.this, MainActivity.class);

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
                UserBiz userBiz = new UserBiz(LoginActivity.this);
                User user = new User();
                user.setPassword("123");
                user.setUsername("q45hu");
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



    }


}