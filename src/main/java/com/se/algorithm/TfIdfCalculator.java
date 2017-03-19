package com.se.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.se.data.Document;
import com.se.data.InvertedIndex;
import com.se.data.Posting;
import com.se.data.ScoreType;
import com.se.data.SearchResult;
import com.se.db.DatabaseUtil;
import com.se.index.WordsTokenizer;

public class TfIdfCalculator implements ScoringAlgorithm {

	private DatabaseUtil databaseUtil;
	private Map<Integer, SearchResult> searchResults;

	public TfIdfCalculator() {
		this(new HashMap<Integer, SearchResult>());
	}

	public TfIdfCalculator(Map<Integer, SearchResult> searchresults) {
		this.searchResults = searchresults;
		this.databaseUtil = DatabaseUtil.create();
	}

	@Override
	public List<SearchResult> calculate(String query) {

		for (String term : WordsTokenizer.tokenizeWithStemmingFilterStop(query.toLowerCase())) {
			InvertedIndex invertedIndex = databaseUtil
					.searchInvertedIndex(term);
			if (invertedIndex == null) {
				continue;
			}
			List<Posting> postings = invertedIndex.getPostings();
			for (Posting posting : postings) {
				Integer docId = posting.getDocID();
				Double tfIdf = posting.getTfidf();
				SearchResult searchResult;
				if (searchResults.containsKey(docId)) {
					searchResult = searchResults.get(docId);
				} else {
					searchResult = new SearchResult();
					Document document = databaseUtil.searchDocument(docId);
					searchResult.setDocument(document);
					searchResults.put(docId, searchResult);
				}
				searchResult.addScore(ScoreType.TFIDF, tfIdf);
				searchResult.addPositions(posting.getPositions());
			}
		}

		List<SearchResult> results = new ArrayList<>(searchResults.values());
		return results;
	}

	public Map<Integer, SearchResult> getSearchResults() {
		return searchResults;
	}

}
