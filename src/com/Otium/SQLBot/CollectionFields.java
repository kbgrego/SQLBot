package com.Otium.SQLBot;

import java.util.ArrayList;

import com.Otium.SQLBot.Field.FIELD;

public class CollectionFields extends ArrayList<Field> {
	private static final long serialVersionUID = 1031385511086521781L;

	public boolean add(FIELD nameOfTable, FieldDataType typeOfField, Key keyOfField, Increment increment){
		Field field = new Field(nameOfTable, typeOfField, keyOfField, increment);
		return super.add(field);
	}
}
