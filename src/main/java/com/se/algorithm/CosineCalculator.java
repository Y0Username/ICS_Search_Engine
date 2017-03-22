package com.se.algorithm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.se.data.Document;
import com.se.data.InvertedIndex;
import com.se.data.Posting;
import com.se.data.ScoreType;
import com.se.data.SearchResult;
import com.se.db.DatabaseUtil;
import com.se.index.TfIdf;
import com.se.index.WordsTokenizer;

public class CosineCalculator implements ScoringAlgorithm {

	private DatabaseUtil databaseUtil;
	private Map<Integer, SearchResult> searchResults;
	private Map<String, InvertedIndex> tokenToInvertedIndex;

	public CosineCalculator(Map<String, InvertedIndex> tokenToInvertedIndex) {
		this(new HashMap<Integer, SearchResult>(), tokenToInvertedIndex);
	}

	public CosineCalculator(Map<Integer, SearchResult> searchresults, Map<String, InvertedIndex> tokenToInvertedIndex) {
		this.searchResults = searchresults;
		this.databaseUtil = DatabaseUtil.create();
		this.tokenToInvertedIndex = tokenToInvertedIndex;
	}

	@Override
	public Map<Integer, SearchResult> calculate(String query) {
		if (ScoreType.COSINE.isDisabled()) {
			return searchResults;
		}

		Map<String, Integer> queryTf = new HashMap<>();
		for (String term : WordsTokenizer.tokenizeWithStemmingFilterStop(query
				.toLowerCase())) {
			if (queryTf.containsKey(term)) {
				queryTf.put(term, queryTf.get(term) + 1);
			} else {
				queryTf.put(term, 1);
			}
		}

		for (Entry<String, Integer> entry : queryTf.entrySet()) {
			InvertedIndex invertedIndex = tokenToInvertedIndex.get(entry.getKey());
//			InvertedIndex invertedIndex = databaseUtil.searchInvertedIndex(entry.getKey());			
			if (invertedIndex == null) {
				continue;
			}
			List<Posting> postings = invertedIndex.getPostings();
			Double idf = TfIdf.inverseDocFrequency(invertedIndex.getDocFrq());
			Double qtfIdf = entry.getValue() * idf;
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
				searchResult.addScore(ScoreType.COSINE, qtfIdf * tfIdf);
				searchResult.addPositions(posting.getPositions());
			}
		}

		for (SearchResult result : searchResults.values()) {
			result.setScore(ScoreType.COSINE, result.getScore(ScoreType.COSINE)
					/ result.getDocument().getDocLen());
		}

		return searchResults;
	}

}
