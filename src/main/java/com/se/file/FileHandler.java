package com.se.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FileHandler {
	public static List<File> walker(String path) {
		List<File> files = new ArrayList<File>();
		File root = new File(path);
		File[] list = root.listFiles();
		if (list == null)
			return files;
		for (File f : list) {
			if (f.isDirectory()) {
				files.addAll(walker(f.getAbsolutePath()));
			} else {
				files.add(f.getAbsoluteFile());
			}
		}
		return files;
	}

	public static String configFetch(String key){
		Properties prop = new Properties();
		InputStream input;
		String value = "";
		try {
			input = new FileInputStream("src/main/resources/configuration.properties");
			prop.load(input);
			value = prop.getProperty(key).toString();
		}
		catch (IOException ex) {
			System.err.println(ex);;
		}
		return value;
	}
}
