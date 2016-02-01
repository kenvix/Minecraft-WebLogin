#这是什么
当玩家尝试连接到服务器时，插件会自动向你填写的服务器POST数据      
当服务器返回特定消息的时候，允许或者拒绝玩家登录（默认消息为<[AccessOK]>，其他消息将作为踢出玩家所显示的信息）     
1.7.10以下版本未测试，如果能用的话请告知       
灵感来源于Beelogin，向其作者致谢         

#它有什么优势
##1.更安全
比起传统的CrazyLogin之类的进游戏后再登录的插件，他更安全     
众所周知，CL无法拦截MOD发送的数据包，比如NEI（没登录也能修改世界时间之类的）

##2.更开放
WebLogin只是个服务端，一切验证行为都由目标网页端完成，WebLogin几乎不参与交互      
比如MoeCraft设计的“按一下按钮登录”

##3.功能强大
它会向服务端提交 玩家名称，IP，UUID，时间 信息        
支持密码验证        
支持自定义消息（支持显示网页端给出的消息）                    
当网页端服务器宕机时，可以设置是否允许玩家直接登录（此时可以使用Crazylogin等传统插件）     

#用法
##1.命令
/weblogin 显示插件帮助  
/weblogin reload 重载插件设置          

##2.权限
moecraft.weblogin.command.reload      
允许重载插件，OP默认拥有         

##3.POST数据说明
```text
[
    'name', //玩家名
    'uuid, //UUID
    'ip', //玩家IP
    'time', //服务器时间（UNIX时间戳）
    'pwd', //你设置的密码
]
```
范例：( 来自PHP的print_r() )
```text
Array
(
 [name] => Kenvix
 [uuid] => 922328d2-6a01-33f7-b636-a8f5aacc9795
 [ip] => 127.0.0.1
 [time] => 1454318534931
 [pwd] => helloworld
)
```
##4.插件配置文件说明
```yaml
# 目标服务器地址
url: 'http://localhost/1.php'

# 服务器密码（POST的pwd项）
pwd: 'helloworld'

# 登录成功时服务器给出的消息。当服务器给出这个消息的时候允许玩家登录
successMsg: '<[AccessOK]>'

# 自定义踢出消息？ true表示显示服务器发出的消息。警告：服务器给出空消息表示服务器宕机！
customKickMsg: true

# 踢出玩家所显示的消息，当开启自定义踢出消息时不可用
kickMsg: 'Access denied.'

# 如果服务器宕机，是否不要踢出玩家？
noKickIfServerOffline: false


# 别动这东西！！
version: 1.0
```
#下载
##已编译版本下载:
Github:https://github.com/kenvix/Minecraft-WebLogin/raw/master/out/artifacts/net.moecraft.weblogin/net.moecraft.weblogin.jar        
Git@OSC:https://git.oschina.net/kenvix/Minecraft-WebLogin/raw/master/out/artifacts/net.moecraft.weblogin/net.moecraft.weblogin.jar    
##代码库:
Github:https://github.com/kenvix/Minecraft-WebLogin       
Git@OSC:https://git.oschina.net/kenvix/Minecraft-WebLogin          
#其他
博客：http://zhizhe8.net           
MoeCraft：https://www.moecraft.net         
博客发布贴：http://zhizhe8.net/?p=31