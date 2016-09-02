/**
 * Copyright Otium (c) 2016
 */
package com.Otium.SQLBot;

public enum Increment{
	AUTO("AUTOINCREMENT"), 
	NOAUTO("");
	
	private String Value;
	
	private Increment(String value){
		Value = value;
	}
	
	@Override
	public String toString(){
		return Value;
	}
}
