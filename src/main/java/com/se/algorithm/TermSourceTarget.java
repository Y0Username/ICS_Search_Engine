package com.se.algorithm;

public class TermSourceTarget {
	private Integer targetDocId;
	private Integer sourceDocId;
	private String token;
	
	public TermSourceTarget() {
	}

	public Integer getTargetDocId() {
		return targetDocId;
	}

	public void setTargetDocId(Integer targetDocId) {
		this.targetDocId = targetDocId;
	}

	public Integer getSourceDocId() {
		return sourceDocId;
	}

	public void setSourceDocId(Integer sourceDocId) {
		this.sourceDocId = sourceDocId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TermSourceTarget(Integer targetDocId, Integer sourceDocId,
			String token) {
		this.targetDocId = targetDocId;
		this.sourceDocId = sourceDocId;
		this.token = token;
	}

}
