package io.github.eli112358.KDM_API.data;

/**
 * Created by Eli112358 on 11/6/16.
 */
public class Field {
	private static final String delimiter=" = ";
	private static final char indentChar=' ';
	private String name;
	private String value;
	public Field(String name, String value) {
		this.name=name;
		this.value=value;
	}
	public static Field parse(String line) {
		String temp=String.copyValueOf(line.toCharArray());
		while(temp.charAt(0)==indentChar) temp=temp.substring(1);
		int index=temp.indexOf(delimiter);
		String value=temp.length()-index==delimiter.length()?"":temp.substring(index+delimiter.length());
		return new Field(temp.substring(0,index), value);
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value=value;
	}
}
