package com.mtihc.minecraft.myhelppages.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.mtihc.minecraft.core1.BukkitCommand;
import com.mtihc.minecraft.myhelppages.MyHelpPages;
import com.mtihc.minecraft.myhelppages.Permission;

public class HelpListCommand extends BukkitCommand {

	private MyHelpPages plugin;

	public HelpListCommand(MyHelpPages plugin) {
		super("-list", "List all help pages", "[list page number]", null);
		this.plugin = plugin;
		
		ArrayList<String> aliases = new ArrayList<String>();
		aliases.add("-l");
		setAliases(aliases);
		
		setPermission(Permission.LIST.toString());
		setPermissionMessage(ChatColor.RED + "You don't have permission for the list command.");
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
		
		int pageNumber;
		try {
			pageNumber = Integer.parseInt(args[0]);
		} catch(NullPointerException e) {
			pageNumber = 1;
		} catch(IndexOutOfBoundsException e) {
			pageNumber = 1;
		} catch(NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "Expected a number.");
			sender.sendMessage(getUsage());
			return false;
		}
		
		if(sender.hasPermission(Permission.ALLPAGES.toString())) {
			// sender has permission for all pages
			
			// send list of pages
			sendAllPages(sender, pageNumber, plugin.getConfigYaml().getPageNames());
		}
		else {
			// sender has permission for some pages
			
			// only show the pages he has permission for
			Set<String> original = plugin.getConfigYaml().getPageNames();
			HashSet<String> pagesWithPermission = new HashSet<String>();
			for (String pageName : original) {
				String perm = Permission.convertPageNameToPermission(pageName);
				if(!sender.hasPermission(perm)) {
					continue;
				}
				pagesWithPermission.add(pageName);
			}
			
			// send list of pages
			sendAllPages(sender, pageNumber, pagesWithPermission);
		}
		return true;
	}
	
	private void sendAllPages(CommandSender sender, int page, Set<String> names) {
		int total = names.size();
		int totalPerPage = 10;
		int totalPages = total / totalPerPage + 1;
		int startIndex = (page - 1) * totalPerPage;
		int endIndex = startIndex + totalPerPage;
		
		sender.sendMessage(ChatColor.GREEN + "List of all help pages " + ChatColor.WHITE + "(" + page + "/" + totalPages + ")" + ChatColor.GREEN + ":");
		
		String[] nameArray = names.toArray(new String[names.size()]);
		
		for(int i = startIndex; i < endIndex && i < total; i++) {
			String userFriendly = "/" + nameArray[i].replace("-", " ");
			sender.sendMessage(ChatColor.DARK_GRAY + " " + i + ". " + ChatColor.WHITE + userFriendly);
		}
		String nextPage;
		if(page == totalPages) {
			nextPage = "1";
		}
		else {
			nextPage = String.valueOf(page + 1);
		}
		sender.sendMessage(ChatColor.GREEN + "Next page: " + ChatColor.WHITE + getUsage().replace("[list page number]", nextPage));
	}

}
