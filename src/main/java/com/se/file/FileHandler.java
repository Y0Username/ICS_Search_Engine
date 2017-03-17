package com.se.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.se.index.Tokenizer;

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

	public static String configFetch(String key) {
		Properties prop = new Properties();
		InputStream input;
		//TODO: Hardcoding file path; needs to be fixed
		if(key == "path")
			return "/home/magic/Downloads/WEBPAGES_RAW/";
		String value = "";
		try {
			input = new FileInputStream("src/main/resources/configuration.properties");
			//input = new FileInputStream("src/main/java/com/se/file/configuration.properties");
			prop.load(input);
			value = prop.getProperty(key).toString();
		} catch (IOException ex) {
			System.err.println(ex);
			;
		}
		return value;
	}

	public static String fetch(int fromWord, int toWord, String filePath,
			String url) {
		String path = configFetch("path");
		StringBuilder stringBuilder = new StringBuilder();
		try {
			Document document = Jsoup.parse(new File(path + filePath), "UTF-8",
					url);
			String text = document.text();
			Matcher m = Pattern.compile("[^\\W_]+").matcher(text);
			Integer wordPosition = 0;
			while (m.find()) {
				String currentWord = m.group(0);

				if (!Tokenizer.isStopWord(currentWord)) {
					wordPosition++;
				}

				if (wordPosition <= fromWord) {
					continue;
				}
				stringBuilder.append(currentWord);
				stringBuilder.append(" ");

				if (wordPosition > toWord) {
					break;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

}
