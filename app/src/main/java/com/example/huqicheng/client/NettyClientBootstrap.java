package com.example.huqicheng.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLEngine;

import com.google.gson.Gson;

import com.example.huqicheng.client.SslContextFactory;
import com.example.huqicheng.utils.ClientUtils;
import com.example.huqicheng.message.BaseMsg;
import com.example.huqicheng.message.Constants;
import com.example.huqicheng.message.MsgType;


public class NettyClientBootstrap {
    private static int port;
    private static String host;
    public static SocketChannel socketChannel;
    private int seconds = 1;
    public NettyClientBootstrap(int port, String host) {
        this.port = port;
        this.host = host;
    }
    
    private class ConnectionListener implements ChannelFutureListener {  
    	  
    	  
        private NettyClientBootstrap client;  
        
      
        public ConnectionListener(NettyClientBootstrap client) {  
            this.client = client;  
        }  
      
      
        @Override  
        public void operationComplete(ChannelFuture future) throws Exception {  
            if (!future.isSuccess()) {  
            	seconds = (seconds>=16)? seconds:seconds*2;
                System.out.println("Reconnection in "+seconds+" seconds");  
                final EventLoop eventLoop = future.channel().eventLoop();  
                eventLoop.schedule(new Runnable() {  
      
      
                    @Override  
                    public void run() {  
                        try {
                        	client.start();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }  
                }, seconds, TimeUnit.SECONDS);  
            }  
        }  
      
      
    }  
    public void start() throws InterruptedException {
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(host,port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
            	SSLEngine engine = SslContextFactory.getClientContext().createSSLEngine();
            	engine.setUseClientMode(true);
                engine.setWantClientAuth(false);
                socketChannel.pipeline().addLast(new SslHandler(engine));
            	
            	

                // On top of the SSL handler, add the text line codec.
            	//
            	//socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                
                socketChannel.pipeline().addLast(new StringEncoder());
                socketChannel.pipeline().addLast(new StringDecoder());
                //socketChannel.pipeline().addLast("length-decoder", new LengthFieldBasedFrameDecoder(369295620, 0, 4, 0, 4));
                socketChannel.pipeline().addLast(new IdleStateHandler(20,10,0));
                socketChannel.pipeline().addLast(new NettyClientHandler());
                
            }
        });
        ChannelFuture future =bootstrap.connect(host,port).addListener(new ConnectionListener(this)).sync();
        if (future.isSuccess()) {
            socketChannel = (SocketChannel)future.channel();
            System.out.println("connect server  成功---------");
        }
    }
    public static void main(String[]args){
    	System.out.println("start");
        NettyClientBootstrap bootstrap=ClientUtils.getInstance();

        BaseMsg loginMsg=new BaseMsg();
        loginMsg.setType(MsgType.LOGIN);
        loginMsg.putParams("user", "huqicheng");
        loginMsg.putParams("pwd", "huqicheng");
        if(bootstrap != null){
        	bootstrap.socketChannel.writeAndFlush(new Gson().toJson(loginMsg));
        }
        
//        while (true){
//            TimeUnit.SECONDS.sleep(3);
//            BaseMsg askMsg=new BaseMsg();
//            askMsg.setType(MsgType.ASK);
//            askMsg.putParams("body", "auth");
//            bootstrap.socketChannel.writeAndFlush(askMsg);
//        }
    }
}
