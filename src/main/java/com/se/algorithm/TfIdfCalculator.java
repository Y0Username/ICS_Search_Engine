package com.se.algorithm;

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
	private Map<String, InvertedIndex> tokenToInvertedIndex;

	public TfIdfCalculator(Map<String, InvertedIndex> tokenToInvertedIndex) {
		this(new HashMap<Integer, SearchResult>(), tokenToInvertedIndex);
	}

	public TfIdfCalculator(Map<Integer, SearchResult> searchresults,
			Map<String, InvertedIndex> tokenToInvertedIndex) {
		this.searchResults = searchresults;
		this.databaseUtil = DatabaseUtil.create();
		this.tokenToInvertedIndex = tokenToInvertedIndex;
	}

	@Override
	public Map<Integer, SearchResult> calculate(String query) {
		if (ScoreType.TFIDF.isDisabled()) {
			return searchResults;
		}

		for (String term : WordsTokenizer.tokenizeWithStemmingFilterStop(query
				.toLowerCase())) {
			InvertedIndex invertedIndex = tokenToInvertedIndex.get(term);
//			InvertedIndex invertedIndex = databaseUtil.searchInvertedIndex(term);			
			if (invertedIndex == null) {
				continue;
			}
			List<Posting> postings = invertedIndex.getPostings();
			int noOfSearchResults = Math.min(MAX_SEARCH_RESULTS_PER_TERM,
					postings.size());
			for (int i = 0; i < noOfSearchResults; i++) {
				Posting posting = postings.get(i);
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

		return searchResults;
	}

	public Map<Integer, SearchResult> getSearchResults() {
		return searchResults;
	}

}
