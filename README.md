# WebLogin
Version 2.0
[中文说明](https://github.com/kenvix/Minecraft-WebLogin/blob/master/README-zh.md)
# What's this 
When a player attempting to connect the server, this plugin will post player data to your server    
You can allow/disallow the player joining the server by return some specific message.    
Default message is <[AccessOK]> , return other message will kick the player and show the message to the player.    
This plugin also supports /ban and /unban command, when you enter these command, the plugin will post some data to your server    
# Usage 
## 1.Commands
/weblogin WebLogin Plugin Help    
/weblogin reload Reload plugin    
/wban Ban player by using weblogin    
/wunban Unban player by using weblogin    
/ban Ban player by using weblogin. Available if rewriteBanCommand is true    
/unban Unban player by using weblogin. Available if rewriteBanCommand is true    

##2.Permissions
moecraft.weblogin.command.reload      
Allows someone use /weblogin reload    

## 3.POST Data Descriptio
### Web Login
```text
[
    'name', //User name
    'uuid, //UUID
    'ip', //User IP
    'time', //Server time (UNIX Timestamp)
    'pwd', //Server Password
    'mac',//User MAC(By Luxcoldury)
]
```
Example:
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
### Web Ban
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
## 4.Configuration File
```yaml
# Server Password
pwd: 'helloworld'

# Success Message from server. Give this message will allow players to log in.
successMsg: '<[AccessOK]>'

###########################SETTINGS FOR WEB LOGIN #####################################
# Server URL
url: 'http://localhost/weblogin.php'

# Success Message from server. Give this message will allow players to log in
successMsg: '<[AccessOK]>'

# Custom Kick player message? "true" means show player message from server.WARNING: RETURN EMPTY MESSAGE MEANS CONNECT FAILED!!!
customKickMsg: true

# Kick player message. Unavaiable if "customKickMsg" is "true"
kickMsg: 'Access denied.'

# Retry if the server unavailable? Enter a number for the number of retries
retryIfServerOffline: 5

# Don't Kick Player If Server Unavailable?
noKickIfServerOffline: false

# Kick player if some fatal error happened?
kickIfWebloginError: true
# The message of kickIfWebloginError
kickIfWebloginErrorMessage: 'SERVER ERROR'
########################################################################################

############################# SETTINGS FOR WEB BAN #####################################
# Web Ban Server URL
banUrl: 'http://localhost/wenban.php'

# Enable Web Ban?
enableWebBan: true

# Kick target player if success?( Return successmsg( <[AccessOK]> ))
banKickIfSuccess: true

banKickMessage: 'You are banned!'

# Rewrite /ban and /unban command? May not work.
rewriteBanCommand: true

# Continue(allow other plugin to process ban command) if some fatal error happened? Unavaiable if "rewriteBanCommand" is "false"
banContinueIfFailed: false
########################################################################################
```
# Downloads
## Compiled:
Github:https://github.com/kenvix/Minecraft-WebLogin/raw/master/out/artifacts/net.moecraft.weblogin/net.moecraft.weblogin.jar        
## Git Repository: 
Github:https://github.com/kenvix/Minecraft-WebLogin       
# Other
My Blog: http://kenvix.com           
          
MoeCraft：https://www.moecraft.net         
