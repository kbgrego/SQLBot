package com.Otium.SQLBot;

import javafx.beans.property.SimpleIntegerProperty;

public class SQLInteger extends SimpleIntegerProperty implements SQLData{

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

}
