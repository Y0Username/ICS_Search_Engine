package com.se.index;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.se.data.Posting;

public class TokenizerTest {
	
	@Test
	public void testTokenize() {
		Map<String, Posting> postingListMap = Tokenizer.tokenize(new File("src/test/resources/4/214"), 1);
		for(Entry<String, Posting> entry : postingListMap.entrySet()){
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}
	}
}
