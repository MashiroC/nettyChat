package org.redrock.chatTest.command.impl;

import io.netty.channel.Channel;
import org.redrock.chatTest.been.Message;
import org.redrock.chatTest.command.Command;
import org.redrock.chatTest.service.Receive;

public class GCommand implements Command {

    private String help="{$groupname} {$text}//向一个讨论组发消息";

    public String getHelp(){
        return this.help;
    }

    @Override
    public Message clientRun(String username, String... input) {
        Message message = null;
        if(input.length>1){
            String toUserName = input[0];
            StringBuilder text = new StringBuilder();
            for (int i=1;i<input.length;i++) {
                text.append(input[i]);
            }
            message = new Message(getCommandStr(),username,toUserName,text.toString());
        }
        return message;
    }

    @Override
    public Message serviceRun(Channel incoming, Message message) {
        return Receive.sendMessageToGroup(message,incoming);
    }
}
