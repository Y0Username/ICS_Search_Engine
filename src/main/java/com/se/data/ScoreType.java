package com.se.data;

public enum ScoreType {
	COSINE(0.4), TFIDF(0), TAGWEIGHT(0.15), PAGERANK(0.4), ANCHOR_TEXT(0.05);

	private double scoringWeight;

	ScoreType(double scoringFactor) {
		this.scoringWeight = scoringFactor;
	}

	public double getScoringWeight() {
		return scoringWeight;
	}

	public boolean isDisabled() {
		return scoringWeight == 0;
	}
}
