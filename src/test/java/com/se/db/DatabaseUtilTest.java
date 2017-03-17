package com.se.db;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DatabaseUtilTest {
	private DatabaseUtil databaseUtil;

	@Before
	public void setup() {
		databaseUtil = DatabaseUtil.create();
	}

	@Ignore @Test
	public void searchesTerm() {
		try {
			System.out.println(databaseUtil.searchInvertedIndex("alexand"));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	@Ignore @Test
	public void searchesDocument() {
		try {
			System.out.println(databaseUtil.searchDocument(29250));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}	

}
