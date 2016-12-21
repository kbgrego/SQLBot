package com.Otium.SQLBot;

/**
 * Copyright 2016 Kabanov Gregory 
 */

public abstract class TableObject implements SQLData {
	@SQLBotIgnore private TableFactory<?> Factory;
	
	@SQLBotIgnore public SQLInteger rid; 
	
	private TableObject(){
		rid = new SQLInteger(0);
	}
	
	protected TableObject(TableFactory<?> table){
		this();
		setFactory(table);
	}
	
	public abstract void setListners();

	@SQLBotIgnore 
	public void setFactory(TableFactory<?> table){
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
	
	public void setRid(Integer rid){
		this.rid.set(rid);
	}
	
	public SQLInteger getRid(){
		return rid;
	}
	
	public boolean isHasRid(){
		return rid.get()!=0;
	}

	public void setRid(SQLInteger lastCreatedIndex) {		
		rid.set(lastCreatedIndex.get());
	}

	public TableFactory<?> getTableFactory() {
		return Factory;
	}
	
	@Override
	public final FieldDataType getSQLDataType(){
		return FieldDataType.INTEGER;
	}
	
	@Override
	public String getQueryValue() {
		return rid.getQueryValue();
	}
}
