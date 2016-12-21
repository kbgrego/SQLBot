/**
 * Copyright Otium (c) 2016
 */
package com.Otium.SQLBot;

public class Field{
	public FIELD Name;
	public FieldDataType Type;
	Key Key;
	Increment Increment;
	
	public Field(FIELD nameOfTable, FieldDataType typeOfField, Key keyOfField, Increment increment){
		this.Name = nameOfTable;
		this.Type = typeOfField;
		this.Key = keyOfField;
		this.Increment = increment;
	}
	
	String getCreatingString(){
		return "`" + this.Name + "` " + 
		             this.Type + " " + 
				     this.Key  + " " + 
		             this.Increment;		
	}
	
	public boolean isNeedQuotes(){
		return this.Type==FieldDataType.TEXT  ||  this.Type==FieldDataType.DATETIME || this.Type==FieldDataType.BLOB;
	}
	
	@Override
	public String toString(){
		return getCreatingString();
	}
	
	public static class FIELD{
		private String NameOfField;

		public FIELD(String nameOfField){
			if(nameOfField!=null)
				NameOfField = nameOfField;
			else
				NameOfField = "";
		}
		
		@Override
		public String toString() {
			return NameOfField;			
		}
	}
}
