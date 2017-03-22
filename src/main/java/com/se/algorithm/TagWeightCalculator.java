package com.se.algorithm;

import static com.se.util.Constants.HTML_TAGS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.se.data.Document;
import com.se.data.InvertedIndex;
import com.se.data.Posting;
import com.se.data.ScoreType;
import com.se.data.SearchResult;
import com.se.db.DatabaseUtil;
import com.se.index.WordsTokenizer;

public class TagWeightCalculator implements ScoringAlgorithm {

	private DatabaseUtil databaseUtil;
	private Map<Integer, SearchResult> searchResults;
	private static final int MAX_SEARCH_RESULTS_PER_TERM = 50;

	public TagWeightCalculator() {
		this(new HashMap<Integer, SearchResult>());
	}

	public TagWeightCalculator(Map<Integer, SearchResult> searchresults) {
		this.searchResults = searchresults;
		this.databaseUtil = DatabaseUtil.create();
	}

	@Override
	public Map<Integer, SearchResult> calculate(String query) {
		if (ScoreType.TAGWEIGHT.isDisabled()) {
			return searchResults;
		}

		for (String term : WordsTokenizer.tokenizeWithStemmingFilterStop(query
				.toLowerCase())) {
			InvertedIndex invertedIndex = databaseUtil
					.searchInvertedIndex(term);
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
				} else {
					searchResult = new SearchResult();
					Document document = databaseUtil.searchDocument(docId);
					searchResult.setDocument(document);
					searchResults.put(docId, searchResult);
				}
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

		return searchResults;
	}

}
