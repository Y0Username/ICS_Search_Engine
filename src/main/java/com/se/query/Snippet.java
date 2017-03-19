package com.se.query;

import java.util.ArrayList;
import java.util.List;

import com.se.data.Document;
import com.se.file.FileHandler;

public class Snippet {
	private static Snippet snippet = null;
	
	Snippet() {
		
	}

	SnippetRange findRange(List<List<Integer>> positions) {		
		SnippetRange range = new SnippetRange();
		if(positions.isEmpty()){
			return range;
		}
		List<Integer> indices = new ArrayList<>(positions.size());
		for (int i = 0; i < positions.size(); i++) {
			indices.add(0);
		}
		int minimumDistance = Integer.MAX_VALUE;
		while (indicesAreWithinBound(positions, indices)) {
			int min = Integer.MAX_VALUE;
			int minIndex = 0;
			int max = Integer.MIN_VALUE;
			for (int i = 0; i < positions.size(); i++) {
				int index = indices.get(i);
				int position = positions.get(i).get(index);
				if (position < min) {
					min = position;
					minIndex = i;
				}
				max = Math.max(max, position);
			}
			if ((max - min) < minimumDistance) {
				minimumDistance = max - min;
				range.setFromIndex(min);
				range.setToIndex(max);
			}
			indices.set(minIndex, indices.get(minIndex) + 1);
		}
		return range;
	}

	public static String generate(Document document,
			List<List<Integer>> positions) {
		if(snippet == null){
			snippet = new Snippet();
		}
		SnippetRange range = snippet.findRange(positions);
		return snippet.fetchSnippet(document, range);
	}

	private String fetchSnippet(Document document, SnippetRange range) {
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

	private boolean indicesAreWithinBound(List<List<Integer>> positions,
			List<Integer> indices) {
		for (int i = 0; i < positions.size(); i++) {
			if (indices.get(i) >= positions.get(i).size()) {
				return false;
			}
		}
		return true;
	}

}
