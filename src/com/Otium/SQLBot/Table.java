/**
 * Copyright Otium (c) 2016
 */
package com.Otium.SQLBot;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Otium.SQLBot.Field.FIELD;

public class Table{
	private static final int FIRST_INDEX = 0;
	private ConnectDatabase Database;
	public  TABLE NameOfTable;
	public  CollectionFields FieldsOfTable;
	public  List<FIELD> PrimaryKeyOfTable;
	
	public Table(TABLE nameOfTable, ConnectDatabase database){
		this.NameOfTable = nameOfTable;
		this.FieldsOfTable  = new CollectionFields();
		this.PrimaryKeyOfTable = new ArrayList<FIELD>();
		this.Database = database;
	}
	
	public void setDatabase(ConnectDatabase database){
		this.Database = database;
	}
	
	public  void   Create(){
		Database.executeQuery(getQueryOfCreatingTable());
	}	
	
	private String getQueryOfCreatingTable(){
		
		StringBuffer query = new StringBuffer();

		query.append("Create table if not exists '" + this.NameOfTable + "' (")
		     .append(getSequenceOfTableFields())
		     .append(getSequenceOfTablePrimaryKeys())
		     .append(")");
		
		return query.toString();				
	}
	
	private String getSequenceOfTableFields(){		
		StringBuffer sequence = new StringBuffer();
		
		for(Field field : this.FieldsOfTable){	
			
			if( this.FieldsOfTable.indexOf(field) != FIRST_INDEX )
				sequence.append(",");
			
			sequence.append(field.getCreatingString());
		}
		return sequence.toString();
	}
	
	private String getSequenceOfTablePrimaryKeys(){		
		
		if(PrimaryKeyOfTable.isEmpty())
			return new String();
			
		StringBuffer sequence = new StringBuffer();
		sequence.append(", PRIMARY KEY(");
		for( FIELD nameOfField : this.PrimaryKeyOfTable ){	
			if( this.PrimaryKeyOfTable.indexOf(nameOfField) != FIRST_INDEX )
				sequence.append(",");
			sequence.append(nameOfField);
		}
		sequence.append(")");					
		return sequence.toString();
	}
	
	public  int   RecordInsert(Record record){
		return Database.executeInsertQuery(getQueryToDoRecordInsert(record));
	}
	
	private String getQueryToDoRecordInsert(Record record){
		StringBuffer query  = new StringBuffer();
		
		query.append("INSERT OR IGNORE INTO `") 
		     .append(this.NameOfTable)
		     .append("` (") 
		     	.append(record.getParameters().getQuerySequenceOfFields())
		     .append(") VALUES (")
             	.append(record.getParameters().getQuerySequenceOfValues())
             .append(");"); 

		return query.toString();
	}
	
	public  void   RecordUpdate(Record record, CollectionRecordsCondition conditions){
		Database.executeQuery(getQueryToDoRecordUpdate(record, conditions));
	}
	
	private String getQueryToDoRecordUpdate(Record record, CollectionRecordsCondition conditions){
		StringBuffer qeury = new StringBuffer();
		
		qeury.append("UPDATE `")
		     .append(this.NameOfTable)
		     .append("` SET ")
			 .append(record.getParameters().getQuerySequenceOfFieldsSetValue())
		     .append(conditions.getQuerySequence());	

	
		return qeury.toString();
	}
	
	public  void   RecordDelete(CollectionRecordsCondition conditions){
		Database.executeQuery(getQueryToDoRecordDelete(conditions));
	}
	
	private String getQueryToDoRecordDelete(CollectionRecordsCondition conditions){
		StringBuffer qeury = new StringBuffer();

		qeury.append("DELETE FROM `") 
			 .append(this.NameOfTable)
			 .append("`")
		     .append(conditions.getQuerySequence());			

		return qeury.toString();
	}
	
	public List<Record> Select(){
		return Select(0);
	}
	
	public List<Record> Select(int limit) {
		return Select(new CollectionRecordsCondition(), limit);
	}
	
	public List<Record> Select(CollectionRecordsCondition conditions, int limit){
		return Select(conditions, new CollectionSorting(), limit);
	}
	
	public List<Record> Select(CollectionRecordsCondition conditions, CollectionSorting sorting, int limit){
		try{
			return selectWithThrows(conditions, sorting, limit);
		} catch (Exception e) {
			if(Database.isDEBUG())
				e.printStackTrace();
			return new ArrayList<Record>();
		}
	}
	
	@SuppressWarnings("unused")
	private List<Record> selectWithThrows(CollectionRecordsCondition conditions, CollectionSorting sorting, int limit) throws SQLException{
		List<Record> records = new ArrayList<Record>();		
		
		ResultSet resultSet = Database.executeQuery(getQueryToDoSelect(conditions, sorting, limit));
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		
		if(resultSet==null)
			return records;
		
	    while(resultSet.next()) {
	        Record record = new Record();
	        for(int i=1; i <= resultSetMetaData.getColumnCount(); i++)
	        	record.addParameter(FieldsOfTable.get(i-1), resultSet.getObject(i));
	        records.add(record);
	    }	
	    
	    return records;
	}
	

	private String getQueryToDoSelect(CollectionRecordsCondition conditions, CollectionSorting sorting, int limit){
		StringBuffer qeury = new StringBuffer();			
		
		qeury.append("SELECT * FROM `")
		     .append(this.NameOfTable)
		     .append("`")			
			 .append(conditions.getQuerySequence())			
			 .append(sorting.getQuerySequence())							
			 .append(getLimitParameter(limit));
		
		return qeury.toString();
	}
	
	private String getLimitParameter(int limit){
		if(limit>0)
			return " LIMIT " + limit;
		else
			return "";
	}
	
	public Field getFieldByName(FIELD name){
		for(Field field : FieldsOfTable)
			if(field.Name==name)
				return field;
		return null;
	}
	
	public Field getFieldByName(String name){
		for(Field field : FieldsOfTable)
			if(field.Name.toString().equals(name))
				return field;
		return null;
	}
	
	
	public static class TABLE{
		
		private String Name;
		
		public TABLE(String name){
			if(name!=null)
				Name = name;
			else
				Name = new String();
		}
		
		@Override
		public String toString() {
			return Name;
		}
	}
}