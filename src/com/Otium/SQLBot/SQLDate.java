package com.Otium.SQLBot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleObjectProperty;

public class SQLDate extends SimpleObjectProperty<LocalDate> implements SQLData, Comparable<SQLDate>{
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	protected SQLDate(){
		set(LocalDate.MIN);
	}
	
	protected SQLDate(String date){
		set(date);
	}
	
	public SQLDate(LocalDate date) {		
		set(date);
	}

	@Override
	public final FieldDataType getSQLDataType() {
		return FieldDataType.DATE;
	}

	@Override
	public String getQueryValue() {
		if(get()!=null)
			return "'" + get().format(formatter) + "'";
		else
			return "''";
	}
		
	public void set(String date) {
		if(date.matches("(\\d{4})-(\\d{2})-(\\d{2})"))			
			super.set(LocalDate.parse(date, formatter));
	}

	public String toString(){
		return get().format(formatter);
	}

	@Override
	public int compareTo(SQLDate o) {
		return get().compareTo(o.get());
	}
}
