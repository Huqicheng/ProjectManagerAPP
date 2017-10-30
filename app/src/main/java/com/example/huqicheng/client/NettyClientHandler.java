package com.example.huqicheng.client;

import android.util.Log;

import java.util.concurrent.TimeUnit;


import com.example.huqicheng.message.BaseMsg;
import com.example.huqicheng.message.MsgType;
import com.example.huqicheng.utils.ClientUtils;
import com.google.gson.Gson;



import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
	@Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);

        //reconnect to server
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
            	NettyClientBootstrap client = ClientUtils.getInstance();
            	try {
					client.start();
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
        ClientUtils.onChatMsgRecievedForWeChat(baseMsg);
    	MsgType msgType=baseMsg.getType();
        switch (msgType){
            case LOGIN:{
                //向服务器发起登录
                Log.d("Debug:", "messageReceived: msg ");
            }break;
            case PING:{
                System.out.println("receive ping from server----------");

                

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
