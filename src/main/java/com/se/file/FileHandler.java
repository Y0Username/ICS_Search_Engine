package com.se.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
}
