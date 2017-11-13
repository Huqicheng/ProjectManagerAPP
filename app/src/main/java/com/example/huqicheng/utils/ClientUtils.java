package com.example.huqicheng.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.huqicheng.message.*;

import com.example.huqicheng.client.NettyClientBootstrap;
import com.example.huqicheng.pm.CalendarActivity;
import com.example.huqicheng.pm.CalendarFragment;
import com.example.huqicheng.service.OnChatMsgRecievedListener;
import com.google.gson.Gson;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ClientUtils {
	private static NettyClientBootstrap client = null;
	private static OnChatMsgRecievedListener listenerForWeChat;
	private static Context context;

	public static void setContext(Context ctx){
		context = ctx;
	}

	public static void setListenerForWeChat(OnChatMsgRecievedListener listener){
		listenerForWeChat = listener;
	}

	//static method for handler
	public static boolean onChatMsgRecievedForWeChat(BaseMsg msg){
		if(listenerForWeChat == null){
			return false;
		}
		if(listenerForWeChat.getId(1)!=Long.parseLong(msg.getGroupId() )){
			return false;
		}
		listenerForWeChat.onChatMsgRecieved(msg);

		return true;
	}
	
	public static NettyClientBootstrap getInstance(String client_id){
		if(client == null){
			startClient(client_id);
		}
		
		return client;
	}

	private static void startClient(String client_id) {
		Constants.setClientId(client_id);


        try {
			client=new NettyClientBootstrap(8000,"192.168.137.1",client_id);
			client.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void notification(BaseMsg msg){
		Log.d("msg recieved:","notification");

		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				//设置通知标题
				.setContentTitle("最简单的Notification")
				//设置通知内容
				.setContentText("只有小图标、标题、内容");

		notifyManager.notify(1, builder.build());
	}

	public static void send(BaseMsg msg){
		if(client == null || client.socketChannel == null){
			return;
		}
		client.socketChannel.writeAndFlush(new Gson().toJson(msg));
	}
}
