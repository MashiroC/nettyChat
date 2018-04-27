package org.redrock.chatTest.service;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.redrock.chatTest.been.Message;
import org.redrock.chatTest.command.CommandFactory;

public class RequestHandler extends SimpleChannelInboundHandler<Message> {

    private static CommandFactory commandFactory = new CommandFactory("org.redrock.chatTest.command.impl");

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        Channel incoming = channelHandlerContext.channel();
        Message responseMessage = commandFactory.serviceInvoke(incoming, message);
        if(responseMessage!=null){
            incoming.writeAndFlush(responseMessage);
        }
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        Receive.addUser(incoming);
        System.out.println(incoming.remoteAddress() + "加入");

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        Receive.removedUser(incoming);
        System.out.println(incoming.remoteAddress() + "离开");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("Client:" + incoming.remoteAddress() + " 抛异常");
        System.out.println(cause.getMessage());
        System.out.println(cause.getLocalizedMessage());
        ctx.close();
    }

}
