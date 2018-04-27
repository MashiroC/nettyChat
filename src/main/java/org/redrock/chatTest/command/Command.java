package org.redrock.chatTest.command;

import com.sun.org.apache.regexp.internal.RE;
import io.netty.channel.Channel;
import org.redrock.chatTest.been.Message;

public interface Command {
    String SERVICE = "service";
    String PRINT = "print";

    Message clientRun(String username, String... input);

    Message serviceRun(Channel incoming, Message message);

    String getHelp();

    default String getCommandStr() {
        return this.getClass().getSimpleName().replace("Command", "").toLowerCase();
    }
}
