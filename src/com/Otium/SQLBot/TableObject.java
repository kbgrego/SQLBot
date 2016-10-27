package com.Otium.SQLBot;

/**
 * Copyright 2016 Kabanov Gregory 
 */

public abstract class TableObject {
	@SQLBotIgnore private TableFactory<? super TableObject> Factory;
	
	@SQLBotIgnore private Integer Rid; 
	
	public abstract void setListners();

	@SQLBotIgnore 
	public void setFactory(TableFactory<? super TableObject> table){
		this.Factory = table;		
	}
	
	@SQLBotIgnore
	public void Save(){
		if(Rid==0)
			Factory.Create(this);
		else
			Factory.Update(this);
	}
	
	@SQLBotIgnore
	public void Delete(){
		if(Rid!=0)
			Factory.Delete(this);
	}
	
	public void setRid(String rid){
		Rid = Integer.parseInt(rid);
	}
	
	public String getRid(){
		return Rid.toString();
	}
	
}
