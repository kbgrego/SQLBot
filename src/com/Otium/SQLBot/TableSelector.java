/**
 * Copyright 2016 Kabanov Gregory 
 */

package com.Otium.SQLBot;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Otium.SQLBot.Record.Parameter;

public class TableSelector<T extends TableObject> {	
	private TableFactory<T> factory;
	private List<T> patterns;	
	
	private static Map<Class<?>, List<TableObject>> buffer = new HashMap<>(); 
	
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
		
		prepearSelection();
						
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

	private void prepearSelection() {
		for(Field field : factory.getTable().FieldsOfTable){
			try {
				Class<?> object_class = getClassOfField(field);
				if(object_class!=null && TableObject.class.isAssignableFrom(object_class) && !buffer.containsKey(object_class)){
					TableFactory object_factory = (TableFactory) object_class.getMethod("getTableFactory").invoke(object_class.newInstance());				
					buffer.put(object_class, new TableSelector<TableObject>(object_factory).getList()); 
				}
			} catch (Exception e) {
				if( factory.getConnection().isDEBUG() )
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}		
	}

	private void setParam(Parameter param, T obj) throws TableObjectNotFoundException {
		try {
			String seterName = TableObject.getSetterName(param.Field.Name.toString());
			Class<?> object_class = getClassOfField(param.Field);
			if(buffer.containsKey(object_class)){
				factory.getMainClass().getMethod(seterName, new Class[]{SQLTableObject.class})
				                         .invoke(obj, new SQLTableObject(tryGetTableObject(param)));
			} else { 
				factory.getMainClass().getMethod(seterName, new Class[]{param.Value.getClass()})
				                         .invoke(obj, param.Value);
			}
		} catch (Exception e){
			if( factory.getConnection().isDEBUG() )
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	private TableObject tryGetTableObject(Parameter param) throws TableObjectNotFoundException, NoSuchFieldException, SecurityException {
		Class<?> object_class = getClassOfField(param.Field);
		for(TableObject object : buffer.get(object_class))
			if(object.rid.compareTo(((SQLInteger) param.Value))==0)
				return object;
		
		return null;
	}

	private Class<?> getClassOfField(Field field) throws NoSuchFieldException {
		Class<?> object_class=null;
		Object paramType;
		if((field.Name!=TableFactory.RID_FIELD) && ((paramType=factory.getMainClass().getField(field.Name.toString()).getGenericType()) instanceof ParameterizedType))
			object_class = (Class<?>) ((ParameterizedType)paramType).getActualTypeArguments()[0];
		return object_class;
	}
	
	public void Delete(){
		factory.getTable().RecordDelete(getConditions());
	}
	
	public void Update(T sample){
		factory.getTable().RecordUpdate(factory.getObjectRecord(sample), getConditions());
	}

	private CollectionRecordsCondition getConditions() {
		CollectionRecordsCondition conditions = new CollectionRecordsCondition(CollectionRecordsConditionType.AND);	
		for(T pattern : patterns)
			conditions.add(factory.getObjectRecord(pattern));
		return conditions;
	}
}
