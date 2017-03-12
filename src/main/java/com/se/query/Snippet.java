package com.se.query;

import java.util.ArrayList;
import java.util.List;

public class Snippet {

	public static SnippetRange findRange(List<List<Integer>> positions) {
		SnippetRange range = new SnippetRange();
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

	private static boolean indicesAreWithinBound(List<List<Integer>> positions,
			List<Integer> indices) {
		for (int i = 0; i < positions.size(); i++) {
			if (indices.get(i) >= positions.get(i).size()) {
				return false;
			}
		}
		return true;
	}

}
