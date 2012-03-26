package com.mtihc.minecraft.myhelppages.exceptions;

public class MyHelpPageException extends Exception {

	private static final long serialVersionUID = 5189176008676068303L;

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
	
	public MyHelpPageException(Type type) {
		super(type.getMessage());
		this.type = type;
	}
	
	public MyHelpPageException(Type type, String msg) {
		super(msg);
		this.type = type;
	}

	public MyHelpPageException(Type type, Throwable cause) {
		super(type.getMessage(), cause);
		this.type = type;
	}

	public MyHelpPageException(Type type, String msg, Throwable cause) {
		super(msg, cause);
		this.type = type;
	}

	public Type getType() {
		return type;
	}
}
