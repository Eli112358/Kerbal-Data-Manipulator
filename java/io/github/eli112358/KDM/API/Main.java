package io.github.eli112358.KDM.API;

import io.github.eli112358.KDM.API.data.Field;
import io.github.eli112358.KDM.API.data.Node;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
/**
 * Created by Eli112358 on 11/6/16.
 */
public class Main {
	public static void save(Data data, boolean isClipboard) {
		try {
			new Saver(data, isClipboard).start();
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
		private static String pattern="yyyyMMdd-HHmmss";
		private static SimpleDateFormat format=new SimpleDateFormat(pattern);
		private final Data data;
		private final boolean isClipboard;
		private BufferedWriter bw;
		private int indentation=0;
		private Saver(Data data, boolean isClipboard) {
			this.data=data;
			this.isClipboard=isClipboard;
		}
		void start() throws IOException {
			String now=format.format(new Date());
			File output=data.getFile();
			Path source=Paths.get(output.toURI());
			Path newName=source.resolveSibling(output.getName()+"-"+getType()+now);
			if(isClipboard) output=newName.toFile();
			else Files.move(source, newName);
			bw=new BufferedWriter(new FileWriter(output));
			saveNode(data.getNode(0));
		}
		private String getType() {
			return isClipboard?"clipboard":"backup";
		}
		private void saveNode(Node node) {
			printToFile(node.label);
			printToFile("{");
			indentation++;
			node.getFieldsIterator().forEachRemaining(this::saveField);
			node.getNodesIterator().forEachRemaining(this::saveNode);
			indentation--;
			printToFile("}");
		}
		private void saveField(Field field) {
			printToFile(field.toString());
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
