package com.se.data;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("Corpus")
public class CorpusData {
	@Id
	private String key = "N";
	private Long value;

	public CorpusData() {
		this(0l);
	}

	public CorpusData(long n) {
		value = n;
	}

	public void incrementValue() {
		value++;
	}

}
