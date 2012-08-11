package com.mtihc.minecraft.myhelppages;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.minecraft.myhelppages.exceptions.HelpPageException;
import com.mtihc.minecraft.myhelppages.util.YamlFile;

/**
 * Implementation of the <code>IHelpConfiguration</code> interface. 
 * 
 * @author Mitch
 *
 */
public class HelpConfiguration extends YamlFile implements IHelpConfiguration {

	public HelpConfiguration(JavaPlugin plugin) {
		super(plugin, "config");
		reload();
		// causes the default-pages.yml to be loaded... if necessary
		if(!getConfig().contains("pages")) {
			addDefaults("default-pages");
		}
	}

	private ConfigurationSection pages() {
		YamlConfiguration config = getConfig();
		// get pages section
		ConfigurationSection result = config.getConfigurationSection("pages");
		if(result == null) {
			// create default if it doesnt exist
			result = config.createSection("pages");
			addDefaults("default-pages");
		}
		return result;
	}
	
	private ConfigurationSection messages() {
		ConfigurationSection result = getConfig().getConfigurationSection("messages");
		if(result == null) {
			// causes the defaults to be loaded
			reload();
		}
		return result;
	}
	
	
	
	
	
	
	@Override
	public String getMessageNoPagePermission() {
		return messages().getString("noPagePermission");
	}

	@Override
	public String getMessagePageNotFound() {
		return messages().getString("pageNotFound");
	}

	@Override
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
	
	
	
	
	

	@Override
	public Set<String> getPageNames() {
		return pages().getKeys(false);
	}

	@Override
	public boolean hasPage(String name) {
		return pages().contains(name);
	}
	
	@Override
	public List<String> getPage(String name) {
		return pages().getStringList(name);
	}
	
	
	
	
	
	
	
	public void addPage(String name, List<String> lines) throws HelpPageException {
		if(!pages().contains(name)) {
			throw new HelpPageException(HelpPageException.Type.PAGE_ALREADY_EXIST, ChatColor.RED + "Page " + ChatColor.WHITE + "\"" + name + "\"" + ChatColor.RED + " already not exists.");
		}
		else {
			pages().set(name, lines);
		}
	}
	
	public void removePage(String name) throws HelpPageException {
		if(!pages().contains(name)) {
			throw new HelpPageException(HelpPageException.Type.PAGE_NOT_EXIST, ChatColor.RED + "Page " + ChatColor.WHITE + "\"" + name + "\"" + ChatColor.RED + " does not exist.");
		}
		else {
			pages().set(name, null);
		}
	}
	
}
