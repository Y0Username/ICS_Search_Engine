package com.se.algorithm;

import java.util.Map;

import com.se.data.SearchResult;

public interface ScoringAlgorithm {
	public static final int MAX_SEARCH_RESULTS_PER_TERM = 10000;

	public Map<Integer, SearchResult> calculate(String query);

}
