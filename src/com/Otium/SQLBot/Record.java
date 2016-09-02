/**
 * Copyright Otium (c) 2016
 */
package com.Otium.SQLBot;

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
	
	public void addParameter(Field field, Object value){
		Parameters.add(field, value.toString());
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
		public String Value;
		
		public Parameter(Field field, String value){				
			this.Field = field;
			this.Value = value;
		}
		
	}
}
