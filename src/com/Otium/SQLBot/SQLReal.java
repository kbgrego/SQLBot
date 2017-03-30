package com.Otium.SQLBot;

import javafx.beans.property.SimpleDoubleProperty;

public class SQLReal extends SimpleDoubleProperty implements SQLData, Comparable<SQLReal>{

	public SQLReal() {
		super();
	}

	public SQLReal(double parseDouble) {
		super();
		set(parseDouble);
	}

	@Override
	public FieldDataType getSQLDataType() {
		return FieldDataType.REAL;
	}

	@Override
	public String getQueryValue() {
		return Double.toString(get());
	}

	public String toString(){
		return Double.toString(get());
	}

	@Override
	public int compareTo(SQLReal o) {
		return Double.compare(get(), o.get());
	}
}
