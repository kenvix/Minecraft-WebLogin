# WebLogin
Version 2.0
# 这是什么
当玩家尝试连接到服务器时，插件会自动向你填写的服务器POST数据    
当服务器返回特定消息的时候，允许或者拒绝玩家登录（默认消息为<[AccessOK]>，其他消息将作为踢出玩家所显示的信息）   
该插件也支持/ban和/unban指令，当你输入这些命令时，插件会自动提交一些数据到你指定的服务器
1.7.10以下版本未测试，如果能用的话请告知     
灵感来源于Beelogin，向其作者致谢         

# 它有什么优势
## 1.更安全
比起传统的CrazyLogin之类的进游戏后再登录的插件，他更安全     
众所周知，CL无法拦截MOD发送的数据包，比如NEI（没登录也能修改世界时间之类的）

## 2.更开放
WebLogin只是个服务端，一切验证行为都由目标网页端完成，WebLogin几乎不参与交互      
比如MoeCraft设计的“按一下按钮登录”

## 3.功能强大
它会向服务端提交 玩家名称，IP，UUID，时间 信息        
支持密码验证        
支持自定义消息（支持显示网页端给出的消息）                    
当网页端服务器宕机时，可以设置是否允许玩家直接登录（此时可以使用Crazylogin等传统插件）     

# 用法
## 命令
/weblogin WebLogin Plugin Help    
/weblogin reload Reload plugin    
/wban Ban player by using weblogin    
/wunban Unban player by using weblogin    
/ban Ban player by using weblogin. Available if rewriteBanCommand is true    
/unban Unban player by using weblogin. Available if rewriteBanCommand is true    
           
/weblogin 显示插件帮助  
/weblogin reload 重载插件设置       
/wban 使用weblogin封禁玩家
/wunban 使用weblogin解除封禁玩家
/ban 使用weblogin封禁玩家. 选项 rewriteBanCommand 为 true 时可用
/unban 使用weblogin解除封禁玩家. 选项 rewriteBanCommand 为 true 时可用

## 权限
moecraft.weblogin.command.reload      
Allows someone use /weblogin reload    
允许重载插件，OP默认拥有         

## 数据说明
### 外部网页登录
```text
[
    'name', //玩家名
    'uuid, //UUID
    'ip', //玩家IP
    'time', //服务器时间（UNIX时间戳）
    'pwd', //你设置的密码
    'mac', //MAC地址(感谢Luxcoldury)
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
### Web Ban 网页连锁封号
```text
[
    'name', //Target player name
    'sender', //The player who want to ban/unban the target player
    'reason', //Ban/unban reason
    'action', //Give 0 for ban action, give 1 for unban action
    'time', //Server time (UNIX Timestamp)
    'pwd', //Server Password
]
Web ban will rewrite /ban and /unban command.
```
## 4.插件配置文件说明
```yaml
# 登录成功/封号成功时服务器给出的消息。当服务器给出这个消息的时候允许玩家登录
successMsg: '<[AccessOK]>'

# 服务器密码（POST的pwd项）
pwd: 'helloworld'

###########################外部网页登录设置#####################################
# 目标服务器地址
url: 'http://localhost/weblogin.php'

# 是否启用外部网页登录
enableWebLogin: true



# 自定义踢出消息？ true表示显示服务器发出的消息。警告：服务器给出空消息表示服务器宕机！
customKickMsg: true

# 踢出玩家所显示的消息，当开启自定义踢出消息时不可用
kickMsg: 'Access denied.'

# 是否要在服务器宕机时重试？输入重试次数
retryIfServerOffline: 5

# 如果服务器宕机，是否不要踢出玩家？
noKickIfServerOffline: false

# 是否在发生致命错误时踢出玩家？
kickIfWebloginError: true
# 发生致命错误时踢出玩家的消息
kickIfWebloginErrorMessage: 'SERVER ERROR'
########################################################################################

############################# 网页连锁封号的设置 #####################################
# 网页连锁封号的目标服务器地址
banUrl: 'http://localhost/wenban.php'

# 启用网页连锁封号？
enableWebBan: true

# 是否在成功时（给出<[AccessOK]>）踢出玩家？
banKickIfSuccess: true

# 封号成功时踢出玩家的消息
banKickMessage: 'You are banned!'

# 重写 /ban /unban 指令? 可能不工作
rewriteBanCommand: true

# Continue(allow other plugin to process ban command) if some fatal error happened? Unavaiable if "rewriteBanCommand" is "false"
banContinueIfFailed: false
########################################################################################
```
# Downloads 下载
## Compiled: 已编译版本下载:
Github:https://github.com/kenvix/Minecraft-WebLogin/raw/master/out/artifacts/net.moecraft.weblogin/net.moecraft.weblogin.jar        
Git@OSC:https://git.oschina.net/kenvix/Minecraft-WebLogin/raw/master/out/artifacts/net.moecraft.weblogin/net.moecraft.weblogin.jar    
## Git Repository: 代码库:
Github:https://github.com/kenvix/Minecraft-WebLogin       
Git@OSC:https://git.oschina.net/kenvix/Minecraft-WebLogin          
# Other 其他:
My Blog: http://kenvix.com           

博客：http://kenvix.com           
MoeCraft：https://www.moecraft.net         
博客发布贴：http://kenvix.com/?p=31
