package com.Otium.SQLBot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Copyright 2016 Kabanov Gregory 
 */

public abstract class TableObject {
	public static String getGetterName(String field_name) {
		return  "get" + getCapitalize(field_name);
	}

	public static String getSetterName(String field_name) {
		return  "set" + getCapitalize(field_name);
	}

	private static String getCapitalize(String string) {		 
		return string.toUpperCase().substring(0,1) + string.substring(1);
	}

	@SQLBotIgnore private TableFactory<? extends TableObject> Factory;
	
	@SQLBotIgnore public SQLInteger rid; 
	
	private TableObject(){
		rid = new SQLInteger(0);
	}
	
	protected TableObject(TableFactory<? extends TableObject> table){
		this();
		setFactory(table);
	}
	
	public abstract void setListners();

	@SQLBotIgnore 
	public void setFactory(TableFactory<? extends TableObject> table){
		this.Factory = table;		
	}
	
	@SQLBotIgnore
	public void Save(){
		TableObject object;
		for(Field field : Factory.getTable().FieldsOfTable){
			try {
				Method geter = getClass().getMethod(getGetterName(field.Name.toString()));
				Object value = geter.invoke(this);
				if(value != null && 
						   value instanceof SQLTableObject && 
						   (object=((SQLTableObject<? extends TableObject>)value).get())!=null &&
						   !object.isHasRid()) 
							object.Save();
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				if(Factory.getConnection().isDEBUG())
					e.printStackTrace();
			}
		}
			
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

	public TableFactory<? extends TableObject> getTableFactory() {
		return Factory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Factory == null) ? 0 : Factory.hashCode());
		result = prime * result + ((rid == null) ? 0 : Integer.hashCode(rid.get()));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableObject other = (TableObject) obj;
		if (Factory == null) {
			if (other.Factory != null)
				return false;
		} else if (!Factory.equals(other.Factory))
			return false;
		if (rid == null) {
			if (other.rid != null)
				return false;
		} else if (rid.get() != other.rid.get())
			return false;
		return true;
	}
}
