package com.example.huqicheng.pm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.huqicheng.bll.UserBiz;
import com.example.huqicheng.entity.User;


public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail;
    private EditText etName;
    private EditText etPassword;
    private Button btnRegister;
    private UserBiz userBiz;
    private Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                final User currentUser = (User)msg.obj;

                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String username = currentUser.getUsername();
                        Toast.makeText(getApplicationContext(), username + " registered successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("username",username);
                        intent.putExtra("pwd",currentUser.getPassword());
                        startActivity(intent);
                    }
                });
            }
        };
    }

    private void initUI() {
        etEmail = (EditText) findViewById(R.id.etEmail);
        etName = (EditText) findViewById(R.id.etName);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterButtonClicked();
            }
        });
    }

    private void onRegisterButtonClicked() {
        if (etEmail.getText().toString().equals("")) {
            etEmail.setError(getString(R.string.error_email));
            return;
        }
        if (etName.getText().toString().equals("")) {
            etName.setError(getString(R.string.error_name));
            return;
        }
        if (etPassword.getText().toString().equals("")) {
            etPassword.setError(getString(R.string.error_pass));
            return;
        }
        userBiz = new UserBiz(this);
        new Thread() {
            @Override
            public void run() {
                doRegister();
            }
        }.start();
    }

    public void doRegister() {
        User currentUser;
        currentUser = userBiz.registerUser(etEmail.getText().toString(),etName.getText().toString(),etPassword.getText().toString(),"normal");
        Message msg = Message.obtain();
        msg.what = 1;
        msg.obj = currentUser;
        handler.handleMessage(msg);
    }
}