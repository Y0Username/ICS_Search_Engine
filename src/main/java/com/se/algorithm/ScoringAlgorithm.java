package com.se.algorithm;

import java.util.List;
import java.util.Map;

import com.se.data.SearchResult;

public interface ScoringAlgorithm {

	public List<SearchResult> calculate(String query);

	public Map<Integer, SearchResult> getSearchResults();
}
