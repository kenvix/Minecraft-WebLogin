package net.moecraft.weblogin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class WebLogin extends JavaPlugin implements Listener {



	public String g(String n) {
		return getConfig().getString(n);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (g("enableWebLogin").equals("true") && cmd.getName().equalsIgnoreCase("weblogin")){
			if(args.length < 1) {
				sender.sendMessage("------------WebLogin By Kenvix @ MoeCraft------------");
				sender.sendMessage("/weblogin              WebLogin Command Help");
				sender.sendMessage("/weblogin reload       Reload Plugin");
				sender.sendMessage("/wban                  Ban player by using weblogin");
				sender.sendMessage("/wunban                Unban player by using weblogin");
				sender.sendMessage("----------------------------------------------------");
				sender.sendMessage("Web Login Status: " + g("enableWebLogin"));
				sender.sendMessage("Web Ban Status: " + g("enableWebBan"));
				sender.sendMessage("Web Ban Rewrite /ban Command: " + g("rewriteBanCommand"));
				sender.sendMessage("----------------------------------------------------");
			} else {
				if(args[0].equals("reload") && sender.hasPermission("moecraft.weblogin.command.reload")) {
					reloadConfig();
					sender.sendMessage("WebLogin Reloaded");
				} else {
					sender.sendMessage("Unknown Command! Type /weblogin for help");
				}
			}
			return true;
		}
		if(g("enableWebBan").equals("true")) {
			int action = -1; //0 for ban . 1 for unban
			if(cmd.getName().equalsIgnoreCase("wban") || (cmd.getName().equalsIgnoreCase("ban") && g("rewriteBanCommand").equals("true"))) {
				if(!sender.hasPermission("bukkit.command.ban.player") && !sender.hasPermission("bukkit.command.ban")) {
					sender.sendMessage("Access denied");
					return true;
				}
				if(args.length < 1) {
					sender.sendMessage("[Weblogin] Ban a player");
					sender.sendMessage("/wban <player> [reason]");
					if(g("rewriteBanCommand").equals("true")) {
						sender.sendMessage("/ban <player> [reason]");
					}
					return true;
				}
				action = 0;
			}
			if(cmd.getName().equalsIgnoreCase("wunban") || (cmd.getName().equalsIgnoreCase("unban") && g("rewriteBanCommand").equals("true"))) {
				if(!sender.hasPermission("bukkit.command.ban.player") && !sender.hasPermission("bukkit.command.ban")) {
					sender.sendMessage("Access denied");
					return true;
				}
				if(args.length < 1) {
					sender.sendMessage("[Weblogin] Unban a player");
					sender.sendMessage("/wunban <player> [reason]");
					if(g("rewriteBanCommand").equals("true")) {
						sender.sendMessage("/unban <player> [reason]");
					}
					return true;
				}
				action = 1;
			}
			if(action == 1 || action == 0) {
				String postData = "name=" + args[0] +
						"&sender=" + sender.getName() +
						"&action=" + action +
						"&time=" + System.currentTimeMillis() +
						"&pwd=" + g("pwd");
				if(args.length >= 2) {
					if(!args[1].isEmpty()) {
						postData += "&reason="+ args[1];
					}
				}
				sender.sendMessage("[Weblogin] Banning/Unbanning Player: " + args[0]);
				try {
					String r = post(g("banUrl"), postData);
					if(!r.isEmpty()) {
						if(r.equals(g("successMsg"))) {
							if(action == 1) {
								sender.sendMessage("[Weblogin] Unbanned " + args[0]);
							} else {
								if(g("banKickIfSuccess").equals("true")) {
									Player player = Bukkit.getServer().getPlayer(args[0]);
									if(player.isOnline()) {
										player.kickPlayer(g("banKickMessage"));
									}
								}
								sender.sendMessage("[Weblogin] Banned " + args[0]);
							}
						}
					}
				} catch (Exception ex) {
					sender.sendMessage("[Weblogin] Ban/Unban Failed: " + args[0]);
					if (action == 1) {
						getLogger().warning(sender.getName() +  " Wants to Unban " + args[0] + " But failed: ");
					} else {
						getLogger().warning(sender.getName() +  " Wants to Ban " + args[0] + " But failed: ");
					}
					ex.getStackTrace();
					if(g("banContinueIfFailed").equals("true")) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
			getLogger().info("Data Folder not exists, create it");
		}
		File file = new File(getDataFolder(), "config.yml");
		if(!file.exists()) {
			saveDefaultConfig();
			getLogger().info("Config not exists, create it");
		}
		reloadConfig();
		getLogger().info("Enabled - By: Kenvix @ MoeCraft");
	}

	public void onDisable() {
		getLogger().info("Disabled");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(AsyncPlayerPreLoginEvent e) {
		if(g("enableWebLogin").equals("true")) {
			try{
				getLogger().info(e.getName() + e.getAddress() + " is trying to log in ...");
				String postData = "name=" + e.getName() +
						"&uuid=" + e.getUniqueId() +
						"&ip=" + e.getAddress().toString().replace("/","") +
						"&time=" + System.currentTimeMillis() +
						"&pwd=" + g("pwd");
				try {
					postData += "&mac=" + getMACAddress(e.getAddress());
				} catch (Exception ex) {}
				int i = 0;
				for(i=0; i < Integer.parseInt(g("retryIfServerOffline")); i++) {
					try {
						String r       = post(g("url"), postData);
						if(!r.isEmpty()) {
							if(!r.equals(g("successMsg"))) {
								String kickMsg = "";
								if(g("customKickMsg").equals("true")) {
									kickMsg = r;
								} else {
									kickMsg = g("kickMsg");
								}
								e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, kickMsg);
							} else {
								e.allow();
							}
							break;
						}
					} catch (Exception ex) {
						getLogger().warning("Conect failed,try again:" + ex.getMessage());
					}
				}
				if(i > Integer.parseInt(g("retryIfServerOffline"))) {
					if(g("noKickIfServerOffline").equals("true")) {
						getLogger().warning("Can't connect to the server. Allow player " + e.getName());
						e.allow();
					} else {
						getLogger().warning("Can't connect to the server. Kick player " + e.getName());
						e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, g("kickMsg"));
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
				if(g("kickIfWebloginError").equals("true")) {
					e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, g("kickIfWebloginErrorMessage"));
				} else {
					e.allow();
				}
			}
		}
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * @param url 发送请求的 URL
	 * @param data 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public String post(String url, String data) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(data);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception ex) {
			getLogger().warning("!!!Can't POST Data to the server!!!");
			ex.printStackTrace();
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out != null) out.close();
				if(in != null) in.close();
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}


	private static String getMACAddress(InetAddress ia) throws Exception {
        //获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

        //下面代码是把mac地址拼装成String
        StringBuffer sb = new StringBuffer();

        for(int i=0;i<mac.length;i++){
            if(i!=0){
                sb.append("-");
            }
            //mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length()==1?0+s:s);
        }

        //把字符串所有小写字母改为大写成为正规的mac地址并返回
        return sb.toString().toUpperCase();
    }
}