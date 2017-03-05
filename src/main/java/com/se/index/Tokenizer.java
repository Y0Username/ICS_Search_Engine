package com.se.index;

/**
 * Created by Yathish on 3/2/17.
 */

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import weka.core.Stopwords;

import com.se.data.Posting;

public class Tokenizer {

	public static Map<String, Posting> tokenize(File file, Integer docID) {
		Map<String, Posting> postingMap = new HashMap<String, Posting>();
		String bText;
		try {
			Document doc = Jsoup.parse(file, "UTF-8");
			bText = doc.body().text();
		} catch (IOException e) {
			System.err.println(file);
			System.err.println("Error while parsing. " + e);
			return postingMap;
		}
		bText = bText.toLowerCase();
		Matcher m = Pattern.compile("[^\\W_]+").matcher(bText);
		int wordPosition = 1;
		while (m.find()) {
			String currentWord = m.group(0);
			currentWord = stem(currentWord);
			if (isStopWord(currentWord)) {
				continue;
			}

			if (postingMap.containsKey(currentWord)) {
				Posting seenTerm = postingMap.get(currentWord);
				seenTerm.addPosition(wordPosition);
				postingMap.put(currentWord, seenTerm);
			} else {
				Posting newTerm = new Posting(docID, wordPosition);
				postingMap.put(currentWord, newTerm);
			}
			wordPosition++;
		}
		return postingMap;
	}

	private static String stem(String currentWord) {
		SnowballStemmer snowballStemmer = new englishStemmer();
        snowballStemmer.setCurrent(currentWord);
        snowballStemmer.stem();
        return snowballStemmer.getCurrent();		
	}

	private static boolean isStopWord(String currentWord) {
		if (currentWord.length() < 3) {
			return true;
		}
		return Stopwords.isStopword(currentWord);
	}

}