package com.dubbo.core.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class NettyExample {

    public static void main(String[] args) throws IOException {
        // BIO
        ServerSocket serverSocket = new ServerSocket(9022);
        Socket client = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        String request, response;
        while ((request = in.readLine()) != null) {
            if ("Done".equals(request)) {
                break;
            }
            response = request + " SUCCESS";
            out.println(response);
        }

        // NIO
        Channel channel = null;
        ChannelFuture channelFuture = channel.connect(new InetSocketAddress("127.0.0.1", 9022));
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    ByteBuf byteBuf = Unpooled.copiedBuffer("hello", Charset.defaultCharset());
                    ChannelFuture future = channelFuture.channel().writeAndFlush(byteBuf);
                } else {
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }

}
