package com.se.data;

public enum ScoreType {
	COSINE(0.5), TFIDF(0.5);
	
	private double scoringWeight;
	ScoreType(double scoringFactor) {
		this.scoringWeight = scoringFactor; 
	}
	public double getScoringWeight() {
		return scoringWeight;
	}
}
