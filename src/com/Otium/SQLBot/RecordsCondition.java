package com.Otium.SQLBot;

public class RecordsCondition {
	public Field field;
	public ConditionType condType;
	public String cond;
	
	RecordsCondition() {}
	
	public RecordsCondition(Field field, ConditionType conditionType, String condition){
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
		      .append(condType);
		
		if(field.isNeedQuotes())
			buffer.append("'")
			      .append(cond.replace("'", "''"))
			      .append("'");
		else
			buffer.append(cond);
		
		return buffer.toString();
	}
}
