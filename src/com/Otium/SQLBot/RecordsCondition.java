package com.Otium.SQLBot;

public class RecordsCondition {
	public Field field;
	public ConditionType condType;
	public SQLData cond;
	
	RecordsCondition() {}
	
	public RecordsCondition(Field field, ConditionType conditionType, SQLData condition){
		this.field    = field;
		this.condType = conditionType;
		this.cond     = condition;
	}
	
	@Override
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("`")
		      .append(field.Name)
		      .append("`")
		      .append(condType)		
		      .append(cond.getQueryValue());
		
		return buffer.toString();
	}
}
