package io.github.eli112358.KDM_API.data;

import java.util.ArrayList;
/**
 * Created by Eli112358 on 11/6/16.
 */
public class Node {
	private String label;
	private ArrayList<Node> nodes=new ArrayList<>();
	private ArrayList<Field> fields=new ArrayList<>();
	public Node(String label) {
		this.label=label;
	}
	public String getLabel() {
		return label;
	}
	public void addField(Field field) {
		fields.add(field);
	}
	public void addField(String name, String value) {
		addField(new Field(name, value));
	}
	public void addNode(Node node) {
		nodes.add(node);
	}
	public void addNode(String label) {
		addNode(new Node(label));
	}
	public String getDisplayName() {
		return hasField("name")?getField("name").getValue():getLabel();
	}
	public Field getField(int index) {
		return fields.get(index);
	}
	public Field getField(String name) {
		return hasField(name)?getFields(name).get(0):null;
	}
	public ArrayList<Field> getFields(String name) {
		ArrayList<Field> matches=new ArrayList<>();
		for(int x=0; x<fields.size(); x++)
			if(getField(x).getName().equals(name)) matches.add(getField(x));
		return matches;
	}
	public Node getNode(int index) {
		return nodes.get(index);
	}
	public Node getNode(String name) {
		return hasNode(name, null)?getNodes(name).get(0):null;
	}
	public ArrayList<Node> getNodes(String name) {
		ArrayList<Node> matches=new ArrayList<>();
		for(int x=0; x<nodes.size(); x++)
			if(getNode(x).getDisplayName().equals(name)) matches.add(getNode(x));
		return matches;
	}
	public boolean hasField(String name) {
		for(int x=0; x<fields.size(); x++) if(getField(x).getName().equals(name)) return true;
		return false;
	}
	public boolean hasNode(String name, NodeFilter filter) {
		if(filter==null) filter=NodeFilter.either;
		for(int x=0; x<nodes.size(); x++) if(matches(name, filter)) return true;
		return false;
	}
	private boolean matches(String name, NodeFilter filter) {
		boolean isLabelEqual=getLabel().equals(name);
		boolean isNameEqual=getField("name").getValue().equals(name);
		boolean isEither=filter.equals(NodeFilter.either);
		boolean isLabelOnly=filter.equals(NodeFilter.labelOnly);
		boolean isNameOnly=filter.equals(NodeFilter.nameOnly);
		return isEither?isLabelEqual||isNameEqual:isLabelOnly?isLabelEqual:isNameOnly&&isNameEqual;
	}
	private enum NodeFilter {
		either,
		labelOnly,
		nameOnly;
		private boolean equals(NodeFilter filter) {
			return this==filter;
		}
	}
}
