package com.se.index;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;

import com.se.data.Posting;
import com.se.data.parsedDocument;

public class TokenizerTest {
	
	@Ignore @Test
	public void testTokenize() throws IOException {
		parsedDocument pDoc = Tokenizer.tokenize(new File("src/test/resources/4/214"), 1, "www.214.ics.edu");
		Map<String, Posting> postingListMap = pDoc.getPostingMap();
		for(Entry<String, Posting> entry : postingListMap.entrySet()){
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		}
	}
}
