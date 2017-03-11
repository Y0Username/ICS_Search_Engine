package com.se.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.se.data.Document;
import com.se.data.InvertedIndex;
import com.se.data.Posting;
import com.se.data.SearchResult;
import com.se.db.DatabaseUtil;
import com.se.index.Tokenizer;

public class QueryRunner {
	private DatabaseUtil databaseUtil = new DatabaseUtil();

	public List<SearchResult> search(String query) {
		Map<Integer, SearchResult> searchResults = new HashMap<>();
		for (String term : Tokenizer.tokenize(query)) {
			InvertedIndex invertedIndex = databaseUtil
					.searchInvertedIndex(term);
			List<Posting> postings = invertedIndex.getPostings();
			for (Posting posting : postings) {
				Integer docId = posting.getDocID();
				Double tfIdf = posting.getTfidf();
				if (searchResults.containsKey(docId)) {
					searchResults.get(docId).addScore(tfIdf);
				} else {
					SearchResult searchResult = new SearchResult();
					Document document = databaseUtil.searchDocument(docId);
					searchResult.setDocument(document);
					searchResult.setScore(tfIdf);
					searchResults.put(docId, searchResult);
				}
			}
		}

		List<SearchResult> results = new ArrayList<SearchResult>(
				searchResults.values());
		Collections.sort(results);
		return results;
	}

	public static void main(String[] args) {
		QueryRunner queryRunner = new QueryRunner();
		System.out.println(queryRunner.search("alexander ihler"));
	}
}
