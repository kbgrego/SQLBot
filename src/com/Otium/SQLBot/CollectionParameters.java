package com.Otium.SQLBot;

import java.util.ArrayList;

import com.Otium.SQLBot.Record.Parameter;

public class CollectionParameters extends ArrayList<Parameter> {
	private static final long serialVersionUID = 7845799382199333295L;

	public boolean add(Parameter parameter){
		return super.add(parameter);		
	}
		
	public boolean add(Field field, String value){
		Parameter param = new Parameter(field, value);
		return super.add(param);		
	}
}
