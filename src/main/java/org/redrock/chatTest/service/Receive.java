package org.redrock.chatTest.service;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.redrock.chatTest.been.Message;
import org.redrock.chatTest.command.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Receive {
    private static ChannelGroup onlionGroup;//在线的列表

    private static Map<Channel, String> channelMap;//channel - 昵称

    private static Map<String, Channel> nicknameMap;//昵称 - channel

    private static Map<String, ChannelGroup> chatGroup;

    static {
        onlionGroup = new DefaultChannelGroup("onlionGroup", GlobalEventExecutor.INSTANCE);
        channelMap = new HashMap<>();
        nicknameMap = new HashMap<>();
        chatGroup = new HashMap<>();
    }

    public static void userJoin(Channel incoming, String username) {
        channelMap.put(incoming, username);
        nicknameMap.put(username, incoming);
    }

    public static void addUser(Channel channel) {
        onlionGroup.add(channel);
    }

    private static void send(Message msg) {
        System.out.println(msg.toString());
        String toUser = msg.getToUser();
        Channel channel = nicknameMap.get(toUser);
        channel.writeAndFlush(msg);
        System.out.println("发送成功\n");
    }

    public static List<String> getGroupNameList() {
        List<String> groupNicknameList = new ArrayList<>();
        groupNicknameList.addAll(chatGroup.keySet());
        return groupNicknameList;
    }

    public static List<String> getUserNicknameList() {
        List<String> userNicknameList = new ArrayList<>();
        userNicknameList.addAll(channelMap.values());
        return userNicknameList;
    }

    private boolean isUserExist(String toUserNickname) {
        Channel channel = nicknameMap.get(toUserNickname);
        return channel != null && onlionGroup.contains(channel);
    }

    public static void removedUser(Channel incoming) {
        String nickname = channelMap.get(incoming);
        onlionGroup.remove(incoming);
        channelMap.remove(incoming);
        nicknameMap.remove(nickname);
    }

    public static void creatGroup(String nickname, String groupName) {
        ChannelGroup group = new DefaultChannelGroup(groupName, GlobalEventExecutor.INSTANCE);
        Channel channel = nicknameMap.get(nickname);
        group.add(channel);
        chatGroup.put(groupName, group);
    }

    public static Message sendMessageToGroup(Message message, Channel incoming) {
        String toGroup = message.getToUser();
        String fromUser = message.getFromUser();
        ChannelGroup group = chatGroup.get(toGroup);
        Message resultMessage = null;
        if (group != null) {
            if (group.contains(incoming)) {
                for (Channel channel : group) {
                    if (!channel.equals(incoming)) {
                        channel.writeAndFlush(message);
                    }
                }
            } else {
                resultMessage = new Message(Command.PRINT, Command.SERVICE, fromUser, "未加入聊天室！");
            }
        } else {
            resultMessage = new Message(Command.PRINT, Command.SERVICE, fromUser, "未找到聊天室");
        }
        return resultMessage;
    }

    public static List<String> getGroupUserList(String groupname) {
        System.out.println(chatGroup);
        ChannelGroup group = chatGroup.get(groupname);
        System.out.println(group);
        List<String> list = null;
        if (group != null) {
            list = new ArrayList<>();
            for (Channel channel : group) {

                String nickname = channelMap.get(channel);
                list.add(nickname);
            }
        }
        return list;
    }

    public static Message sendMessageToUser(Message message) {
        String toUser = message.getToUser();
        String fromUser = message.getFromUser();
        Channel channel = nicknameMap.get(toUser);
        Message resultMessage = null;
        if (channel != null) {
            message.setCommandStr(Command.PRINT);
            channel.writeAndFlush(message);
        } else {
            resultMessage = new Message(Command.PRINT, Command.SERVICE, fromUser, "未找到用户");
        }
        return resultMessage;
    }

    public static Message joinGroup(String nickname, String groupName) {
        Channel channel = nicknameMap.get(nickname);
        ChannelGroup group = chatGroup.get(groupName);
        Message message = null;
        if (group != null) {
            group.add(channel);
            message = new Message(Command.PRINT, Command.SERVICE, nickname, "加入成功！");
        } else {
            message = new Message(Command.PRINT, Command.SERVICE, nickname, "未找到聊天室！");
        }
        return message;
    }
}
