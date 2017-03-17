package com.se.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final Map<String, Double> HTML_TAGS = initializeTagValues();
	private static Map<String, Double> initializeTagValues() {
		Map<String, Double> tagValues = new HashMap<String, Double>();
		tagValues.put("title", 2.0);
		tagValues.put("h1", 1.0);
		tagValues.put("h2", 1.0);
		tagValues.put("h3", 1.0);
		tagValues.put("h4", 1.0);
		tagValues.put("b", 0.5);
		return tagValues;
	}
}
