package com.se.algorithm;

import static com.se.util.Constants.HTML_TAGS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.se.data.InvertedIndex;
import com.se.data.Posting;
import com.se.data.ScoreType;
import com.se.data.SearchResult;
import com.se.index.WordsTokenizer;

public class TagWeightCalculator implements ScoringAlgorithm {

	private Map<Integer, SearchResult> searchResults;
	private Map<String, InvertedIndex> tokenToInvertedIndex;

	public TagWeightCalculator(Map<String, InvertedIndex> tokenToInvertedIndex) {
		this(new HashMap<Integer, SearchResult>(), tokenToInvertedIndex);
	}

	public TagWeightCalculator(Map<Integer, SearchResult> searchresults,
			Map<String, InvertedIndex> tokenToInvertedIndex) {
		this.searchResults = searchresults;
		this.tokenToInvertedIndex = tokenToInvertedIndex;
	}

	@Override
	public Map<Integer, SearchResult> calculate(String query) {
		if (ScoreType.TAGWEIGHT.isDisabled()) {
			return searchResults;
		}

		for (String term : WordsTokenizer.tokenizeWithStemmingFilterStop(query
				.toLowerCase())) {
			InvertedIndex invertedIndex = tokenToInvertedIndex.get(term);
			if (invertedIndex == null) {
				continue;
			}
			List<Posting> postings = invertedIndex.getPostings();
			int noOfSearchResults = Math.min(MAX_SEARCH_RESULTS_PER_TERM,
					postings.size());
			for (int i = 0; i < noOfSearchResults; i++) {
				Posting posting = postings.get(i);
				Integer docId = posting.getDocID();
				SearchResult searchResult;
				if (searchResults.containsKey(docId)) {
					searchResult = searchResults.get(docId);
					calculateTagWeights(posting, searchResult);
				} 
			}
		}

		return searchResults;
	}

	private void calculateTagWeights(Posting posting, SearchResult searchResult) {
		double score = 0.0;
		for (String tag : posting.getTags()) {
			if (HTML_TAGS.containsKey(tag)) {
				score += HTML_TAGS.get(tag);
			}
		}

		searchResult.addScore(ScoreType.TAGWEIGHT, score);
		searchResult.addPositions(posting.getPositions());
	}

}
