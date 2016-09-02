package com.Otium.SQLBot;

/** 
 *  Types:  
 *  <br>
 *  <br>BIGGEST            - '>'  
 *	<br>SMOLLER            - '<' 
 *	<br>EQUALS             - '=' 
 *	<br>BBIGGEST_OR_EQUALS - '>=' 
 * 	<br>SMOLLER_OR_EQUALS  - '<=' 
 *	<br>NOT_EQUALS         - '<>' 
 *	<br>LIKE               - 'LIKE'
 */
public enum ConditionType {
	BIGGEST(">"), 
	SMOLLER("<"), 
	EQUALS("="), 
	BBIGGEST_OR_EQUALS(">="), 
	SMOLLER_OR_EQUALS("<="), 
	NOT_EQUALS("<>"), 
	LIKE("LIKE");
	
	private String Value;
	
	ConditionType(String value){ 
		this.Value = value; 
	}
	
	@Override
	public String toString(){ 
		return Value; 
	}
}
