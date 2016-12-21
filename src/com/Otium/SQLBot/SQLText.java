package com.Otium.SQLBot;

import javafx.beans.property.SimpleStringProperty;

public class SQLText extends SimpleStringProperty implements SQLData{

	protected SQLText(){
		this("");
	}
	
	public SQLText(String object) {
		this.setValue(object);
	}

	@Override
	public FieldDataType getSQLDataType() {
		return FieldDataType.TEXT;
	}

	@Override
	public String getQueryValue() {
		return "'" + get().replace("'", "''") + "'";
	}
	
}
