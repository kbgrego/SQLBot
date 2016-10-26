package com.Otium.SQLBot;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.Otium.SQLBot.Field.FIELD;
import com.Otium.SQLBot.Record.Parameter;
import com.Otium.SQLBot.Table.TABLE;

/**
 * Copyright 2016 Kabanov Gregory 
 */

public class TableFactory<T> {
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
		Table.FieldsOfTable.add(new FIELD("rid"), FieldDataType.INTEGER, Key.PRIMARY, Increment.AUTO);
				
		for(java.lang.reflect.Field field : MainClass.getDeclaredFields())
			if(!field.isAnnotationPresent(SQLBotIgnore.class) )
				Table.FieldsOfTable.add(new FIELD(field.getName()), FieldDataType.TEXT, Key.NONE, Increment.NOAUTO);
	}
	
	public List<T> getList(){
		List<T> list = new ArrayList<T>();
		Class[] args = new Class[]{String.class};
		
		List<Record> data = Table.Select();
		
		for(Record row : data){
			try {
				T obj = MainClass.newInstance();
				list.add(obj);
				for(Parameter param : row.getParameters()){
						Method seter = MainClass.getMethod("set" + param.Field.Name.toString(), args);
						seter.invoke(obj, new String[]{param.Value});
				}
			} catch (Exception e) {
				//silence is golden
			}
		}
		
		return list;
	}

}
