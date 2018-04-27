package org.redrock.chatTest.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.redrock.chatTest.been.Message;
import org.redrock.chatTest.command.CommandFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    private final int PORT;
    private final String HOST;
    private final String PACKAGE_NAME;
    private String nickname;
    private Channel channel;
    private CommandFactory commandFactory;


    public Client(String packageName, String host, int port) {
        this.PORT = port;
        this.HOST = host;
        this.PACKAGE_NAME = packageName;
        commandFactory = new CommandFactory(PACKAGE_NAME);
    }

    public void run() throws InterruptedException, IOException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(HOST, PORT))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ObjectEncoder())
                                    .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(this.getClass().getClassLoader())))
                                    .addLast(new ClientHandler());
                        }
                    });
            channel = b.connect().sync().channel();
            clientRun();
        } finally {
            group.shutdownGracefully();
        }

    }

    private void send(Message msg) {
        channel.writeAndFlush(msg);
    }

    private void clientRun() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        nickname = reader.readLine();
        send(commandFactory.clientInvoke("add",nickname,null));
        while (true) {
            String input = reader.readLine();
            if("quit".equals(input)){
                break;
            }
            if("help".equals(input)){
                help();
                continue;
            }
            Matcher matcher = Pattern.compile("-(\\w+)\\s+(.*)").matcher(input);
            if (matcher.find()) {
                String commandStr = matcher.group(1);
                String text = matcher.group(2);
                Message message = commandFactory.clientInvoke(commandStr, nickname, text);
                send(message);
            } else {
                System.out.println("input error");
            }
        }
        reader.close();
    }

    private void help() {
        for (String str:commandFactory.helpStr()) {
            System.out.println(str);
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        new Client("org.redrock.chatTest.command.impl","127.0.0.1", 8080).run();

    }

}
