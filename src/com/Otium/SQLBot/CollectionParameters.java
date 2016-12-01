package com.Otium.SQLBot;

import java.util.ArrayList;

import com.Otium.SQLBot.Record.Parameter;

public class CollectionParameters extends ArrayList<Parameter> {
	private static final long serialVersionUID = 7845799382199333295L;
	private static final int FIRST_INDEX = 0;

	public boolean add(Parameter parameter){
		return super.add(parameter);		
	}
		
	public boolean add(Field field, String value){
		Parameter param = new Parameter(field, value);
		return super.add(param);		
	}
	
	public String getQuerySequenceOfFields(){
		StringBuffer sequence = new StringBuffer();
		for(Parameter parameter : this){
			if( indexOf(parameter) != FIRST_INDEX )
				sequence.append(",");
			
			sequence.append("`")
			        .append(parameter.Field.Name)
			        .append("`");
		}
		return sequence.toString();
	}
	
	public String getQuerySequenceOfValues(){
		StringBuffer sequence = new StringBuffer();
		for(Parameter parameter : this){
			if( indexOf(parameter) != FIRST_INDEX )
				sequence.append(",");
			
			if(parameter.Field.Type==FieldDataType.TEXT||parameter.Field.Type==FieldDataType.DATETIME)
				sequence.append("'")
				        .append(parameter.Value.replace("'", "''"))
				        .append("'");
			else
				sequence.append(parameter.Value);
		}
		return sequence.toString();
	}
	
	protected String getQuerySequenceOfFieldsSetValue() {
		StringBuffer sequence = new StringBuffer();
		for(Parameter parameter : this){
			if( indexOf(parameter) != FIRST_INDEX )
				sequence.append(", ");
			
			sequence.append("`")
	        		.append(parameter.Field.Name) 
	        		.append("` = ");
			
			if(parameter.Field.isNeedQuotes())
				sequence.append("'")
				        .append(parameter.Value.replace("'", "''")) 
				        .append("'");
			else
				sequence.append(parameter.Value);
		}
		
		return sequence.toString();
	}
}

