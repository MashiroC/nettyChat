# 一个产品经理肯定会打死我的聊天器

如题 用户体验极差 但是功能齐全

可以私聊 也可以群聊

群聊的话需要先加入讨论组

操作如同linux命令行一般

操作如下：

```
-add {$groupname} //加入一个讨论组

-print 正在开发

-gl {$groupname} //浏览一个讨论组的用户

-u {$username} {&text} //向用户发消息

-g {groupname}  {text}//向一个讨论组发消息

-ls {user||group} //浏览在线的用户名单或已存在的讨论组名单

-gc {$groupname} //创建一个讨论组

-help //调出help

-quit //退出

```

这是大半晚上赶的 学了反射之后感觉这次的代码应该算是优雅不少 算是低耦合了？

还有十六个小时我就跑到成都了 原本想加全一些功能 不想加了 就这样吧