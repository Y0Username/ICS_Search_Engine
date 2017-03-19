package com.se.query;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.se.algorithm.AnchorTextCalculator;
import com.se.algorithm.CosineCalculator;
import com.se.algorithm.ScoringAlgorithm;
import com.se.algorithm.TagWeightCalculator;
import com.se.algorithm.TfIdfCalculator;
import com.se.data.Document;
import com.se.data.ScoreType;
import com.se.data.SearchResult;
import com.se.db.DatabaseUtil;
import com.se.file.FileHandler;
import com.se.index.WordsTokenizer;
import com.se.util.NDCG;

public class QueryRunner {

	public List<SearchResult> search(String query) {
		ScoringAlgorithm tfIdfCalculator = new TfIdfCalculator();
		List<SearchResult> results = tfIdfCalculator.calculate(query);
		ScoringAlgorithm cosineCalculator = new CosineCalculator(
				tfIdfCalculator.getSearchResults());
		results = cosineCalculator.calculate(query);
		ScoringAlgorithm anchorTextCalculator = new AnchorTextCalculator(
				cosineCalculator.getSearchResults());
		anchorTextCalculator.calculate(query);
		ScoringAlgorithm tagWeightCalculator = new TagWeightCalculator(
				anchorTextCalculator.getSearchResults());
		results = tagWeightCalculator.calculate(query);

		Collections.sort(results);
		int NUMBER_OF_SEARCH_RESULTS = results.size();

		if (results.size() > 30) {
			NUMBER_OF_SEARCH_RESULTS = 30;
		}

		List<SearchResult> topKresults = results.subList(0,
				NUMBER_OF_SEARCH_RESULTS);

		DatabaseUtil databaseUtil = DatabaseUtil.create();
		for (SearchResult result : topKresults) {
			result.addScore(ScoreType.PAGERANK,
					databaseUtil.getPagerank(result.getDocId()));
		}

		if (topKresults.size() > 10) {
			NUMBER_OF_SEARCH_RESULTS = 10;
		}

		Collections.sort(topKresults);
		List<SearchResult> finalTopK = topKresults.subList(0,
				NUMBER_OF_SEARCH_RESULTS);

		for (int i = 0; i < NUMBER_OF_SEARCH_RESULTS; i++) {
			SearchResult result = finalTopK.get(i);
			Document document = result.getDocument();
			String snippet = Snippet.generate(document, result.getPositions());
			result.setSnippet(highlight(snippet, query));
			result.setTitle(FileHandler.getTitle(document.getfilePath(),
					document.getUrl()));
		}
		databaseUtil.close();
		return finalTopK;
	}

	private String highlight(String snippet, String query) {
		List<String> tokensList = WordsTokenizer.tokenize(query);
		Set<String> queryTokens = new HashSet<>();
		for (String string : tokensList) {
			queryTokens.add(string);
		}
		List<String> snippetTokensList = WordsTokenizer.tokenize(snippet);
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : snippetTokensList) {
			if (queryTokens.contains(string)) {
				stringBuilder.append("<b>" + string + "</b>");
			} else {
				stringBuilder.append(string);
			}
			stringBuilder.append(" ");
		}

		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		String query = "crista lopes";
		QueryRunner queryRunner = new QueryRunner();
		List<SearchResult> SearchResults = queryRunner.search(query);
		for (SearchResult result : SearchResults) {
			System.out.println(result);
		}
		NDCG ndcg = new NDCG();
		ndcg.findNDCG(query, SearchResults);
	}
}
