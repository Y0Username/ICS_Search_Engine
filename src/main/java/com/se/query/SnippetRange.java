package com.se.query;

public class SnippetRange {
	private int fromIndex;
	private int toIndex;

	public int getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}

	public int getToIndex() {
		return toIndex;
	}

	public void setToIndex(int toIndex) {
		this.toIndex = toIndex;
	}

	@Override
	public String toString() {
		return "SnippetRange [fromIndex=" + fromIndex + ", toIndex=" + toIndex
				+ "]";
	}

}
