package com.se.data;

import java.util.ArrayList;
import java.util.List;

public class AnchorPosting implements Comparable<AnchorPosting>{
	private Integer docID;
	private List<Integer> sourceDocIds;

	public AnchorPosting() {
		this.sourceDocIds = new ArrayList<Integer>();
	}

	public Integer getDocID() {
		return docID;
	}

	public void setDocID(Integer docID) {
		this.docID = docID;
	}

	public List<Integer> getSourceDocIds() {
		return sourceDocIds;
	}

	public void setSourceDocIds(List<Integer> sourceDocIds) {
		this.sourceDocIds = sourceDocIds;
	}

	public void addSourceDocId(Integer sourceDocId) {
		this.sourceDocIds.add(sourceDocId);
	}

	@Override
	public int compareTo(AnchorPosting arg0) {
		return Integer.compare(arg0.sourceDocIds.size(), this.sourceDocIds.size());
	}

}
