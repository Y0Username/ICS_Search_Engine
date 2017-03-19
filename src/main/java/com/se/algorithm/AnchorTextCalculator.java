//package com.se.algorithm;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import com.se.data.AnchorTextToken;
//import com.se.data.Document;
//import com.se.data.InvertedIndex;
//import com.se.data.Posting;
//import com.se.data.ScoreType;
//import com.se.data.SearchResult;
//import com.se.db.DatabaseUtil;
//import com.se.index.WordsTokenizer;
//
//public class AnchorTextCalculator implements ScoringAlgorithm {
//	private DatabaseUtil databaseUtil;
//	private Map<Integer, SearchResult> searchResults;
//
//	public AnchorTextCalculator() {
//		this(new HashMap<Integer, SearchResult>());
//	}
//
//	public AnchorTextCalculator(Map<Integer, SearchResult> searchresults) {
//		this.searchResults = searchresults;
//		this.databaseUtil = DatabaseUtil.create();
//	}
//
//	@Override
//	public List<SearchResult> calculate(String query) {
//		for (String term : WordsTokenizer.tokenize(query.toLowerCase())) {
//			AnchorTextToken anchorTextToken = databaseUtil
//					.searchAnchorText(term);
//			if (anchorTextToken == null) {
//				continue;
//			}
//			Set<Integer> docIds = anchorTextToken.getTargetDocIds();
//			for (Integer docId : docIds) {
//				SearchResult searchResult;
//				if (searchResults.containsKey(docId)) {
//					searchResult = searchResults.get(docId);
//				} else {
//					searchResult = new SearchResult();
//					Document document = databaseUtil.searchDocument(docId);
//					searchResult.setDocument(document);
//					searchResults.put(docId, searchResult);
//				}
//				searchResult.addScore(ScoreType.ANCHOR_TEXT, score);
//				searchResult.addPositions(posting.getPositions());
//			}
//		}
//
//		List<SearchResult> results = new ArrayList<>(searchResults.values());
//		return results;
//	}
//
//	@Override
//	public Map<Integer, SearchResult> getSearchResults() {
//		return searchResults;
//	}
//
//}
