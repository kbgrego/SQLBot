/**
 * Copyright Otium (c) 2016
 */
package com.Otium.SQLBot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Otium.SQLBot.Field;
import com.Otium.SQLBot.Table.TABLE;

/**
 * Class {@code ConnectDatabase} is a service for establish connection to local Database.
 * Default file: "main.db".
 * 
 * @author  Kabanov Gregory
 */
public class ConnectDatabase{
	
	private static final String CLASS_JDBC = "org.sqlite.JDBC";
	private static final String PATH_JDBC  = "jdbc:sqlite:";

	public  List<Table> listTables = new ArrayList<Table>();
	
	public  Connection MainSQLConnection;
	private String     DatabaseName = "main.db";
	private boolean    DEBUG = false;
		
	/** 
	 * Initial file db with a name of file 
	 */
	public ConnectDatabase(String string) {
		ChangeMainDatabase(string);
	}

	public void EstablishConnect() throws ClassNotFoundException, SQLException{
		
		if( MainSQLConnection==null || MainSQLConnection.isClosed() ) {
			
			Class.forName(CLASS_JDBC);
			MainSQLConnection = DriverManager.getConnection(PATH_JDBC + DatabaseName);
			
		}
	}
	
	/**
	 * Procedure to add table in current database. Create a table in database if not exist.  
	 */
	public void createTableInDatabase(Table table) throws InvalidTableExeption, ClassNotFoundException, SQLException{
		
		//check information about table
		if(table.NameOfTable.equals(""))
			throw new InvalidTableExeption();
		if(table.FieldsOfTable.size()==0)
			throw new InvalidTableExeption();
		for(Field f : table.FieldsOfTable)
			if(f.Name.equals(""))
				throw new InvalidTableExeption();
		
		//check connection
		EstablishConnect();
		
		//create and add table to list
		table.setDatabase(this);
		table.Create();
		listTables.add(table);		
	}
	
	/**
	 * Procedure to change the local file with database.
	 * @param name 
	 */
	public void ChangeMainDatabase(String name){
		
		if( name==null || name.equals("") )
			DatabaseName = "main.db";
		else
			DatabaseName = name;
		
	}

	public Table findTableByName(TABLE name) throws TableNotFoundExeption{
		for(Table t : listTables)
			if(t.NameOfTable==name)
				return t;
		throw new TableNotFoundExeption();
	}

	/**
	 * Thrown when application try get table from the general list of tables.
	 */
	public class TableNotFoundExeption extends Exception{

		private static final long serialVersionUID = -1192779220519457545L;
		
	}

	public ResultSet executeQuery(String query) {
		try{
			EstablishConnect();
			if( DEBUG )
				System.out.println(query);
			return MainSQLConnection.createStatement()
					                .executeQuery(query);
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public void onDEBUG(){
		DEBUG = true;
	}
}
