package com.se.file;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.se.index.WordsTokenizer;

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
		// TODO: Hardcoding file path; needs to be fixed
		if (key == "path")
			return "/home/magic/Downloads/WEBPAGES_RAW/";
		String value = "";
		try {
			input = new FileInputStream(
					"src/main/resources/configuration.properties");
			// input = new
			// FileInputStream("src/main/java/com/se/file/configuration.properties");
			prop.load(input);
			value = prop.getProperty(key).toString();
		} catch (IOException ex) {
			System.err.println(ex);
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

				if (!WordsTokenizer.isStopWord(currentWord)) {
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

	public static Map<String, Double> relevancyReader(String query) {
		Map<String, Double> relevancyMap = new LinkedHashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(
				"src/main/resources/google_dcg.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.equals(query)) {
					for (int i = 0; i < 10; i++) {
						line = br.readLine();
						if (line == null)
							break;
						java.util.StringTokenizer itr = new java.util.StringTokenizer(
								line);
						if (itr.countTokens() < 2) {
							break;
						}
						String url = itr.nextToken();
						Double relevancy = Double.parseDouble(itr.nextToken());
						relevancyMap.put(url, relevancy);
					}
				}
			}
		} catch (IOException ex) {
			System.err.println(ex);
		}
		return relevancyMap;
	}

	public static String getTitle(String filePath, String url) {
		String path = configFetch("path");
		String title = url;
		try {
			Document document = Jsoup.parse(new File(path + filePath), "UTF-8",
					url);
			title = document.title();
		} catch (IOException exception) {
			System.err.println(exception.getMessage());
		}
		return title;
	}

}
