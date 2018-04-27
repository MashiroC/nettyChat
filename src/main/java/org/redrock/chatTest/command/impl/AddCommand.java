package org.redrock.chatTest.command.impl;

import io.netty.channel.Channel;
import org.redrock.chatTest.been.Message;
import org.redrock.chatTest.command.Command;
import org.redrock.chatTest.service.Receive;

public class AddCommand implements Command {

    private String help="{$groupname} //加入一个讨论组";

    public String getHelp(){
        return this.help;
    }

    @Override
    public Message clientRun(String username, String... input) {
        Message message = null;
        if (input.length>0) {
            String text = input[0];
            message = new Message(getCommandStr(), username, SERVICE, text);
        } else {
            message = new Message(getCommandStr(), username, SERVICE, null);
        }
        return message;
    }

    @Override
    public Message serviceRun(Channel incoming, Message message) {
        String nickname = message.getFromUser();
        String groupName = message.getText();
        Message resultMessage = null;
        if (groupName == null) {
            Receive.userJoin(incoming, nickname);
        } else {
            resultMessage = Receive.joinGroup(nickname, groupName);
        }
        return resultMessage;
    }

}
