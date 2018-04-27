package org.redrock.chatTest.command.impl;

import io.netty.channel.Channel;
import org.redrock.chatTest.been.Message;
import org.redrock.chatTest.command.Command;

public class PrintCommand implements Command {

    private String help="正在开发";

    public String getHelp(){
        return this.help;
    }

    @Override
    public Message clientRun(String username, String... input) {
        return null;
    }

    @Override
    public Message serviceRun(Channel incoming, Message message) {
        return null;
    }
}
