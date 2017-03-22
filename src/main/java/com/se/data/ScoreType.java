package com.se.data;

public enum ScoreType {

	COSINE(100), TFIDF(1), TAGWEIGHT(2.8), PAGERANK(10000000), ANCHOR_TEXT(1);
	
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
