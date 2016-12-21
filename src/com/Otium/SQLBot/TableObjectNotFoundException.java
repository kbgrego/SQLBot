package com.Otium.SQLBot;

import com.Otium.SQLBot.Table.TABLE;

public class TableObjectNotFoundException extends Exception {

	public TableObjectNotFoundException(TABLE nameOfTable, int i) {
		super("not found table object with rid " + i + " in " + nameOfTable);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6546487540105611804L;

}
