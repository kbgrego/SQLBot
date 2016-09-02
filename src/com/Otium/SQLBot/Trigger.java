/**
 * Copyright Otium (c) 2016
 */
package com.Otium.SQLBot;

import com.Otium.SQLBot.Table.TABLE;

/**
 * Class to manage the trigger in database.<br>
 */ //TODO Create methods to build content of trigger.
class Trigger{
	public  String  TriggerContent;
	
	ConnectDatabase Database;
	String          TriggerName;	
	TriggerAction   TriggerAction;	
	TABLE           TableName;
	
	Trigger(String triggerName, TriggerAction triggerAction, TABLE tableName){
		//initialization
		this.TriggerName   = triggerName;
		this.TriggerAction = triggerAction;
		this.TableName     = tableName;
		this.TriggerContent= new String();
		
	}
	
	public void Create(){
		Database.executeQuery(getQueryToDoCreate());
	}
	
	private String getQueryToDoCreate(){		
		StringBuffer query = new StringBuffer();
		
		query.append("CREATE TRIGGER IF NOT EXISTS ") 
			 .append("'").append(this.TriggerName).append("' ")			 
			 .append(this.TriggerAction)
			 .append(" ON ")
			 .append("`").append(this.TableName).append("`")
			 
		     .append(" BEGIN ")				
		     .append( TriggerContent )		     			
		     .append(" END; ");  
			
		return query.toString();	
	}
}