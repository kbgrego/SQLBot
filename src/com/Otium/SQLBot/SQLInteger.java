package com.Otium.SQLBot;

import javafx.beans.property.SimpleIntegerProperty;

public class SQLInteger extends SimpleIntegerProperty implements SQLData, Comparable<SQLInteger>{

	protected SQLInteger(){
		this.add(0);
	}
	
	public SQLInteger(Integer object) {
		this.setValue(object);
	}

	@Override
	public FieldDataType getSQLDataType() {
		return FieldDataType.INTEGER;
	}

	@Override
	public String getQueryValue() {
		return Integer.toString(this.get());
	}
	
	public String toString(){
		return Integer.toString(get());
	}

	@Override
	public int compareTo(SQLInteger o) {
		return Integer.compare(get(), o.get());
	}
	
}
