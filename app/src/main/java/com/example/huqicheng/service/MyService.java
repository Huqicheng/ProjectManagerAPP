package com.example.huqicheng.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.huqicheng.client.NettyClientBootstrap;
import com.example.huqicheng.message.BaseMsg;
import com.example.huqicheng.message.MsgType;
import com.example.huqicheng.utils.ClientUtils;
import com.google.gson.Gson;

public class MyService extends Service {
    private static NettyClientBootstrap client = null;

    //Listener only for WeChatActivity
    public static OnChatMsgRecievedListener onChatMsgRecievedListener;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MsgBinder();

    }

    public class MsgBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }
    }

    //set listener for UI updating
    public void setOnChatMsgRecievedListener(OnChatMsgRecievedListener listener){
        onChatMsgRecievedListener = listener;
    }

    //static method for handler
    public static void onChatMsgRecieved(BaseMsg msg){
        if(onChatMsgRecievedListener == null){
            return;
        }
        onChatMsgRecievedListener.onChatMsgRecieved(msg);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                do {
//                    System.out.println("Service is running......");
//                    try {
//                        sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                } while (true);
//            }
//        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("debug:","service");
        client = ClientUtils.getInstance();

        Log.d("debug:","start");

        BaseMsg loginMsg=new BaseMsg();
        loginMsg.setType(MsgType.LOGIN);
        loginMsg.putParams("user", "huqicheng");
        loginMsg.putParams("pwd", "huqicheng");
        if(client.socketChannel != null){
            client.socketChannel.writeAndFlush(new Gson().toJson(loginMsg));
        }
    }
}
