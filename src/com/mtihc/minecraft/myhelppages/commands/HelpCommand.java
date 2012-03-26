package com.mtihc.minecraft.myhelppages.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.mtihc.minecraft.core1.BukkitCommand;
import com.mtihc.minecraft.myhelppages.MyHelpPages;
import com.mtihc.minecraft.myhelppages.Permission;

public class HelpCommand extends BukkitCommand {

	private MyHelpPages plugin;

	public HelpCommand(MyHelpPages plugin) {
		super("help", "Shows help about a subject", "[page name]", null);
		this.plugin = plugin;
		
		
		
		BukkitCommand reload = new HelpReloadCommand(plugin);
		BukkitCommand list = new HelpListCommand(plugin);
		
		addNested(reload, plugin.getServer());
		addNested(list, plugin.getServer());
		
		ArrayList<String> help = new ArrayList<String>();
		
		help.add(getDescription());
		
		help.add(ChatColor.GREEN + "Nested commands:");
		
		help.add(reload.getUsage());
		help.add(list.getUsage());
	}

	/* (non-Javadoc)
	 * @see com.mtihc.minecraft.core1.BukkitCommand#execute(org.bukkit.command.CommandSender, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(super.execute(sender, label, args)) {
			return true;
		}
		
		String name = MyHelpPages.convertArgsToPageName(args);

		String userFriendlyName = name.replace("-", " ");
		
		// check existance
		if(!plugin.getConfigYaml().hasPage(name)) {
			String msg = plugin.getConfigYaml().getMessagePageNotFound();
			msg = MyHelpPages.convertColors(msg);
			msg = msg.replace("%page%", userFriendlyName);
			sender.sendMessage(msg);
			return false;
		}
		
		
		String perm = Permission.convertPageNameToPermission(name);

		// check permission
		if(!sender.hasPermission(Permission.ALLPAGES.toString()) && !sender.hasPermission(perm)) {
			String msg = plugin.getConfigYaml().getMessageNoPagePermission();
			msg = MyHelpPages.convertColors(msg);
			msg = msg.replace("%page%", userFriendlyName);
			sender.sendMessage(msg);
			return false;
		}
		
		
		List<String> lines = plugin.getConfigYaml().getPage(name);
		
		// send title
		String title = plugin.getConfigYaml().getMessagePageTile();
		title = MyHelpPages.convertColors(title);
		title = title.replace("%page%", userFriendlyName);
		sender.sendMessage(title);
		
		// send lines of page
		for (String line : lines) {
			line = MyHelpPages.convertColors(line);
			line = line.replace("%page%", userFriendlyName);
			sender.sendMessage(MyHelpPages.convertColors(line));
		}
		return true;
	}

	
}
