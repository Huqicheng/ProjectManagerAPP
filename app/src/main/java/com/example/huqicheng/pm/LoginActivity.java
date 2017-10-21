package com.example.huqicheng.pm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huqicheng.entity.User;
import com.example.huqicheng.utils.FileUtils;
import com.google.gson.Gson;

import java.io.IOException;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        doAutomaticLogin();

        setContentView(R.layout.activity_login);
        initViews();

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
        try {
            if(FileUtils.readFromFile("cookie").equals("")){
                Log.d("debug: ","no items in cookie");
                return;
            }else{
                onClickLogin();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeUserInfoToFile(){
        User user = new User();
        user.setUsername("q45hu");
        user.setPassword("12345678");
        try {
            FileUtils.writeToFile("cookie",new Gson().toJson(user),this);
        } catch (IOException e) {
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
        writeUserInfoToFile();


        intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        this.finish();
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