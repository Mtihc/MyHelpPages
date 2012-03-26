package com.mtihc.minecraft.myhelppages;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.minecraft.core1.YamlFile;
import com.mtihc.minecraft.myhelppages.exceptions.MyHelpPageException;

public class ConfigYaml extends YamlFile {

	public ConfigYaml(JavaPlugin plugin) {
		super(plugin, "config");
		// causes the default-pages.yml to be loaded... if necessary
		if(!getConfig().contains("pages")) {
			addDefaults(getConfig());
		}
	}

	private ConfigurationSection pages() {
		YamlConfiguration config = getConfig();
		ConfigurationSection result = config.getConfigurationSection("pages");
		if(result == null) {
			result = config.createSection("pages");
			addDefaults(config);
		}
		return result;
	}
	
	private ConfigurationSection messages() {
		ConfigurationSection result = getConfig().getConfigurationSection("messages");
		if(result == null) {
			// causes the default config.yml to be loaded
			reload();
		}
		return result;
	}
	
	private void addDefaults(YamlConfiguration config) {
		try {
			config.options().copyDefaults(true);
			
			InputStream defConfigStream = plugin.getResource("default-pages.yml");
			if (defConfigStream != null) {
				YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
				config.addDefaults(defConfig);
				save();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getMessageNoPagePermission() {
		return messages().getString("noPagePermission");
	}
	
	public String getMessagePageNotFound() {
		return messages().getString("pageNotFound");
	}
	
	public String getMessagePageTile() {
		return messages().getString("pageTitle");
	}
	
	public void setMessageNoPagePermission(String value) {
		messages().set("noPagePermission", value);
	}
	
	public void setMessagePageNotFound(String value) {
		messages().set("pageNotFound", value);
	}
	
	public void setMessagePageTile(String value) {
		messages().set("pageTitle", value);
	}
	
	public List<String> getPage(String name) {
		return pages().getStringList(name);
	}
	
	public void addPage(String name, List<String> lines) throws MyHelpPageException {
		if(!pages().contains(name)) {
			throw new MyHelpPageException(MyHelpPageException.Type.PAGE_ALREADY_EXIST, ChatColor.RED + "Page " + ChatColor.WHITE + "\"" + name + "\"" + ChatColor.RED + " already not exists.");
		}
		else {
			pages().set(name, lines);
		}
	}
	
	public void removePage(String name) throws MyHelpPageException {
		if(!pages().contains(name)) {
			throw new MyHelpPageException(MyHelpPageException.Type.PAGE_NOT_EXIST, ChatColor.RED + "Page " + ChatColor.WHITE + "\"" + name + "\"" + ChatColor.RED + " does not exist.");
		}
		else {
			pages().set(name, null);
		}
	}
	
	public Set<String> getPageNames() {
		return pages().getKeys(false);
	}

	public boolean hasPage(String name) {
		return pages().contains(name);
	}
	
	
}
