/**
 * Copyright 2016 Kabanov Gregory 
 */

package com.Otium.SQLBot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.Otium.SQLBot.Record.Parameter;

public class TableSelector<T extends TableObject> {
	private TableFactory<T> factory;
	private List<T> patterns;	
	
	public TableSelector(TableFactory<T> factory) {
		this.patterns = new ArrayList<>();		
		this.factory = factory;
	}
		
	public void	addPattern(T pattern){
		patterns.add(pattern);
	}
	
	public List<T> getList(){
		return getList(0);
	}
	
	public List<T> getList(Integer limit){
		List<T> list = new ArrayList<T>();
					
		List<Record> data = factory.getTable().Select(getConditions(), limit);
		
		for(Record row : data){
			try {
				T obj = getInstance();
				obj.setFactory(factory);
				list.add(obj);
				for(Parameter param : row.getParameters())
					setParam(param, obj);
		        obj.setListners();
			} catch (Exception e) {
				if( factory.getConnection().isDEBUG() )
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}
		
		return list;
	}

	private T getInstance()
			throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return factory.getMainClass().getConstructor(factory.getClass()).newInstance(factory);
	}

	private CollectionRecordsCondition getConditions() {
		CollectionRecordsCondition conditions = new CollectionRecordsCondition(CollectionRecordsConditionType.OR);	
		for(T pattern : patterns)
			conditions.add(factory.getObjectRecord(pattern));
		return conditions;
	}

	private void setParam(Parameter param, T obj) {
		Class<?>[] args = new Class[]{String.class};
		try {
			String seterName = "set" + getCapitalize(param.Field.Name.toString());
			Method seter = factory.getMainClass().getMethod(seterName, args);
			seter.invoke(obj, param.Value);
		} catch (Exception e){
			if( factory.getConnection().isDEBUG() )
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	private String getCapitalize(String string) {		 
		return string.toUpperCase().substring(0,1) + string.substring(1);
	}
}
