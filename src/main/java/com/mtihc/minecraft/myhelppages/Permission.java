package com.mtihc.minecraft.myhelppages;

/**
 * This enum contains permission nodes that are used by the command executor.
 * 
 * @author Mitch
 *
 */
public enum Permission {
	RELOAD("myhelppages.reload"),
	LIST("myhelppages.list"),
	MAIN("myhelppages"),
	ALLPAGES("myhelppages.allpages");
	
	private String node;

	private Permission(String node) {
		this.node = node;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return node;
	}
	
	/**
	 * Input a page name and this method returns the required permission node.
	 * @param name The page name
	 * @return The permission node
	 */
	public static String convertPageNameToPermission(String name) {
		return MAIN + "." + name;
	}
	
}
