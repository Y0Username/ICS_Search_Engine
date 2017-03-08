package com.se.data;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class Document {
	@Id
	private Integer docID;
	private String filePath;
	private String url;

	public Document(Integer docID, String filePath, String url) {
		super();
		this.docID = docID;
		this.filePath = filePath;
		this.url = url;
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

}
