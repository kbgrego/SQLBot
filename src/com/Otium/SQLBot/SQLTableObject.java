package com.Otium.SQLBot;

import javafx.beans.property.SimpleObjectProperty;

public class SQLTableObject<T extends TableObject> extends SimpleObjectProperty<T> implements SQLData{	
	protected SQLTableObject(){
		
	}
	
	protected SQLTableObject(T object){
		this.setValue(object);
	}
	
	@Override
	public final FieldDataType getSQLDataType() {
		return FieldDataType.INTEGER;
	}

	@Override
	public String getQueryValue() {		
		return get()==null ? "0" : get().getRid().getQueryValue();
	}
}
