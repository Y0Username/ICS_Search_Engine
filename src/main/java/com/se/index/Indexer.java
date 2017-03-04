package com.se.index;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.se.data.Posting;
import com.se.data.WordEntry;
import com.se.db.DatabaseUtil;
import com.se.file.FileHandler;

import static com.se.file.FileHandler.configFetch;

public class Indexer {
	private static final String location = configFetch("path");

	public void index() {
		List<File> files = FileHandler.walker(location);
		Map<String, List<Posting>> postingListMap = new HashMap<String, List<Posting>>();
		Integer docID = 0;
		for (File file : files) {
			docID++;
			Map<String, Posting> postingMap = Tokenizer.tokenize(file, docID);

			for (String term : postingMap.keySet()) {
				List<Posting> postingList;
				if (postingListMap.containsKey(term)) {
					postingList = postingListMap.get(term);
				} else {
					postingList = new ArrayList<Posting>();
				}
				postingList.add(postingMap.get(term));
				postingListMap.put(term, postingList);
			}
		}

		Set<WordEntry> wordEntries = new HashSet<WordEntry>();
		for (Entry<String, List<Posting>> entry : postingListMap.entrySet()) {
			WordEntry wordEntry = new WordEntry();
			wordEntry.setPostings(entry.getValue());
			wordEntry.setTerm(entry.getKey());
			wordEntries.add(wordEntry);
		}

		DatabaseUtil db = new DatabaseUtil();
		db.insert(wordEntries);

		print(postingListMap);
	}
	
	private void print(Map<String, List<Posting>> postingListMap) {
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

	public static void main(String[] args) {
		Indexer indexer = new Indexer();
		indexer.index();
	}
}
