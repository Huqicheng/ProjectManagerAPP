package com.example.huqicheng.utils;

import com.example.huqicheng.message.*;

import com.example.huqicheng.client.NettyClientBootstrap;
import com.google.gson.Gson;

public class ClientUtils {
	private static NettyClientBootstrap client = null;
	
	public static NettyClientBootstrap getInstance(){
		if(client == null){
			startClient();
		}
		
		return client;
	}

	private static void startClient() {
		// TODO use the real Client Id of this client
		Constants.setClientId("1");


        try {
			client=new NettyClientBootstrap(8080,"192.168.137.1");
			client.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void send(BaseMsg msg){
		client.socketChannel.writeAndFlush(new Gson().toJson(msg));
	}
}
