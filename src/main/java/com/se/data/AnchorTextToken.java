package com.se.data;

import java.util.HashSet;
import java.util.Set;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("AnchorTexts")
public class AnchorTextToken {
	@Id
	private String term;
	private Set<Integer> targetDocIds;
	
	public AnchorTextToken() {
		targetDocIds = new HashSet<Integer>();
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Set<Integer> getTargetDocIds() {
		return targetDocIds;
	}

	public void setTargetDocIds(Set<Integer> targetDocIds) {
		this.targetDocIds = targetDocIds;
	}

	public void addTargetDocId(int targetDoc) {
		targetDocIds.add(targetDoc);
	}

}
