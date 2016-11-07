package io.github.eli112358.KDM_API.data;

/**
 * Created by Eli112358 on 11/6/16.
 */
public class Field {
	private String name;
	private String value;
	public Field(String name, String value) {
		this.name=name;
		this.value=value;
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
