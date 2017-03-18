package com.se.algorithm;

import java.util.ArrayList;
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
import com.se.index.StringTokenizer;
import com.se.index.TfIdf;

public class CosineCalculator implements ScoringAlgorithm {

	private DatabaseUtil databaseUtil;
	private Map<Integer, SearchResult> searchResults;

	public CosineCalculator() {
		this(new HashMap<Integer, SearchResult>());
	}

	public CosineCalculator(Map<Integer, SearchResult> searchresults) {
		this.searchResults = searchresults;
		this.databaseUtil = DatabaseUtil.create();
	}

	@Override
	public List<SearchResult> calculate(String query) {
		Map<String, Integer> queryTf = new HashMap<>();
		for (String term : StringTokenizer.tokenize(query.toLowerCase())) {
			if (queryTf.containsKey(term)) {
				queryTf.put(term, queryTf.get(term) + 1);
			} else {
				queryTf.put(term, 1);
			}
		}

		for (Entry<String, Integer> entry : queryTf.entrySet()) {
			InvertedIndex invertedIndex = databaseUtil
					.searchInvertedIndex(entry.getKey());
			if (invertedIndex == null) {
				continue;
			}
			List<Posting> postings = invertedIndex.getPostings();
			Double idf = TfIdf.inverseDocFrequency(invertedIndex.getDocFrq());
			Double qtfIdf = entry.getValue() * idf;
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
				searchResult.addScore(ScoreType.COSINE, qtfIdf * tfIdf);
				searchResult.addPositions(posting.getPositions());
			}
		}

		return normalize();
	}

	private List<SearchResult> normalize() {
		List<SearchResult> results = new ArrayList<>(searchResults.values());
		for (SearchResult result : results) {
			result.setScore(ScoreType.COSINE, result.getScore(ScoreType.COSINE)
					/ result.getDocument().getDocLen());
		}
		return results;
	}

	@Override
	public Map<Integer, SearchResult> getSearchResults() {
		return searchResults;
	}

}
