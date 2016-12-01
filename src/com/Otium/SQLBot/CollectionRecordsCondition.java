package com.Otium.SQLBot;

import java.util.ArrayList;

import com.Otium.SQLBot.Record.Parameter;

public class CollectionRecordsCondition extends ArrayList<RecordsCondition> {
	private static final long serialVersionUID = -2527722864096743032L;
	
	private CollectionRecordsConditionType type;
	
	public CollectionRecordsCondition() {
		type = CollectionRecordsConditionType.AND;
	}

	public boolean add(RecordsCondition condition){
		return super.add(condition);
	}
	
	public boolean add(Record record){
		if(record!=null)
			for(Parameter param : record.getParameters())
				add(param.Field, ConditionType.EQUALS, param.Value);
		return true;
	}
	
	public boolean add(Field field, ConditionType type, String value){
		return super.add(new RecordsCondition(field, type, value));
	}
		
	public boolean add(Field field, ConditionType type, Integer value){
		return super.add(new RecordsCondition(field, type, value.toString()));
	}

	public CollectionRecordsConditionType getType() {
		return type;
	}
}
