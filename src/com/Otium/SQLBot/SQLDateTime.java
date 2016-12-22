package com.Otium.SQLBot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleObjectProperty;

public class SQLDateTime extends SimpleObjectProperty<LocalDateTime> implements SQLData, Comparable<SQLDateTime>{
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	protected SQLDateTime(){
		this.setValue(LocalDateTime.MIN);
	}
	
	protected SQLDateTime(String datetime){
		this.setValue(LocalDateTime.parse(datetime, formatter));
	}
	
	public SQLDateTime(LocalDateTime datetime) {
		this.setValue(datetime);
	}

	@Override
	public final FieldDataType getSQLDataType() {
		return FieldDataType.DATETIME;
	}

	@Override
	public String getQueryValue() {
		return "'" + get().format(formatter) + "'";
	}
	
	public String toString(){
		return get().format(formatter);
	}

	@Override
	public int compareTo(SQLDateTime o) {
		return get().compareTo(o.get());
	}
}
