package net.moecraft.weblogin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;
import sun.swing.StringUIClientPropertyKey;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class WebLogin extends JavaPlugin implements Listener {



	public String g(String n) {
		return getConfig().getString(n);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("weblogin")){
			if(args.length < 1) {
				sender.sendMessage("------------WebLogin By Kenvix @ MoeCraft------------");
				sender.sendMessage("/weblogin              WebLogin Command Help");
				sender.sendMessage("/weblogin reload       Reload Plugin");
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
		getLogger().info(e.getName() + e.getAddress() + " is trying to log in ...");
		String postData = "name=" + e.getName() +
				"&uuid=" + e.getUniqueId() +
				"&ip=" + e.getAddress().toString().replace("/","") +
				"&time=" + System.currentTimeMillis() +
				"&pwd=" + g("pwd");
		String r       = post(g("url"), postData);
		if(r.isEmpty()) {
			if(g("noKickIfServerOffline").equals("true")) {
				getLogger().info("Can't connect to the server. Allow player " + e.getName());
				e.allow();
			} else {
				getLogger().info("Can't connect to the server. Kick player " + e.getName());
				e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, g("kickMsg"));
			}
		} else {
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
}