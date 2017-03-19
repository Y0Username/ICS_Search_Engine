package com.se.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("AnchorTexts")
public class AnchorTextToken {
	@Id
	private String term;
	private List<AnchorPosting> targetAnchorTexts;

	public AnchorTextToken() {
		targetAnchorTexts = new ArrayList<AnchorPosting>();
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public List<AnchorPosting> getTargetDocIds() {
		return targetAnchorTexts;
	}

	public void setTargetDocIds(List<AnchorPosting> targetDocIds) {
		this.targetAnchorTexts = targetDocIds;
	}

	public void addTargetDocId(AnchorPosting targetDoc) {
		targetAnchorTexts.add(targetDoc);
	}

	public void sortTargetDocIds() {
		Collections.sort(targetAnchorTexts);
		
	}

}