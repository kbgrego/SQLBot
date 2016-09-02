package com.Otium.SQLBot;

public class TableSorting {
	public Field    field;
	public SortType type;
	
	TableSorting(){}
	
	public TableSorting(Field field, SortType type){
		this.field = field;
		this.type = type;
	}
	@Override
	public String toString(){
		return "`" + field.Name + "` " + type;
	}
}
