package com.se.data;

public enum ScoreType {
	COSINE(0.5), TFIDF(0.5), TAGWEIGHT(0.1);

	private double scoringWeight;

	ScoreType(double scoringFactor) {
		this.scoringWeight = scoringFactor;
	}

	public double getScoringWeight() {
		return scoringWeight;
	}
}
