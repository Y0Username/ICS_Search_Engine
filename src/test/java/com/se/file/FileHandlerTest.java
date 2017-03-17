package com.se.file;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class FileHandlerTest {
	
	@Ignore @Test
	public void fetchesSnippet() {
		Assert.assertEquals("Ihler Associate Professor ",FileHandler.fetch(3, 5, "64/443", "www.ics.uci.edu/~ihler"));
	}

}
