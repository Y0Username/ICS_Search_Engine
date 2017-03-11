package com.se.index;

public class TfIdf {
	private static Long N = 37419l;

	public static double termFrequency(Integer termFrequency) {
		return (1 + Math.log10(termFrequency));
	}

	public static double inverseDocFrequency(Integer docFrequency) {
		return Math.log10(N.doubleValue() / docFrequency);
	}

}
