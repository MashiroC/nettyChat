package org.redrock.chatTest.command.impl;

import io.netty.channel.Channel;
import org.redrock.chatTest.been.Message;
import org.redrock.chatTest.command.Command;
import org.redrock.chatTest.service.Receive;

import java.util.List;

public class LsCommand implements Command {

    private String help="{user||group} //浏览在线的用户名单或已存在的讨论组名单";

    public String getHelp(){
        return this.help;
    }

    @Override
    public Message clientRun(String username, String... input) {
        Message message = null;
        if (input.length > 0) {
            String type = input[0];
            if ("group".equals(type) || "user".equals(type)) {
                message = new Message(getCommandStr(), username, SERVICE, type);
            }
        }
        return message;
    }

    @Override
    public Message serviceRun(Channel incoming, Message message) {
        String nickname = message.getFromUser();
        String type = message.getText();
        List<String> list = null;
        if ("group".equals(type)) {
            list = Receive.getGroupNameList();
        } else if ("user".equals(type)) {
            list = Receive.getUserNicknameList();
        }
        Message resultMessage = null;
        if (list != null) {
            resultMessage = new Message(PRINT, SERVICE, nickname, list.toString());
        }
        return resultMessage;
    }
}
