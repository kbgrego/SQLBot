package com.Otium.SQLBot;

class Settings {
	private ConnectDatabaseTableSettings SettingsTable;
	
	int Increment = 1;
	
	Settings(ConnectDatabase database){
		SettingsTable = new ConnectDatabaseTableSettings(database);
		try {
			Increment = ((SQLInteger)SettingsTable.Select(1).get(0).getParameterByField(SettingsTable.FieldValue).Value).get()+1;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	int getIncrement(){
		return Increment++;
	}
	
	int getIncrement(int size){
		return Increment=Increment+size;
	}
}
