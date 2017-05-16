package com.Otium.SQLBot;

import com.Otium.SQLBot.Field.FIELD;

class ConnectDatabaseTableSettings extends Table {
	private static TABLE Name = new TABLE("sqlite_sequence");
	
	static Field FieldName  = new Field(new FIELD("name"),  FieldDataType.TEXT,    Key.UNIQUE , Increment.NOAUTO);
	static Field FieldValue = new Field(new FIELD("value"), FieldDataType.TEXT,    Key.NONE   , Increment.NOAUTO);

	ConnectDatabaseTableSettings(ConnectDatabase database) {
		super(Name, database);
		FieldsOfTable.add(FieldName);
		FieldsOfTable.add(FieldValue);		
	}

}
