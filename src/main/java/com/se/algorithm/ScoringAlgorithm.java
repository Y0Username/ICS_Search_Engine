package com.se.algorithm;

import java.util.List;

import com.se.data.SearchResult;

public interface ScoringAlgorithm {

	public List<SearchResult> calculate(String query);

}
