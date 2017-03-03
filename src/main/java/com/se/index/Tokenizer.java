package com.se.index;

/**
 * Created by Yathish on 3/2/17.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.se.data.Posting;
import com.se.db.DatabaseUtil;

public class Tokenizer {

	public static void walker(String path, List<File> allFiles) {
		File root = new File(path);
		File[] list = root.listFiles();
		if (list == null)
			return;
		for (File f : list) {
			if (f.isDirectory()) {
				walker(f.getAbsolutePath(), allFiles);
			} else {
				allFiles.add(f.getAbsoluteFile());
			}
		}
	}

	public static void main(String[] args) throws IOException {
		List<File> allFiles = new ArrayList<File>();
		String location = "/home/magic/workspace/ICS_Search_Engine/data/0";
		walker(location, allFiles);
		Map<String, List<Posting>> postingListMap = new HashMap<String, List<Posting>>();
		Integer docID = 0;
		for (File files : allFiles) {
			docID++;
			Map<String, Posting> postingMap = new HashMap<String, Posting>();
			String bText;
			try {
				Document doc = Jsoup.parse(files, "UTF-8");
				bText = doc.body().text();
			} catch (Exception e) {
				System.out.println(files);
				System.out.println("Error while parsing: " + e);
				continue;
			}
			bText = bText.toLowerCase();
			Matcher m = Pattern.compile("[^\\W_]+").matcher(bText);
			// TODO: Stemming and stop word removal
			int pos = 1;
			while (m.find()) {
				String currentWord = m.group(0);
				if (isStopWord(currentWord)) {
					continue;
				}

				if (postingMap.containsKey(currentWord)) {
					Posting seenTerm = postingMap.get(currentWord);
					seenTerm.setTermFreq((seenTerm.getTermFreq() + 1));
					List<Integer> posList = seenTerm.getPositions();
					posList.add(pos);
					seenTerm.setPositions(posList);
					postingMap.put(currentWord, seenTerm);
				} else {
					List<Integer> posList = new ArrayList<Integer>();
					posList.add(pos);
					Posting newTerm = new Posting(docID, 1, posList);
					postingMap.put(currentWord, newTerm);
				}
				pos++;
			}

			for (String term : postingMap.keySet()) {
				if (postingListMap.containsKey(term)) {
					List<Posting> postingList = postingListMap.get(term);
					postingList.add(postingMap.get(term));
					postingListMap.put(term, postingList);
				} else {
					List<Posting> postingList = new ArrayList<Posting>();
					postingList.add(postingMap.get(term));
					postingListMap.put(term, postingList);
				}
			}
		}
		// Printing the final Postings List for each term
		DatabaseUtil db = new DatabaseUtil();
		db.insert(postingListMap);

		for (String terms : postingListMap.keySet()) {
			System.out.println("Term: " + terms);
			List<Posting> postingList = postingListMap.get(terms);
			for (Posting pList : postingList) {
				System.out.println("DocID: " + pList.getDocID().toString());
				System.out.println("Term Frequency: " + pList.getTermFreq());
				System.out.print("Positions:");
				for (Integer posi : pList.getPositions()) {
					System.out.print(" " + posi);
				}
				System.out.println();
			}
			System.out.println();
		}
	}

	private static boolean isStopWord(String currentWord) {
		return currentWord.length() < 3;
	}
}