package com.mtihc.minecraft.myhelppages.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.mtihc.minecraft.core1.BukkitCommand;
import com.mtihc.minecraft.myhelppages.MyHelpPages;
import com.mtihc.minecraft.myhelppages.Permission;

public class HelpReloadCommand extends BukkitCommand {

	private MyHelpPages plugin;
	
	public HelpReloadCommand(MyHelpPages plugin) {
		super("-reload", "Reload the configuration file after you have made changes.", "", null);
		this.plugin = plugin;
		
		ArrayList<String> aliases = new ArrayList<String>();
		aliases.add("-r");
		aliases.add("-rel");
		setAliases(aliases);
		
		setPermission(Permission.RELOAD.toString());
		setPermissionMessage(ChatColor.RED + "You don't have permission to reload the configuration.");
	}

	/* (non-Javadoc)
	 * @see com.mtihc.minecraft.core1.BukkitCommand#execute(org.bukkit.command.CommandSender, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(super.execute(sender, label, args)) {
			return true;
		}
		
		if(!testPermission(sender)) {
			return false;
		}
		
		plugin.getConfigYaml().reload();
		sender.sendMessage(ChatColor.GREEN + "Reloaded configuration of " + ChatColor.WHITE + plugin.getDescription().getFullName() + ChatColor.GREEN + ".");
		
		return true;
	}

	
}
