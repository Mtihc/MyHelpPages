package com.mtihc.minecraft.myhelppages;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * This class can execute the commands of the plugin.
 * 
 * <p>
 * It can be used as a paremeter in the <code>setExecutor()</code> method 
 * of a <code>PluginCommand</code>.
 * </p>
 * 
 * <p>
 * For example:
 * <pre>plugin.getCommand(label).setExecutor(executor);</pre>
 * </p>
 * 
 * @author Mitch
 *
 */
public class HelpCommandExecutor implements CommandExecutor {

	private IHelpConfiguration config;

	/**
	 * Constructor.
	 * 
	 * @param config The configuration for this executor
	 */
	public HelpCommandExecutor(IHelpConfiguration config) {
		this.config = config;
	}
	
	
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		String lbl = label.toLowerCase();
		
		// label must be help
		if(lbl.equalsIgnoreCase("help")) {
			String nested;
			
			// get first argument (could be one of the commands)
			try {
				nested = args[0];
			} catch(Exception e) {
				// there are no arguments, so
				// executed the main command
				help(sender, args);
				return true;
			}
			
			// remove first argument (which is a command label)
			String[] newArgs;
			try {
				newArgs = Arrays.copyOfRange(args, 1, args.length);
			} catch(Exception e) {
				newArgs = new String[0];
			}
			
			
			// 
			// Find and execute nested command. 
			// Use the newArgs, so that it doesn't include
			// the label of the nested command. 
			// 
			if(nested.equalsIgnoreCase("-list")) {
				list(sender, newArgs);
			}
			else if(nested.equalsIgnoreCase("-reload")) {
				reload(sender, newArgs);
			}
			else if(nested.equals("?")) {
				commandHelp(sender, newArgs);
			}
			else {
				// didn't execute a nested command
				
				// So don't use newArgs here.
				// The first argument is probably a page name!
				help(sender, args);
			}
			return true;
			
		}
		else {
			// we don't know this command label
			return false;
		}
	}
	
	/**
	 * Send command help
	 * @param sender The command sender
	 * @param args The commnd arguments
	 */
	public void commandHelp(CommandSender sender, String[] args) {
		sender.sendMessage(ChatColor.GREEN + "Command list:");
		
		sender.sendMessage(ChatColor.WHITE + "/help" + ChatColor.DARK_GREEN + " See the main help page.");
		sender.sendMessage(ChatColor.WHITE + "/help [page name]" + ChatColor.DARK_GREEN + " See a help page.");
		sender.sendMessage(ChatColor.WHITE + "/help -list [number]" + ChatColor.DARK_GREEN + " List all help pages.");
		sender.sendMessage(ChatColor.WHITE + "/help -reload" + ChatColor.DARK_GREEN + " Reload the configuration.");
		
	}
	
	/**
	 * The help command. Shows a help page to the command sender.
	 * @param sender The command sender
	 * @param args The command arguments
	 */
	public void help(CommandSender sender, String[] args) {

		// get page name from aguments
		String name = convertArgsToPageName(args);

		// replace - with space
		String userFriendlyName = name.replace("-", " ");
		
		// check existance
		if(!config.hasPage(name)) {
			// send configured error message
			String msg = config.getMessagePageNotFound();
			msg = convertColors(msg);
			msg = msg.replace("%page%", userFriendlyName);
			sender.sendMessage(msg);
			return;
		}
		
		// get permission by page name
		String perm = Permission.convertPageNameToPermission(name);

		// check permission
		if(!sender.hasPermission(Permission.ALLPAGES.toString()) && !sender.hasPermission(perm)) {
			// send configured error message
			String msg = config.getMessageNoPagePermission();
			msg = convertColors(msg);
			msg = msg.replace("%page%", userFriendlyName);
			sender.sendMessage(msg);
			return;
		}
		
		
		List<String> lines = config.getPage(name);
		
		// send configured title
		String title = config.getMessagePageTile();
		title = convertColors(title);
		title = title.replace("%page%", userFriendlyName);
		sender.sendMessage(title);
		
		// send lines of page
		for (String line : lines) {
			line = convertColors(line);
			line = line.replace("%page%", userFriendlyName);
			sender.sendMessage(line);
		}
	}

	/**
	 * The list command. Send a list of names of all pages that the command sender has permission for.
	 * @param sender The command sender
	 * @param args The command arguments
	 */
	public void list(CommandSender sender, String[] args) {

		if(!sender.hasPermission(Permission.LIST.toString())) {
			sender.sendMessage(ChatColor.RED + "You don't have permission for the list command.");
			return;
		}
		
		int pageNumber;
		try {
			pageNumber = Integer.parseInt(args[0]);
		} catch(NullPointerException e) {
			// args is null
			pageNumber = 1;
		} catch(IndexOutOfBoundsException e) {
			// args is empty
			pageNumber = 1;
		} catch(NumberFormatException e) {
			// args[0] is not a number
			sender.sendMessage(ChatColor.RED + "Expected a page number.");
			return;
		}
		
		if(sender.hasPermission(Permission.ALLPAGES.toString())) {
			// sender has permission for all pages
			
			// send list of pages
			sendPageList(sender, pageNumber, config.getPageNames());
		}
		else {
			// sender has permission for some pages
			
			// only show the pages he has permission for
			Set<String> original = config.getPageNames();
			HashSet<String> pagesWithPermission = new HashSet<String>();
			for (String pageName : original) {
				String perm = Permission.convertPageNameToPermission(pageName);
				if(sender.hasPermission(perm)) {
					pagesWithPermission.add(pageName);
				}
			}
			
			// send list of pages
			sendPageList(sender, pageNumber, pagesWithPermission);
		}
	}

	/**
	 * The reload command. Reload the configuration files.
	 * @param sender The command sender.
	 * @param args The command arguments.
	 */
	public void reload(CommandSender sender, String[] args) {

		if(!sender.hasPermission(Permission.RELOAD.toString())) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to reload the configuration.");
			return;
		}
		
		config.reload();
		sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
		
	}

	/**
	 * Used by list command. Send a list of page names to the command sender. 
	 * @param sender The command sender
	 * @param page The page number for the list of names
	 * @param names The list of names
	 */
	private static void sendPageList(CommandSender sender, int page, Set<String> names) {
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
		sender.sendMessage(ChatColor.GREEN + "Next page: " + ChatColor.WHITE + "/help -list " + nextPage);
	}
	
	

	/**
	 * Replace variable names for colors, to actual color values in a string.
	 * @param source The string with color variable names
	 * @return The colored string
	 */
	private static String convertColors(String source) {
		String result = source;
		ChatColor[] values = ChatColor.values();
		// iterate over chat colors
		for (ChatColor color : values) {
			// get normal name of color
			String name = color.name().replace("_", "").toLowerCase();
			// replace variable name with real color
			result = result.replace("%" + name + "%", color.toString());
		}
		return result;
	}
	
	/**
	 * Used to convert a list of strings to a page name. 
	 * @param args The arguments passed in a command
	 * @return The page name
	 */
	private static String convertArgsToPageName(String[] args) {
		// put a dash(-) inbetween the arguments
		String result = "";
		if(args != null) {
			for (String arg : args) {
				result += arg + "-";
			}
		}
		
		
		if(!result.isEmpty()) {
			// remove that last dash
			result = result.substring(0, result.length() - 1);
			
			
			return "help-" + result;
		}
		else {
			return "help";
		}
	}
}
