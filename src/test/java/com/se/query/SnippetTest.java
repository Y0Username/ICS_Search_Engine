package com.se.query;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class SnippetTest {

	@Test
	public void calculatesRange() {
		List<List<Integer>> positions = new ArrayList<List<Integer>>();
		positions.add(getList(1, 5, 7));
		positions.add(getList(3, 4, 21));
		positions.add(getList(8, 34));
		Snippet snippet = new Snippet();
		SnippetRange range = snippet.findRange(positions);

		Assert.assertEquals(4, range.getFromIndex());
		Assert.assertEquals(8, range.getToIndex());
	}

	private List<Integer> getList(int... inputs) {
		List<Integer> list = new ArrayList<Integer>();
		for (int input : inputs) {
			list.add(input);
		}
		return list;
	}

	@Test
	public void calculatesRange2() {
		List<List<Integer>> positions = new ArrayList<List<Integer>>();
		positions.add(getList(1, 5, 7));
		positions.add(getList(3, 4, 21));
		positions.add(getList(34));
		Snippet snippet = new Snippet();
		SnippetRange range = snippet.findRange(positions);
		Assert.assertEquals(7, range.getFromIndex());
		Assert.assertEquals(34, range.getToIndex());
	}

}
