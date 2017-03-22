package com.se.data;

public enum ScoreType {
	COSINE(0.5), TFIDF(0.5), TAGWEIGHT(0.5), PAGERANK(1000), ANCHOR_TEXT(10);

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
