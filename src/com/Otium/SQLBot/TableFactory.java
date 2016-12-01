/**
 * Copyright 2016 Kabanov Gregory 
 */
package com.Otium.SQLBot;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.Otium.SQLBot.Field.FIELD;
import com.Otium.SQLBot.Table.TABLE;

public class TableFactory<T extends TableObject> {
	protected FIELD RID_FIELD = new FIELD("rid");
	
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
			if(!field.isAnnotationPresent(SQLBotIgnore.class) )
				Table.FieldsOfTable.add(new FIELD(field.getName()), FieldDataType.TEXT, Key.NONE, Increment.NOAUTO);
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
	
	private String getLastCreatedIndex(TableObject tableObject){
		try {
			CollectionRecordsCondition conditions = new CollectionRecordsCondition();
			List<TableSorting> sorting = new ArrayList<TableSorting>();
			Record record = getObjectRecord(tableObject);
			conditions.add(record);							
			sorting.add(new TableSorting(Table.getFieldByName(RID_FIELD), SortType.ASC));					
			return Table.Select(conditions, sorting , 1).get(0).getParameterByField(Table.getFieldByName(RID_FIELD)).Value;
		} catch (FieldNotFoundException e) {
			return "0";
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
	
	private String getCapitalize(String string) {		 
		return string.toUpperCase().substring(0,1) + string.substring(1);
	}
	
	protected Record getObjectRecord(TableObject tableObject) {
		Record record = new Record();
		for(Field field:Table.FieldsOfTable){
			if(field.Name == RID_FIELD) continue;
			try {
				String geterName = "get" + getCapitalize(field.Name.toString());
				Method seter = MainClass.getMethod(geterName);
				Object value = seter.invoke(tableObject);
				if(value == null)
					continue;
				else if(value instanceof TableObject)
					record.addParameter(field, ((TableObject)value).getRid() );
				else
					record.addParameter(field, value.toString());
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
}
