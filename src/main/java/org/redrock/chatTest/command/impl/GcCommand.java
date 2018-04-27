package org.redrock.chatTest.command.impl;

import io.netty.channel.Channel;
import org.redrock.chatTest.been.Message;
import org.redrock.chatTest.command.Command;
import org.redrock.chatTest.service.Receive;

public class GcCommand implements Command {

    private String help="{$groupname} //创建一个讨论组";

    public String getHelp(){
        return this.help;
    }

    @Override
    public Message clientRun(String username, String... input) {
        //-gc test
        Message message=null;
        if(input.length==1){
            String groupName = input[0];
            message = new Message(getCommandStr(),username,SERVICE,groupName);
        }
        return message;
    }

    @Override
    public Message serviceRun(Channel incoming, Message message) {
        String nickname = message.getFromUser();
        String groupName=message.getText();
        Receive.creatGroup(nickname,groupName);
        return null;
    }
}
