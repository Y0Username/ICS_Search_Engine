package com.se.query;

import java.util.Collections;
import java.util.List;

import com.se.algorithm.CosineCalculator;
import com.se.algorithm.ScoringAlgorithm;
import com.se.algorithm.TagWeightCalculator;
import com.se.algorithm.TfIdfCalculator;
import com.se.data.Document;
import com.se.data.SearchResult;
import com.se.file.FileHandler;

public class QueryRunner {

	public List<SearchResult> search(String query) {
		ScoringAlgorithm tfIdfCalculator = new TfIdfCalculator();
		List<SearchResult> results = tfIdfCalculator.calculate(query);
		ScoringAlgorithm cosineCalculator = new CosineCalculator(tfIdfCalculator.getSearchResults());
		results = cosineCalculator.calculate(query);
		ScoringAlgorithm tagWeightCalculator = new TagWeightCalculator(cosineCalculator.getSearchResults());
		results = tagWeightCalculator.calculate(query);
		
		Collections.sort(results);
		int NUMBER_OF_SEARCH_RESULTS = results.size();

		if (results.size() > 10) {
			NUMBER_OF_SEARCH_RESULTS = 10;
		}
		
		List<SearchResult> topKresults = results.subList(0,
				NUMBER_OF_SEARCH_RESULTS);
		
		for (int i = 0; i < NUMBER_OF_SEARCH_RESULTS; i++) {
			SearchResult result = results.get(i);
			SnippetRange range = Snippet.findRange(result.getPositions());
			result.setSnippet(generateSnippet(result.getDocument(), range));
		}				
		return topKresults;
	}

	private String generateSnippet(Document document, SnippetRange range) {
		int MIN_WORDS_IN_SNIPPET = 25;
		int MAX_WORDS_IN_SNIPPET = 40;
		int fromIndex = range.getFromIndex();
		int toIndex = range.getToIndex();
		int numberOfWords = toIndex - fromIndex;
		if (numberOfWords < MIN_WORDS_IN_SNIPPET) {
			int add = (MIN_WORDS_IN_SNIPPET - numberOfWords) / 2;
			fromIndex = fromIndex - add;
			if (fromIndex < 0) {
				toIndex -= fromIndex;
				fromIndex = 0;
			}
			toIndex = toIndex + add;
		}
		if (numberOfWords > MAX_WORDS_IN_SNIPPET) {
			toIndex = fromIndex + MAX_WORDS_IN_SNIPPET;
		}

		String snippet = FileHandler.fetch(fromIndex, toIndex,
				document.getfilePath(), document.getUrl());
		return snippet;
	}

	public static void main(String[] args) {
		QueryRunner queryRunner = new QueryRunner();
		for (SearchResult result : queryRunner.search("Software Engineering")) {
			System.out.println(result);
		}
	}
}


