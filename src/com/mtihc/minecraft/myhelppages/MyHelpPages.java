package com.mtihc.minecraft.myhelppages;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.minecraft.myhelppages.commands.HelpCommand;

public class MyHelpPages extends JavaPlugin {

	private ConfigYaml config;
	private HelpCommand command;

	public ConfigYaml getConfigYaml() {
		return config;
	}
	
	public static String convertColors(String source) {
		String result = source;
		ChatColor[] values = ChatColor.values();
		for (ChatColor color : values) {
			String name = color.name().replace("_", "").toLowerCase();
			result = result.replace("%" + name + "%", color.toString());
		}
		return result;
	}
	
	public static String convertArgsToPageName(String[] args) {
		String result = "";
		for (String arg : args) {
			result += arg + "-";
		}
		if(!result.isEmpty()) {
			return "help-" + result.substring(0, result.length() - 1);
		}
		else {
			return "help";
		}
	}
	
	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command originalCommand,
			String label, String[] args) {
		if(label.equalsIgnoreCase(command.getLabel()) || command.getAliases().contains(label.toLowerCase())) {
			command.execute(sender, label.toLowerCase(), args);
			return true;
		}
		else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
	@Override
	public void onDisable() {
		getLogger().info("disabled");
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onEnable() {
		
		config = new ConfigYaml(this);
		config.reload();
		
		command = new HelpCommand(this);
		
		getCommand("help").setExecutor(this);
		
		getLogger().info("version " + getDescription().getVersion() + " enabled");
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#getConfig()
	 */
	@Override
	public FileConfiguration getConfig() {
		return config.getConfig();
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#reloadConfig()
	 */
	@Override
	public void reloadConfig() {
		config.reload();
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#saveConfig()
	 */
	@Override
	public void saveConfig() {
		config.save();
	}

	
}
