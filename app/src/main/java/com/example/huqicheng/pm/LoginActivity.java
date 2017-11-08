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

import com.example.huqicheng.config.Config;
import com.example.huqicheng.entity.User;
import com.example.huqicheng.nao.UserNao;
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

    PersistentCookieStore store = PersistentCookieStore.getInstance(Config.SERVER_IP);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(store).start();

        //doAutomaticLogin();

        setContentView(R.layout.activity_login);
        initViews();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        intent = new Intent(LoginActivity.this, CalendarActivity.class);

                        startActivity(intent);

                        finish();

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

    private void writeUserInfoToFile(){
        try {
            store.add(new URI(Config.SERVER_IP),new HttpCookie("username","q45hu"));
            store.add(new URI(Config.SERVER_IP),new HttpCookie("password","12345678"));
            new Thread(store).start();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


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
                UserNao un = new UserNao();
                User user = new User();
                user.setPassword("12345678");
                user.setUsername("q45hu");
                User res = un.doLogin();

                if(res != null){
                    writeUserInfoToFile();
                    Message msg = Message.obtain();
                    msg.what = 1;
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