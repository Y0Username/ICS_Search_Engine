package com.se.data;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("AnchorTexts")
public class AnchorTextToken {
	@Id
	private String term;
	private List<Integer> targetDocIds;

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public List<Integer> getTargetDocIds() {
		return targetDocIds;
	}

	public void setTargetDocIds(List<Integer> targetDocIds) {
		this.targetDocIds = targetDocIds;
	}

}
