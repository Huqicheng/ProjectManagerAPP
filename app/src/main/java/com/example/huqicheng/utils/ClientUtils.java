package com.example.huqicheng.utils;

import com.example.huqicheng.message.*;

import com.example.huqicheng.client.NettyClientBootstrap;

public class ClientUtils {
	private static NettyClientBootstrap client = null;
	
	public static NettyClientBootstrap getInstance(){
		if(client == null){
			startClient();
		}
		
		return client;
	}

	private static void startClient() {
		// TODO Auto-generated method stub
		Constants.setClientId("002");
        try {
			client=new NettyClientBootstrap(8080,"192.168.11.9");
			client.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void send(String groupId, BaseMsg msg){
		
	}
}
