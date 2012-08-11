package com.mtihc.minecraft.myhelppages.exceptions;

/**
 * This class represents an exception that is usually thrown when 
 * when a page is requested, but it doesn't exist. 
 * Or when a page is added and it already exists.
 * 
 * @author Mitch
 *
 */
public class HelpPageException extends Exception {

	private static final long serialVersionUID = 5189176008676068303L;

	/**
	 * The type of exception
	 * 
	 * @author Mitch
	 *
	 */
	public enum Type {
		PAGE_NOT_EXIST("That page does not exist."),
		PAGE_ALREADY_EXIST("That page already exists.");
		
		private String message;

		private Type(String message) {
			this.message = message;
		}
		
		public String getMessage() {
			return message;
		}
	}
	
	private Type type;
	
	public HelpPageException(Type type) {
		super(type.getMessage());
		this.type = type;
	}
	
	public HelpPageException(Type type, String msg) {
		super(msg);
		this.type = type;
	}

	public HelpPageException(Type type, Throwable cause) {
		super(type.getMessage(), cause);
		this.type = type;
	}

	public HelpPageException(Type type, String msg, Throwable cause) {
		super(msg, cause);
		this.type = type;
	}

	public Type getType() {
		return type;
	}
}
