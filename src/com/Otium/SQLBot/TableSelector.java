/**
 * Copyright 2016 Kabanov Gregory 
 */

package com.Otium.SQLBot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
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
				T obj = factory.getInstance();
				obj.setFactory(factory);
				for(Parameter param : row.getParameters())
					setParam(param, obj);
		        obj.setListners();
				list.add(obj);
			} catch (Exception e) {
				if( factory.getConnection().isDEBUG() )
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}
		
		return list;
	}

	private CollectionRecordsCondition getConditions() {
		CollectionRecordsCondition conditions = new CollectionRecordsCondition(CollectionRecordsConditionType.AND);	
		for(T pattern : patterns)
			conditions.add(factory.getObjectRecord(pattern));
		return conditions;
	}

	private void setParam(Parameter param, T obj) throws TableObjectNotFoundException {
		try {
			String seterName = TableObject.getSetterName(param.Field.Name.toString());
			Class<?> object_class=null;
			Object paramType;
			if((param.Field.Name!=TableFactory.RID_FIELD) && ((paramType=factory.getMainClass().getField(param.Field.Name.toString()).getGenericType()) instanceof ParameterizedType))
				object_class = (Class<?>) ((ParameterizedType)paramType).getActualTypeArguments()[0];
			if(object_class!=null && TableObject.class.isAssignableFrom(object_class)){
				factory.getMainClass().getMethod(seterName, new Class[]{SQLTableObject.class})
				                         .invoke(obj, new SQLTableObject(tryGetTableObject(param, obj, seterName, object_class)));
			} else { 
				factory.getMainClass().getMethod(seterName, new Class[]{param.Value.getClass()}).invoke(obj, param.Value);
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException | NoSuchFieldException e){
			if( factory.getConnection().isDEBUG() )
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	private TableObject tryGetTableObject(Parameter param, T obj, String seterName, Class<?> object_class)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException,
			TableObjectNotFoundException {
		TableFactory object_factory = (TableFactory) object_class.getMethod("getTableFactory").invoke(object_class.newInstance());
		TableSelector selector = new TableSelector(object_factory);
		TableObject instance = (TableObject)factory.getInstance();
		instance.setRid((SQLInteger)param.Value);
		selector.addPattern(instance);
		List result = selector.getList(1); 
		if(!result.isEmpty())					
			return (TableObject) result.get(0);
		else
			throw new TableObjectNotFoundException(factory.getTable().NameOfTable, ((SQLInteger)param.Value).get());
	}
}
