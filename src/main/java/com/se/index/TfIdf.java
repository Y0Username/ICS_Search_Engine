package com.se.index;

public class TfIdf {
	private static Long N = 37419l;

	public static double termFrequency(Integer termFrequency) {
		if(termFrequency == 0){
			return 0;
		}
		return (1 + Math.log10(termFrequency));
	}

	public static double inverseDocFrequency(Integer docFrequency) {
		if(docFrequency == 0){
			return 0;
		}
		return Math.log10(N.doubleValue() / docFrequency);
	}

}
