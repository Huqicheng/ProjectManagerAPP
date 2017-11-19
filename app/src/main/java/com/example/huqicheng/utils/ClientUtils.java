package com.example.huqicheng.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.huqicheng.config.Config;
import com.example.huqicheng.message.*;

import com.example.huqicheng.client.NettyClientBootstrap;
import com.example.huqicheng.pm.CalendarActivity;
import com.example.huqicheng.pm.CalendarFragment;
import com.example.huqicheng.pm.R;
import com.example.huqicheng.service.OnChatMsgRecievedListener;
import com.google.gson.Gson;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ClientUtils {
	private static NettyClientBootstrap client = null;
	private static OnChatMsgRecievedListener listenerForWeChat;
	private static OnChatMsgRecievedListener listenerForGroupList;
	private static Context context;
	private static boolean isConnected = false;

	public static void setIsConnected(boolean isConnect){
		isConnected = isConnect;
	}
	public static void setContext(Context ctx){
		context = ctx;
	}

	public static void setListenerForGroupList(OnChatMsgRecievedListener listener){
		listenerForGroupList = listener;
	}

	public static void setListenerForWeChat(OnChatMsgRecievedListener listener){
		listenerForWeChat = listener;
	}

	//static method for handler
	public static boolean onChatMsgRecievedForWeChat(BaseMsg msg){
		if(listenerForWeChat == null){
			return false;
		}
		if(listenerForWeChat.getId()!=Long.parseLong(msg.getGroupId() )){
			return false;
		}
		listenerForWeChat.onChatMsgRecieved(msg);

		return true;
	}

	public static boolean onChatMsgRecievedForGroupList(BaseMsg msg){
		if(listenerForGroupList == null){
			return false;
		}
		listenerForGroupList.onChatMsgRecieved(msg);

		return true;
	}
	
	public static NettyClientBootstrap getInstance(String client_id){
		if(client == null){
			startClient(client_id);
		}
		
		return client;
	}

	private static void startClient(final String client_id) {
		Constants.setClientId(client_id);


        try {
			// start connection asynchronously
			new Thread(){
				@Override
				public void run() {
					client=new NettyClientBootstrap(Config.SOCKET_SERVER_PORT,Config.SOCKET_SERVER_IP,client_id);
					try {
						client.start();
						setIsConnected(true);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void notification(BaseMsg msg){
		Log.d("msg recieved:","notification");

		NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle((String)msg.getParams().get("body"))
				.setContentText((String)msg.getParams().get("username"));

		notifyManager.notify(1, builder.build());
	}

	public static boolean send(BaseMsg msg){
		if(client == null || client.socketChannel == null || isConnected == false){
			Log.d("ClientUtils" , "You are offline!");
			return false;
		}
		client.socketChannel.writeAndFlush(new Gson().toJson(msg));
		return true;
	}
}
