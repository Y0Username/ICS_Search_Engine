package com.se.data;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class Documents {
	@Id
	private Integer docID;
	private String filePath;
	private String url;
	private Integer docLen;

	public Documents(Integer docID, String filePath, String url, Integer docLen) {
		super();
		this.docID = docID;
		this.filePath = filePath;
		this.url = url;
		this.docLen = docLen;
	}

	public Integer getDocID() {
		return docID;
	}

	public void setDocID(Integer docID) {
		this.docID = docID;
	}

	public String getfilePath() {
		return filePath;
	}

	public void setfilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getDocLen() { return docLen; }

	public void setDocLen(Integer docLen) { this.docLen = docLen; }

}
