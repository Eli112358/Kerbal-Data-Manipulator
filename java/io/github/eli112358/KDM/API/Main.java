package io.github.eli112358.KDM.API;

import io.github.eli112358.KDM.API.data.Field;
import io.github.eli112358.KDM.API.data.Node;

import java.io.*;
import java.util.ArrayList;
/**
 * Created by Eli112358 on 11/6/16.
 */
public class Main {
	public static void save(Data data) {
		try {
			new Saver(data).start();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
	public static Data load(String fileName) {
		return load(new File(fileName));
	}
	public static Data load(File file) {
		Data data=new Data(file);
		Node node=data;
		ArrayList<Node> stack=new ArrayList<>();
		ArrayList<String> lines=new ArrayList<>();
		System.gc();
		try {
			BufferedReader br=new BufferedReader(new FileReader(file));
			while(br.ready()) lines.add(br.readLine());
			br.close();
		} catch(java.io.IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		for(String line1 : lines) {
			String line=line1.trim();
			if(line.isEmpty()) continue;
			if(line.contains(Field.delimiter)) node.addField(line);
			else if(line.equals("}")) {
				Node prev=stack.remove(0);
				prev.addNode(node);
				node=prev;
			} else if(!line.equals("{")) {
				stack.add(0, node);
				node=new Node(line);
			}
		}
		return data;
	}
	private static class Saver {
		private final Data data;
		private BufferedWriter bw;
		private int indentation=0;
		private Saver(Data data) {
			this.data=data;
		}
		public void start() throws IOException {
			bw=new BufferedWriter(new FileWriter(data.getFile()));
			saveNode(data.getNode(0));
		}
		private void saveNode(Node node) {
			printToFile(node.label);
			printToFile("{");
			indentation++;
			node.getNodesIterator().forEachRemaining(this::saveNode);
			node.getFieldsIterator().forEachRemaining(field->printToFile(field.toString()));
			indentation--;
			printToFile("}");
		}
		private void printToFile(String text) {
			try {
				bw.write(String.format("%"+indentation+text, "\t"));
				bw.flush();
			} catch(IOException e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
	}
	public static class Data extends Node {
		private final File file;
		public Data(File file) {
			super("");
			this.file=file;
		}
		public File getFile() {
			return file;
		}
	}
}
