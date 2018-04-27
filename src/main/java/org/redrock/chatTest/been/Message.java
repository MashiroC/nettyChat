package org.redrock.chatTest.been;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {

    private String fromUser;
    private String toUser;
    private String text;
    private String commandStr;

    public Message(){}

    public Message(String commandStr,String fromUserNickname,String toUserNickname,String text){
        this.fromUser = fromUserNickname;
        this.toUser = toUserNickname;
        this.commandStr=commandStr;
        this.text= text;
    }

    public String toString(){
        return "消息类型："+commandStr+" 发送者："+ fromUser +" 接收者"+ toUser +" 消息："+text;
    }

}
