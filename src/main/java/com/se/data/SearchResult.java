package com.se.data;

import java.util.ArrayList;
import java.util.List;

public class SearchResult implements Comparable<SearchResult> {
	private Document document;
	private Double score;
	private List<List<Integer>> positions;
	private String snippet;

	public SearchResult() {
		score = 0.0;
		positions = new ArrayList<>();
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public void addScore(Double tfIdf) {
		this.score += tfIdf;
	}

	@Override
	public int compareTo(SearchResult arg0) {
		return Double.compare(arg0.score, this.score);
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public void addPositions(List<Integer> position) {
		this.positions.add(position);
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public List<List<Integer>> getPositions() {
		return positions;
	}

	public void setPositions(List<List<Integer>> positions) {
		this.positions = positions;
	}

	@Override
	public String toString() {
		return "SearchResult [document=" + document + ", score=" + score
				+ ", positions=" + positions + ", snippet=" + snippet + "]";
	}

}
