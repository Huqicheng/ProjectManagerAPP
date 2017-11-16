package com.example.huqicheng.client;

import android.util.Log;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import com.example.huqicheng.dao.UserDao;
import com.example.huqicheng.entity.Event;
import com.example.huqicheng.message.BaseMsg;
import com.example.huqicheng.message.MsgCache;
import com.example.huqicheng.message.MsgType;
import com.example.huqicheng.utils.ClientUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
    String client_id;
    public NettyClientHandler(String id){
        client_id = id;
    }
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);

        //reconnect to server
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
            	NettyClientBootstrap client = ClientUtils.getInstance(client_id);
            	try {
					client.start();

                    if(client == null || client.socketChannel == null){
                        return;
                    }

                    BaseMsg loginMsg=new BaseMsg();
                    loginMsg.setType(MsgType.LOGIN);
                    loginMsg.putParams("user", "huqicheng");
                    loginMsg.putParams("pwd", "huqicheng");
                    if(client.socketChannel != null){
                        client.socketChannel.writeAndFlush(new Gson().toJson(loginMsg));
                    }
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }, 2, TimeUnit.SECONDS);
        ctx.close();
    }
	
	
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    BaseMsg pingMsg=new BaseMsg();
                    pingMsg.setType(MsgType.PING);
                    ctx.writeAndFlush(new Gson().toJson(pingMsg));

                    System.out.println("send ping to server----------");
                    break;
                default:
                    break;
            }
        }
    }
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
    	System.out.println(msg);
        BaseMsg baseMsg = new Gson().fromJson(msg, BaseMsg.class);
        //ClientUtils.notification(baseMsg);

    	MsgType msgType=baseMsg.getType();
        switch (msgType){
            case LOGIN:{

            }break;
            case ReplyForLogin:{
                //get list
                if(!baseMsg.getParams().containsKey("msgList")){
                    return;
                }
                Type type = new TypeToken<HashMap<String,Object>>(){}.getType();
                Log.d("reply for login",(String)baseMsg.getParams().get("msgList"));
                Map<String,Object> msgList = new Gson().fromJson((String)baseMsg.getParams().get("msgList"),type);
                for(Map.Entry<String,Object> entry:msgList.entrySet()){
                    MsgCache.putPair(entry.getKey(),Long.parseLong((String)entry.getValue()));
                }
            }break;
            case PING:{
                System.out.println("receive ping from server----------");

                

            }break;
            case ChatMsg:{
                // TODO call different interfaces for different UI
                if(ClientUtils.onChatMsgRecievedForWeChat(baseMsg)){
                    break;
                }

                //TODO notify the new comming msg if wechat activity is not existed
                ClientUtils.notification(baseMsg);

                //TODO notify ChatFragment to update UI
                if(ClientUtils.onChatMsgRecievedForGroupList(baseMsg)){
                    break;
                }

                MsgCache.putPair(baseMsg.getGroupId(),baseMsg.getDate());
            }break;
            case ReplyForChatMsg:{
                ClientUtils.onChatMsgRecievedForWeChat(baseMsg);
                //ClientUtils.notification(baseMsg);
            }break;

            default:break;
        }
        ReferenceCountUtil.release(msgType);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    		throws Exception {
    	// TODO Auto-generated method stub
    	System.out.println("Error: "+cause.getMessage());
    }
    
	@Override
	protected void channelRead0(ChannelHandlerContext arg0, String arg1)
			throws Exception {
		// TODO Auto-generated method stub
		messageReceived(arg0,arg1);
	}
	
}
