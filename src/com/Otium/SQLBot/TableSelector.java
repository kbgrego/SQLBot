/**
 * Copyright 2016 Kabanov Gregory 
 */

package com.Otium.SQLBot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Otium.SQLBot.Record.Parameter;

public class TableSelector<T extends TableObject> {	
	private TableFactory<T> factory;
	private List<T> patterns;
	private List<ConditionType> types;
	
	private static Map<Integer, TableObject> OBJECT_BUFFER = new HashMap<>();
	private List<Class<?>> CLASS_BUFFER = new ArrayList<>();
	
	public TableSelector(TableFactory<T> factory) {
		this.patterns = new ArrayList<>();
		this.types = new ArrayList<>();
		this.factory = factory;		 		
	}
		
	public void	addPattern(T pattern){
		addPattern(pattern, ConditionType.EQUALS);
	}
	
	public void	addPattern(T pattern, ConditionType type){
		patterns.add(pattern);
		types.add(type);
	}
	
	public List<T> getList(){
		List<T> list = (List<T>) getSimpleList(factory, getConditions());
		setTableObjectInBuffer();
		return list;
	}
	
	private List<TableObject> getSimpleList(TableFactory<?> factory){
		return getSimpleList(factory, CollectionRecordsCondition.NULL);
	}
	
	private List<TableObject> getSimpleList(TableFactory<?> factory, CollectionRecordsCondition conditions){
		return getSimpleList(factory, conditions, 0);
	}
	
	private void setTableObjectInBuffer() {
		for(TableObject object: OBJECT_BUFFER.values()){
			if(!object.isLoaded){
				for(Field field : object.getTableFactory().getTable().FieldsOfTable){
					try {
						Class<?> object_class = getClassOfField(object.getTableFactory(), field);
						if(object_class!=null && TableObject.class.isAssignableFrom(object_class)){
							trySetTableObjectParameter(object, field, object_class);
						}
					} catch (Exception e) {
						if( factory.getConnection().isDEBUG() )
							System.err.println(e.getClass().getName() + ": " + e.getMessage());
					}
				}
				object.isLoaded = true;
			}
		}
	}

	private void trySetTableObjectParameter(TableObject object, Field field, Class<?> field_class)
			throws FieldNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		String getterName = TableObject.getGetterName(field.Name.toString());
		int rid = ((SQLInteger)object.record.getParameterByField(field).Value).get();
		SQLTableObject<TableObject> field_object = (SQLTableObject<TableObject>) object.getTableFactory().getMainClass().getMethod(getterName).invoke(object);
		field_object.set(OBJECT_BUFFER.get(rid));
	}

	public List<TableObject> getSimpleList(TableFactory<?> factory, CollectionRecordsCondition conditions,Integer limit){
		List<TableObject> list = new ArrayList<>();	
						
		List<Record> data = factory.getTable().Select(conditions, limit);		
		
		for(Record row : data){
			try {
				TableObject obj = factory.getInstance();
				obj.setFactory(factory);
				obj.record = row;
				int rid = ((SQLInteger)row.getParameterByField(factory.getTable().getFieldByName(TableFactory.RID_FIELD)).Value).get(); 
		        if(!OBJECT_BUFFER.containsKey(rid))
					OBJECT_BUFFER.put(rid, obj); 
				for(Parameter param : row.getParameters())
					setParam(factory, param, obj);
		        obj.setListners();
				list.add(obj);
			} catch (Exception e) {
				if( factory.getConnection().isDEBUG() )
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		}
		
		return list;
	}

	private void prepearSelection(TableFactory<?> factory, Field field, Class<?> object_class) {
		if(CLASS_BUFFER.contains(object_class))
			return;
		CLASS_BUFFER.add(object_class);

		try {
			if(object_class!=null && TableObject.class.isAssignableFrom(object_class) && !OBJECT_BUFFER.containsKey(object_class)){
				TableFactory<?> object_factory = (TableFactory<?>) object_class.getMethod("getTableFactory").invoke(object_class.newInstance());
				getSimpleList(object_factory);
			}
		} catch (Exception e) {
			if( factory.getConnection().isDEBUG() )
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	private void setParam(TableFactory<?> factory, Parameter param, TableObject obj) throws TableObjectNotFoundException {
		try {
			String seterName = TableObject.getSetterName(param.Field.Name.toString());
			Class<?> object_class = getClassOfField(factory, param.Field);
			if(object_class!=null && TableObject.class.isAssignableFrom(object_class)){
				if(OBJECT_BUFFER.containsKey(((SQLInteger)param.Value).get()))
					factory.getMainClass().getMethod(seterName, new Class[]{SQLTableObject.class})
                        .invoke(obj, new SQLTableObject<>(OBJECT_BUFFER.get(((SQLInteger)param.Value).get())));
				else {
					prepearSelection(factory, param.Field, object_class);
					factory.getMainClass().getMethod(seterName, new Class[]{SQLTableObject.class})
                    	.invoke(obj, new SQLTableObject<>(OBJECT_BUFFER.get(((SQLInteger)param.Value).get())));
				}
			} else { 
				factory.getMainClass().getMethod(seterName, new Class[]{param.Value.getClass()})
				                         .invoke(obj, param.Value);
			}
		} catch (Exception e){
			if( factory.getConnection().isDEBUG() )
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	private Class<?> getClassOfField(TableFactory<?> factory, Field field) throws NoSuchFieldException {
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
	
	public void Create(List<T> samples){
		factory.Create(samples);
	}

	private CollectionRecordsCondition getConditions() {
		CollectionRecordsCondition conditions = new CollectionRecordsCondition(CollectionRecordsConditionType.AND);	
		for(int i=0; i<patterns.size();i++)
			conditions.add(factory.getObjectRecord(patterns.get(i)), types.get(i));
		return conditions;
	}
}
