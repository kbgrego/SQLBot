package com.Otium.SQLBot;

import java.util.ArrayList;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SQLBlob extends SimpleListProperty<Byte> implements SQLData {
		
	public SQLBlob(byte[] array) {
		this(FXCollections.observableList(new ArrayList<Byte>()));
		for(byte b : array)
			add(b);
	}
	
	private SQLBlob(ObservableList<Byte> set){
		super(set);
	}

	public SQLBlob() {
		super(FXCollections.observableList(new ArrayList<Byte>()));
	}

	@Override
	public final FieldDataType getSQLDataType() {
		return FieldDataType.BLOB;
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

	@Override
	public final String getQueryValue() {
		StringBuffer buffer = new StringBuffer();
			   
		buffer.append("x'");
	    for ( Byte b : this.getValue() ) 
	        buffer.append(hexArray[ (b & 0xFF) >>> 4]).append(hexArray[b & 0x0F]);
	   				
		return buffer.append("'").toString();
	}

}
