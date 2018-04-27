package org.redrock.chatTest.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.redrock.chatTest.been.Message;

import java.util.Date;

public class ClientHandler extends SimpleChannelInboundHandler<Message> {

//    private static CommandFactory commandFactory = new CommandFactory("org.redrock.chatTest.command.impl");

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        System.out.println("【"+message.getFromUser()+"】 "+new Date());
        System.out.println(message.getText());
        System.out.println();
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("Client:" + incoming.remoteAddress() + " 抛异常");
        System.out.println(cause.getMessage());
        ctx.close();

    }
}
