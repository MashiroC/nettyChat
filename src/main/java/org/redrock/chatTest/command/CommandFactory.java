package org.redrock.chatTest.command;

import io.netty.channel.Channel;
import org.redrock.chatTest.been.Message;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CommandFactory {
    private Map<String, Command> commandMap = null;
    private String packageName = null;

    public CommandFactory(String packageName) {
        this.packageName = packageName;
        load();
    }

    private void load() {
        commandMap = new HashMap<>();
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageName.replaceAll("\\.", "/"));
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();
                if (protocol.equalsIgnoreCase("file")) {
                    String packagePath = resource.getPath();
                    loadClass(packageName, packagePath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadClass(String packageName, String packagePath) {
        File[] files = new File(packagePath).listFiles(file -> file.isDirectory() || file.getName().endsWith(".class"));
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (file.isDirectory()) {
                    String subPackageName = packageName + "." + fileName;
                    String subPackagePath = packagePath + "/" + fileName;
                    loadClass(subPackageName, subPackagePath);
                } else {
                    fileName = packageName + "." + fileName.substring(0, fileName.lastIndexOf("."));
                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName(fileName);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (clazz != null) {

                        if (!clazz.equals(Command.class) && Command.class.isAssignableFrom(clazz)) {
                            Command command = null;
                            try {
                                command = (Command) clazz.newInstance();
                                String commandStr = clazz.getSimpleName();
                                commandStr = commandStr.replaceAll("Command", "");
                                commandMap.put(commandStr.toLowerCase(), command);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public List<String> helpStr(){
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Command> command:commandMap.entrySet()) {
            String str = "-"+command.getKey()+" "+command.getValue().getHelp();
            list.add(str);
        }
        return list;
    }

    public Message clientInvoke(String commandStr, String username, String text) {
        Command command = commandMap.get(commandStr);
        Message message = null;
        if (command != null) {
            if (text == null) {
                message = command.clientRun(username);
            } else {
                message = command.clientRun(username, text.split("\\s+"));
            }

            if (message != null) {
                return message;
            }
        }
        return null;
    }

    public Message serviceInvoke(Channel incoming, Message message) {
        Message resultMessage = null;
        if (message != null) {
            String commandStr = message.getCommandStr();
            if (commandStr != null) {
                Command command = commandMap.get(commandStr);
                if (command != null) {
                    resultMessage = command.serviceRun(incoming,message);
                }
            } else {
                throw new RuntimeException("命令为null");
            }
        } else {
            throw new RuntimeException("message为null");
        }
        return resultMessage;
    }
}
