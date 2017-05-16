package com.Otium.SQLBot;

class Settings {
	private ConnectDatabaseTableSettings SettingsTable;
	
	int Increment = 0;
	
	Settings(ConnectDatabase database){
		SettingsTable = new ConnectDatabaseTableSettings(database);
		try {
			Increment = ((SQLInteger)SettingsTable.Select(1).get(0).getParameterByField(SettingsTable.FieldValue).Value).get();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	int getIncrement(){
		return Increment++;
	}
}
