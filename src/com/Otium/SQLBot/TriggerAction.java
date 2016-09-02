/**
 * Copyright Otium (c) 2016
 */
package com.Otium.SQLBot;

/**
 * Specified type of action in trigger.
 * @author Kabanov Gregory
 */
public enum TriggerAction{
	
	/**
	 * Trigger on insert. 
	 */
	INSERT,
	
	/**
	 * Trigger of update.
	 */
	UPDATE,
	
	/**
	 * Trigger on delete.
	 */
	DELETE
}
