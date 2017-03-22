package com.se.algorithm;

import java.util.Map;

import com.se.data.SearchResult;

public interface ScoringAlgorithm {

	public Map<Integer, SearchResult> calculate(String query);

}
