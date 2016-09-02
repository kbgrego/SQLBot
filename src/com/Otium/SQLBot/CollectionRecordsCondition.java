package com.Otium.SQLBot;

import java.util.ArrayList;

public class CollectionRecordsCondition extends ArrayList<RecordsCondition> {
	private static final long serialVersionUID = -2527722864096743032L;

	public boolean add(RecordsCondition condition){
		return super.add(condition);
	}
	
	public boolean add(Field field, ConditionType type, String value){
		return super.add(new RecordsCondition(field, type, value));
	}
		
	public boolean add(Field field, ConditionType type, Integer value){
		return super.add(new RecordsCondition(field, type, value.toString()));
	}
}
