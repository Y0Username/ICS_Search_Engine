package com.se.servlets;

import com.se.data.SearchResult;

public class SearchResultsUI {
	private String snippet;
	private String score;
	private String url;
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public SearchResultsUI() {

	}
	
	public SearchResultsUI(SearchResult searchResult) {
		snippet = searchResult.getSnippet();
		score = searchResult.getTotalScore().toString();
		url = searchResult.getUrl();
		title = searchResult.getUrl();
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
