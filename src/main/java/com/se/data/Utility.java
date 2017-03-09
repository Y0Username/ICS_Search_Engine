package com.se.data;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class Utility {
	@Id
	private String key = "N";
	private Long value;

	public Utility() {
		this(0l);
	}

	public Utility(long n) {
		value = n;
	}

	public void incrementValue() {
		value++;
	}

}
