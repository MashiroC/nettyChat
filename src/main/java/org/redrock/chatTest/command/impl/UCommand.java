package org.redrock.chatTest.command.impl;

import io.netty.channel.Channel;
import org.redrock.chatTest.been.Message;
import org.redrock.chatTest.command.Command;
import org.redrock.chatTest.service.Receive;

public class UCommand implements Command {

    private String help="{$username} {&text} //向用户发消息";

    public String getHelp(){
        return this.help;
    }

    @Override
    public Message clientRun(String username, String... input) {
        Message message = null;
        if (input.length > 1) {
            StringBuilder text = new StringBuilder();
            String toUsername = input[0];
            for (int i = 1; i < input.length; i++) {
                text.append(input[i]);
            }
            message = new Message(getCommandStr(), username, toUsername, text.toString());
        }
        return message;
    }

    @Override
    public Message serviceRun(Channel incoming, Message message) {
        return Receive.sendMessageToUser(message);
    }
}
