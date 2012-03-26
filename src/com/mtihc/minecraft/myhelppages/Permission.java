package com.mtihc.minecraft.myhelppages;


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
	
	public static String convertPageNameToPermission(String name) {
		return MAIN + "." + name;
	}
	
}
