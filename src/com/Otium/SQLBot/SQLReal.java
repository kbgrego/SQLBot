package com.Otium.SQLBot;

import javafx.beans.property.SimpleDoubleProperty;

public class SQLReal extends SimpleDoubleProperty implements SQLData {

	@Override
	public FieldDataType getSQLDataType() {
		return FieldDataType.REAL;
	}

	@Override
	public String getQueryValue() {
		return Double.toString(get());
	}

}
