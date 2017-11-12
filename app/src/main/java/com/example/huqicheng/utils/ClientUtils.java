package com.example.huqicheng.utils;

import com.example.huqicheng.message.*;

import com.example.huqicheng.client.NettyClientBootstrap;
import com.example.huqicheng.service.OnChatMsgRecievedListener;
import com.google.gson.Gson;

public class ClientUtils {
	private static NettyClientBootstrap client = null;
	private static OnChatMsgRecievedListener listenerForWeChat;

	public static void setListenerForWeChat(OnChatMsgRecievedListener listener){
		listenerForWeChat = listener;
	}

	//static method for handler
	public static void onChatMsgRecievedForWeChat(BaseMsg msg){
		if(listenerForWeChat == null){
			return;
		}
		listenerForWeChat.onChatMsgRecieved(msg);
	}
	
	public static NettyClientBootstrap getInstance(String client_id){
		if(client == null){
			startClient(client_id);
		}
		
		return client;
	}

	private static void startClient(String client_id) {
		// TODO use the real Client Id of this client
		Constants.setClientId(client_id);


        try {
			client=new NettyClientBootstrap(8000,"192.168.137.1",client_id);
			client.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void send(BaseMsg msg){
		if(client == null || client.socketChannel == null){
			return;
		}
		client.socketChannel.writeAndFlush(new Gson().toJson(msg));
	}
}
