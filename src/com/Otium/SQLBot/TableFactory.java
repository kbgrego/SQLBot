/**
 * Copyright 2016 Kabanov Gregory 
 */
package com.Otium.SQLBot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.Otium.SQLBot.Field.FIELD;
import com.Otium.SQLBot.Table.TABLE;

public class TableFactory<T extends TableObject> {
	protected static FIELD RID_FIELD = new FIELD("rid");
	
	private ConnectDatabase Connection;
	private TABLE nameTable;
	private Table Table;
	private Class<T> MainClass;
	

	public TableFactory (ConnectDatabase Connection, Class<T> mainClass) {
		this.Connection = Connection;			
		this.MainClass = mainClass;
		initialTable();
	}
	
	private void initialTable(){
		nameTable = new TABLE(MainClass.getName());
		
		Table = new Table(nameTable, Connection);
		
		initialFields();						
		
		Table.Create();
	}

	private void initialFields() {
		Table.FieldsOfTable.add(RID_FIELD, FieldDataType.INTEGER, Key.PRIMARY, Increment.AUTO);
				
		for(java.lang.reflect.Field field : MainClass.getDeclaredFields())
			if(!field.isAnnotationPresent(SQLBotIgnore.class) && SQLData.class.isAssignableFrom(field.getType()) )
				try {
					FieldDataType type = (FieldDataType) field.getType().getMethod("getSQLDataType").invoke(field.getType().newInstance());
					Table.FieldsOfTable.add(new FIELD(field.getName()), type, Key.NONE, Increment.NOAUTO);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException | InstantiationException e) {
					if(Connection.isDEBUG())
						e.printStackTrace();
				}
	}
	
	public List<T> getList(){
		TableSelector<T> selector = new TableSelector<>(this);
		return selector.getList();
	}

	protected void Create(TableObject tableObject) {
		if(!tableObject.isHasRid())	
			Table.RecordInsert(getObjectRecord(tableObject));
		tableObject.setRid(getLastCreatedIndex(tableObject));			
	}
	
	private SQLInteger getLastCreatedIndex(TableObject tableObject){
		try {
			CollectionRecordsCondition conditions = new CollectionRecordsCondition();
			CollectionSorting sorting = new CollectionSorting();
			Record record = getObjectRecord(tableObject);
			conditions.add(record);							
			sorting.add(new TableSorting(Table.getFieldByName(RID_FIELD), SortType.ASC));					
			return (SQLInteger)Table.Select(conditions, sorting , 1).get(0).getParameterByField(Table.getFieldByName(RID_FIELD)).Value;
		} catch (FieldNotFoundException e) {
			return new SQLInteger(0);
		} 
	}
	
	protected void Update(TableObject tableObject) {
		if(tableObject.isHasRid()){
			Record record = getObjectRecord(tableObject);
			
			CollectionRecordsCondition conditions = new CollectionRecordsCondition();
			
			conditions.add(Table.getFieldByName(RID_FIELD),ConditionType.EQUALS,tableObject.getRid());			
			
			Table.RecordUpdate(record, conditions);
		}			
	}
	
	protected void Delete(TableObject tableObject) {
		if(tableObject.isHasRid()){		
			CollectionRecordsCondition conditions = new CollectionRecordsCondition();
			
			conditions.add(Table.getFieldByName(RID_FIELD),ConditionType.EQUALS,tableObject.getRid());			
			
			Table.RecordDelete(conditions);
		}			
	}
	

	
	protected Record getObjectRecord(TableObject tableObject) {
		Record record = new Record();
		for(Field field:Table.FieldsOfTable){			
			try {
				Method geter = MainClass.getMethod(TableObject.getGetterName(field.Name.toString()));
				Object value = geter.invoke(tableObject);
				if(value == null || (field.Name == RID_FIELD && ((SQLInteger)value).get()==0))
					continue;
				else if(value instanceof SQLData)
					record.addParameter(field, value);
				else
					throw new NotSQLDataException(field.Name.toString());
			} catch (Exception e){
				//silence is golden
			}
		}
		return record;
	}
	
	protected Table getTable(){
		return Table;
	}
	
	protected Class<T> getMainClass(){
		return MainClass;
	}

	protected ConnectDatabase getConnection(){
		return Connection;
	}
	
	protected T getInstance()
			throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		T instance = MainClass.getConstructor().newInstance();
		MainClass.getMethod("setFactory", new Class[]{TableFactory.class}).invoke(instance, this);
		return instance;
	}
}
