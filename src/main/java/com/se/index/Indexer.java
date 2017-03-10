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
import com.se.data.InvertedIndex;
import com.se.data.parsedDocument;
import com.se.db.DatabaseUtil;
import com.se.file.FileHandler;
import com.se.util.MapPrinter;

public class Indexer {
	private static final String location = FileHandler.configFetch("path");

	public void index() {
		List<File> files = FileHandler.walker(location);
		Map<String, List<Posting>> postingListMap = new HashMap<>();
		Integer docID = 0;
		for (File file : files) {
			docID++;
			parsedDocument pDoc = Tokenizer.tokenize(file, docID, file.getAbsolutePath());
			Map<String, Posting> postingMap = pDoc.getPostingMap();

			for (String term : postingMap.keySet()) {
				List<Posting> postingList;
				if (postingListMap.containsKey(term)) {
					postingList = postingListMap.get(term);
				} else {
					postingList = new ArrayList<>();
				}
				postingList.add(postingMap.get(term));
				postingListMap.put(term, postingList);
			}
		}

		Set<InvertedIndex> wordEntries = new HashSet<>();
		for (Entry<String, List<Posting>> entry : postingListMap.entrySet()) {
			InvertedIndex wordEntry = new InvertedIndex();
			wordEntry.setPostings(entry.getValue());
			wordEntry.setTerm(entry.getKey());
			wordEntries.add(wordEntry);
		}

		DatabaseUtil db = new DatabaseUtil();
		db.insert(wordEntries);

		MapPrinter.print(postingListMap);
	}

	public static void main(String[] args) {
		Indexer indexer = new Indexer();
		indexer.index();
	}

}
