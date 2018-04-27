package org.redrock.chatTest.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class ChatServer {
    private final int PORT;

    public ChatServer(int port) {
        this.PORT = port;
    }

    public void run() throws InterruptedException, IOException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(PORT))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
                                    .addLast(new ObjectEncoder())
                                    .addLast(new RequestHandler());
                        }
                    });
            ChannelFuture f = b.bind().sync();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in));
            while (true){
                String input = reader.readLine();
//                Message message = new Message(input);
//                for(Channel channel:RequestHandler.getGroup()){
//                    channel.writeAndFlush(message);
//                }
            }
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        new ChatServer(8080).run();
    }

}
