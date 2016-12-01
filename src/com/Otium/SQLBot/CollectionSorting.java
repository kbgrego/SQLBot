/**
 * Copyright 2016 Kabanov Gregory 
 */

package com.Otium.SQLBot;

import java.util.ArrayList;

public class CollectionSorting extends ArrayList<TableSorting> {
	private static final long serialVersionUID = 5841719410922880652L;
	private static final int FIRST_INDEX = 0;
	
	public boolean add(TableSorting sorting){
		return super.add(sorting);		
	}
		
	public boolean add(Field field, SortType type){
		return super.add(new TableSorting(field, type));		
	}
	
	public String getQuerySequence(){
		
		StringBuffer sequence = new StringBuffer();
		
		for(TableSorting sort : this)
			if(isFirst(sort))
				sequence.append(" ORDER BY ")
						.append(sort);
			else
				sequence.append(", ")
				        .append(sort);
		
		return sequence.toString();
	}

	private boolean isFirst(TableSorting sort) {
		return indexOf(sort) == FIRST_INDEX;
	}
}
