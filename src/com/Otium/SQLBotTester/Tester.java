package com.Otium.SQLBotTester;

import java.util.List;

import com.Otium.SQLBot.*;
import com.Otium.SQLBot.Field.FIELD;
import com.Otium.SQLBot.Table.TABLE;

public class Tester {
	private final static TABLE SUBJECTS = new TABLE("subjects");
	private final static FIELD ID       = new FIELD("id");
	private final static FIELD NAME     = new FIELD("name");
	private final static FIELD VALUE    = new FIELD("value");

	public static void main(String[] args) {
		System.out.println(" SQLBot Tester ");
		System.out.println(" Connecting... ");
		ConnectDatabase mainConnect = new ConnectDatabase("simple.db");
		mainConnect.onDEBUG();
		
		System.out.println(" Initialize table... ");
		Table table = initalizeTable(mainConnect);
		
		System.out.println(" Insert the data... ");
		Record rec = new Record();
		rec.addParameter(table.getFieldByName(ID),    "1");
		rec.addParameter(table.getFieldByName(NAME),  "temp");
		rec.addParameter(table.getFieldByName(VALUE), "nothing");
				
		table.RecordInsert(rec);
		
		System.out.println(" Close connection... ");
	}

	private static Table initalizeTable(ConnectDatabase db) {
		Table table_subjects = new Table(SUBJECTS, db);		
		table_subjects.FieldsOfTable.addAll(initializeFields());			
		table_subjects.Create();
		return table_subjects;
	}
	
	private static List<Field> initializeFields(){
		CollectionFields fields = new CollectionFields();
		fields.add(ID,    FieldDataType.INTEGER, Key.PRIMARY, Increment.AUTO);
		fields.add(NAME,  FieldDataType.TEXT,    Key.NONE, Increment.NOAUTO);
		fields.add(VALUE, FieldDataType.TEXT,    Key.NONE, Increment.NOAUTO);
		
		return fields;
	}

}

