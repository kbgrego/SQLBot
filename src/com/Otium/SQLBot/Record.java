/**
 * Copyright Otium (c) 2016
 */
package com.Otium.SQLBot;

import java.time.LocalDateTime;

/**
 * Class to manage records in table.
 */
public class Record{
	private CollectionParameters Parameters;
	
	public Record(){
		Parameters = new CollectionParameters();
	}
	
	public Parameter getParameterByField(Field field) throws FieldNotFoundException{
		for(Parameter p : Parameters)
			if(p.Field==field)
				return p;
		throw new FieldNotFoundException(); 
	}
	
	public void addParameter(Field field, SQLData value){
		if(value!=null)
			Parameters.add(field, value);
	}
	
	public CollectionParameters getParameters(){
		return Parameters;
	}
	
	@Override
	public String toString(){
		
		StringBuffer values = new StringBuffer();
		
		for(Parameter p : Parameters)
			values.append(p.Value + " ");
		
		return values.toString();
		
	}
	
	public void addParameter(Parameter parameter) {
		Parameters.add(parameter);		
	}

	public void removeParameter(Field field) {
		for(Parameter parameter :  Parameters)
			if(parameter.Field == field){
				Parameters.remove(parameter);
				break;
			}
	}

	/**
	 * It contains field and value.
	 */
	public static class Parameter{
		
		public Field Field;
		public SQLData Value;
		
		public Parameter(Field field, SQLData value){				
			this.Field = field;
			this.Value = value;
		}
		
	}

	protected void addParameter(Field field, Object object) {
		if (object == null)
			switch (field.Type) {
				case INTEGER:  addParameter(field, new SQLInteger((Integer) object));break;
				case BLOB:     addParameter(field, new SQLBlob());break;
				case DATETIME: addParameter(field, new SQLDateTime((LocalDateTime) null));break;
				case REAL:     addParameter(field, new SQLReal());break;
				case TEXT:     addParameter(field, new SQLText(null));break;
			}
		else if (object instanceof Integer)
			addParameter(field, new SQLInteger((Integer) object));
		else if (object instanceof byte[])
			addParameter(field, new SQLBlob((byte[]) object));
		else if (object instanceof SQLData)
			addParameter(field, (SQLData) object);
		else {
			if(object.toString().matches("(\\d{4})-(\\d{2})-(\\d{2})\\s(\\d{2})\\:(\\d{2})\\:(\\d{2})"))
				addParameter(field, new SQLDateTime(object.toString()));
			else
				addParameter(field, new SQLText(object.toString()));			
		}
	}
}
