package com.mtihc.minecraft.myhelppages;

import java.util.List;
import java.util.Set;

/**
 * This interface is used by the <code>HelpCommandExecutor</code> to send messages and help pages.
 * 
 * @author Mitch
 *
 */
public interface IHelpConfiguration {

	/**
	 * @return the title of a page
	 */
	public abstract String getMessagePageTile();

	/**
	 * @return message to be sent when the player has no permission to view the requested page
	 */
	public abstract String getMessageNoPagePermission();

	/**
	 * @return message to be sent when the requested page was not found
	 */
	public abstract String getMessagePageNotFound();
	
	/**
	 * @param name The page name
	 * @return lines of text on the specified page
	 */
	public abstract List<String> getPage(String name);

	/**
	 * @param name The page name
	 * @return whether the page exists
	 */
	public abstract boolean hasPage(String name);

	/**
	 * @return Set of all page names
	 */
	public abstract Set<String> getPageNames();

	/**
	 * Reloads any changes to the configuration
	 */
	public abstract void reload();

}