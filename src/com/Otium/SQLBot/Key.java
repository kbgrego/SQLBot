/**
 * Copyright Otium (c) 2016
 */
package com.Otium.SQLBot;

public enum Key {
	PRIMARY("PRIMARY KEY"),
	UNIQUE("UNIQUE"),
	NONE("");
	
	private String Value;
	Key(String value){
		Value = value;
	}
	
	@Override
	public String toString(){
		return Value;
	}
}
