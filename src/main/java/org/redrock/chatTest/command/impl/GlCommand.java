package org.redrock.chatTest.command.impl;

import io.netty.channel.Channel;
import org.redrock.chatTest.been.Message;
import org.redrock.chatTest.command.Command;
import org.redrock.chatTest.service.Receive;

import java.util.List;

public class GlCommand implements Command {

    private String help="{$groupname} //浏览一个讨论组的用户";

    public String getHelp(){
        return this.help;
    }

    @Override
    public Message clientRun(String username, String... input) {
        Message message=null;
        if(input.length==1){
            String groupName = input[0];
            message=new Message(getCommandStr(),username,SERVICE,groupName);
        }
        return message;
    }

    @Override
    public Message serviceRun(Channel incoming, Message message) {
        String groupname = message.getText();
        String nickname = message.getFromUser();
        List<String> userNicknameList= Receive.getGroupUserList(groupname);
        Message resultMessage;
        if(userNicknameList!=null){
            resultMessage=new Message(PRINT,Command.SERVICE,nickname,userNicknameList.toString());
        }else {
            resultMessage=new Message(PRINT,Command.SERVICE,nickname,"未找到讨论组");
        }
        return resultMessage;
    }
}
