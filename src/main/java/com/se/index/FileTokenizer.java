package com.se.index;

import static com.se.util.Constants.HTML_TAGS;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.se.data.Posting;

public class FileTokenizer {
	private int noOfTokens;
	private Map<String, Posting> postingMap;

	public FileTokenizer(File file, Integer docID, String url)
			throws IOException {
		Document document = Jsoup.parse(file, "UTF-8", url);
		postingMap = new HashMap<>();
		List<String> tokens = WordsTokenizer.tokenize(document.text());
		noOfTokens = tokens.size();
		postingMap = createPostings(tokens, docID);
		extractTags(postingMap, document);
	}

	public Map<String, Posting> getPostingMap() {
		return postingMap;
	}

	private void extractTags(Map<String, Posting> postingMap, Document doc) {
		for (Element element : doc.getAllElements()) {
			String tag = element.tagName();
			if(!HTML_TAGS.containsKey(tag)){
				continue;
			}
			List<String> tokens = WordsTokenizer.tokenize(element.ownText());
			for (String token : tokens) {
				Posting posting = postingMap.get(token);
				if (posting == null) {
					continue;
				}
				posting.addTag(tag);
			}
		}
	}
	
	private Map<String, Posting> createPostings(Collection<String> tokens,
			Integer docID) {
		Map<String, Posting> postingMap = new HashMap<>();
		Integer wordPosition = 0;
		for (String token : tokens) {
			if (postingMap.containsKey(token)) {
				Posting seenTerm = postingMap.get(token);
				seenTerm.addPosition(wordPosition);
			} else {
				Posting newTerm = new Posting(docID, wordPosition);
				postingMap.put(token, newTerm);
			}
			wordPosition++;
		}
		return postingMap;
	}

	public int getNoOfTokens() {
		return noOfTokens;
	}

}