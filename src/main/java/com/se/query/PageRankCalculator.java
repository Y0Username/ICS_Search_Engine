package com.se.query;

import java.util.Map;

import com.se.algorithm.ScoringAlgorithm;
import com.se.data.ScoreType;
import com.se.data.SearchResult;
import com.se.db.DatabaseUtil;

public class PageRankCalculator implements ScoringAlgorithm {

	private Map<Integer, SearchResult> searchResults;
	private DatabaseUtil databaseUtil;

	public PageRankCalculator(Map<Integer, SearchResult> searchResults) {
		this.searchResults = searchResults;
		this.databaseUtil = DatabaseUtil.create();
	}

	@Override
	public Map<Integer, SearchResult> calculate(String query) {
		if(ScoreType.PAGERANK.isDisabled()){
			return searchResults;
		}
		
		for (SearchResult result : searchResults.values()) {
			result.addScore(ScoreType.PAGERANK,
					databaseUtil.getPagerank(result.getDocId()));
		}
		return searchResults;
	}

}
