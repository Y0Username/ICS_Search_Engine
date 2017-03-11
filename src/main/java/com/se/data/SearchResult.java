package com.se.data;

public class SearchResult implements Comparable<SearchResult> {
	private Document document;
	private Double score;

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
		if (this.score == arg0.score) {
			return 0;
		}
		return arg0.score > this.score ? 1 : -1;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public String toString() {
		return "SearchResult [document=" + document + ", score=" + score + "]";
	}

}
