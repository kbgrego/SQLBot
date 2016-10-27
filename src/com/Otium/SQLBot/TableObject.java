package com.Otium.SQLBot;

/**
 * Copyright 2016 Kabanov Gregory 
 */

public abstract class TableObject {
	@SQLBotIgnore private TableFactory<? extends TableObject> Factory;
	
	@SQLBotIgnore private Integer Rid = 0; 
	
	protected TableObject(){
		
	}
	
	public TableObject(TableFactory<? extends TableObject> table){
		setFactory(table);
	}
	
	public abstract void setListners();

	@SQLBotIgnore 
	public void setFactory(TableFactory<? extends TableObject> table){
		this.Factory = table;		
	}
	
	@SQLBotIgnore
	public void Save(){
		if(isHasRid())
			Factory.Update(this);
		else
			Factory.Create(this);
	}
	
	@SQLBotIgnore
	public void Delete(){
		if(isHasRid())
			Factory.Delete(this);
	}
	
	public void setRid(String rid){
		Rid = Integer.parseInt(rid);
	}
	
	public String getRid(){
		return Rid.toString();
	}
	
	public boolean isHasRid(){
		return Rid!=0;
	}
	
}
