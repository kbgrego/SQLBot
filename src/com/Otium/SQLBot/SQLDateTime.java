package com.Otium.SQLBot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleObjectProperty;

public class SQLDateTime extends SimpleObjectProperty<LocalDateTime> implements SQLData, Comparable<SQLDateTime>{
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final DateTimeFormatter short_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	protected SQLDateTime(){
		this.setValue(LocalDateTime.MIN);
	}
	
	protected SQLDateTime(String datetime){
		if(datetime.matches("(\\d{4})-(\\d{2})-(\\d{2})\\s(\\d{2})\\:(\\d{2})\\:(\\d{2})"))
			this.setValue(LocalDateTime.parse(datetime, formatter));
		else
			this.setValue(LocalDateTime.parse(datetime + " 00:00:00", formatter));
	}
	
	public SQLDateTime(LocalDate date) {		
		this.setValue(LocalDateTime.parse(date.format(short_formatter)+ " 00:00:00", formatter));
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
		if(get()!=null)
			return "'" + get().format(formatter) + "'";
		else
			return "''";
	}
	
	public String toString(){
		return get().format(formatter);
	}

	@Override
	public int compareTo(SQLDateTime o) {
		return get().compareTo(o.get());
	}
}
