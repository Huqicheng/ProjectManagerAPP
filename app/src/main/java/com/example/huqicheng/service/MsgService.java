package com.example.huqicheng.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.huqicheng.message.BaseMsg;

public class MsgService extends Service {
    public static OnChatMsgRecievedListener onChatMsgRecievedListener;

    public static void setOnChatMsgRecievedListener(OnChatMsgRecievedListener listener){
        onChatMsgRecievedListener = listener;
    }

    //static method for handler
    public static void onChatMsgRecieved(BaseMsg msg){
        if(onChatMsgRecievedListener == null){
            return;
        }
        onChatMsgRecievedListener.onChatMsgRecieved(msg);
    }

    public class MsgBinder extends Binder {
        public MsgService getService(){
            return MsgService.this;
        }
    }

    public MsgService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MsgBinder();
    }
}
