package com.Otium.SQLBot;

public class NotSQLDataException extends Exception {

	public NotSQLDataException(String string) {
		super(string + " isn't implements SQLData");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -567496853725942315L;

}
