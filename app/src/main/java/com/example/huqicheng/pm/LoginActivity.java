package com.example.huqicheng.pm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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
import com.example.huqicheng.service.CalendarNotificationService;
import com.example.huqicheng.utils.PersistentCookieStore;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
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
    private UserBiz userBiz;
    User currentUser;
    private static final String TAG="LoginActivity";
    //PersistentCookieStore store = PersistentCookieStore.getInstance(Config.SERVER_IP);

    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE=9001;
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
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        });
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
                    case 2:
                        dologin(currentUser);
                    case 3:
                        Log.d(TAG,msg.toString());
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

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doAutomaticLogin();
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
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

    private void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }
    private void handleResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account=result.getSignInAccount();
            String name=account.getDisplayName();
            String email=account.getEmail();
            String password="123";

            userBiz = new UserBiz(this);

            currentUser = userBiz.registerUser(email,name,password,"normal");
            Message msg = Message.obtain();
            msg.what = 2;
            msg.obj = currentUser;
            handler.handleMessage(msg);


        }
    }
    private void dologin(User user){
        userBiz = new UserBiz(this);
        User res = userBiz.doLogin(user,"username");
        Message msg = Message.obtain();
        msg.what = 3;
        msg.obj = res;
        handler.handleMessage(msg);

    }
    private void doAutomaticLogin(){
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE){
            final GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
           // handleResult(result);
            new Thread() {
                @Override
                public void run() {
                    handleResult(result);
                }
            }.start();
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
        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}