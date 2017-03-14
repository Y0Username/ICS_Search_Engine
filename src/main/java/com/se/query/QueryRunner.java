package com.se.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.se.data.Document;
import com.se.data.InvertedIndex;
import com.se.data.Posting;
import com.se.data.SearchResult;
import com.se.db.DatabaseUtil;
import com.se.file.FileHandler;
import com.se.index.TfIdf;
import com.se.index.Tokenizer;

public class QueryRunner {
	private DatabaseUtil databaseUtil = new DatabaseUtil();

	public List<SearchResult> search(String queryDoc) {
		Map<String, Integer> query = new HashMap<>();
		for (String term : Tokenizer.tokenize(queryDoc.toLowerCase())) {
			if (query.containsKey(term)) {
				query.put(term, query.get(term)+1);
			} else {
				query.put(term, 1);
			}
		}

		Map<Integer, SearchResult> searchResults = new HashMap<>();
		for (Entry<String, Integer> entry : query.entrySet()) {
			InvertedIndex invertedIndex = databaseUtil
					.searchInvertedIndex(entry.getKey());
			if (invertedIndex == null) { continue; }
			List<Posting> postings = invertedIndex.getPostings();
			/*
			* fetch DF
			* calculate IDF = N/DF
			* calculate w(t,q) = tf*idf
			* */
			Double idf = TfIdf.inverseDocFrequency(invertedIndex.getDocFrq());
			Double qtfIdf = entry.getValue() * idf;
			for (Posting posting : postings) {
				Integer docId = posting.getDocID();
				Double tfIdf = posting.getTfidf();
				SearchResult searchResult;
				if (searchResults.containsKey(docId)) {
					searchResult = searchResults.get(docId);
				} else {
					/*
					if (exists)
					* SearchResult.cosine += w(t,q)*w(t,d)
					 * else
					 *  SearchResult.cosine = w(t,q)*w(t,d)
					* */
					searchResult = new SearchResult();
					Document document = databaseUtil.searchDocument(docId);
					searchResult.setDocument(document);
					searchResults.put(docId, searchResult);
				}
				searchResult.addScore(tfIdf);
				searchResult.addCosine(qtfIdf, tfIdf);
				searchResult.addPositions(posting.getPositions());
			}
		}

		List<SearchResult> results = new ArrayList<>(searchResults.values());
		for(SearchResult result : results){
			result.setCosine(result.getCosine()/result.getDocument().getDocLen());
//			result.addScore(result.getCosine());
//			result.setScore(result.getScore()/result.getDocument().getDocLen());
		}
		Collections.sort(results);
		int NUMBER_OF_SEARCH_RESULTS = 0;
		if(results.size() > 10)
			NUMBER_OF_SEARCH_RESULTS = 10;
		else
			NUMBER_OF_SEARCH_RESULTS = results.size();
		List<SearchResult> topKresults = results.subList(0, NUMBER_OF_SEARCH_RESULTS);
		for (int i=0; i<NUMBER_OF_SEARCH_RESULTS; i++) {
			SearchResult result = results.get(i);
			SnippetRange range = Snippet.findRange(result.getPositions());
			result.setSnippet(generateSnippet(result.getDocument(), range));
		}
		return topKresults;
	}

	//private Double getCosine(){ return 0.0; }

	private String generateSnippet(Document document, SnippetRange range) {
		int MIN_WORDS_IN_SNIPPET = 25;
		int MAX_WORDS_IN_SNIPPET = 40;
		int fromIndex = range.getFromIndex();
		int toIndex = range.getToIndex();
		int numberOfWords = toIndex - fromIndex;
		if(numberOfWords < MIN_WORDS_IN_SNIPPET ){
			int add = (MIN_WORDS_IN_SNIPPET - numberOfWords)/2;
			fromIndex = fromIndex - add;
			if (fromIndex < 0) {
				toIndex -= fromIndex;
				fromIndex = 0;
			}
			toIndex = toIndex + add;					
		}
		if(numberOfWords > MAX_WORDS_IN_SNIPPET){
			toIndex = fromIndex + MAX_WORDS_IN_SNIPPET;
		}
		
		String snippet = FileHandler.fetch(fromIndex,
				toIndex, document.getfilePath(), document.getUrl());
		return snippet;
	}

	public static void main(String[] args) {
		QueryRunner queryRunner = new QueryRunner();
		for (SearchResult result : queryRunner.search("Yathish")) {
			System.out.println(result);
		}
	}
}
