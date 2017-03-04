package com.se.index;

/**
 * Created by Yathish on 3/2/17.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

import com.se.data.Posting;

public class Tokenizer {

	public static Map<String, Posting> tokenize(File file, Integer docID) {
		Map<String, Posting> postingMap = new HashMap<String, Posting>();
		String bText;
		try {
			Document doc = Jsoup.parse(file, "UTF-8");
			bText = doc.body().text();
		} catch (Exception e) {
			System.out.println(file);
			System.out.println("Error while parsing: " + e);
			return postingMap;
		}
		bText = bText.toLowerCase();
		Matcher m = Pattern.compile("[^\\W_]+").matcher(bText);
		int pos = 1;
		while (m.find()) {
			String currentWord = m.group(0);
			if (isStopWord(currentWord)) {
				continue;
			}
			
			currentWord = stem(currentWord);

			if (postingMap.containsKey(currentWord)) {
				Posting seenTerm = postingMap.get(currentWord);
				seenTerm.setTermFreq((seenTerm.getTermFreq() + 1));
				List<Integer> posList = seenTerm.getPositions();
				posList.add(pos);
				seenTerm.setPositions(posList);
				postingMap.put(currentWord, seenTerm);
			} else {
				List<Integer> posList = new ArrayList<Integer>();
				posList.add(pos);
				Posting newTerm = new Posting(docID, 1, posList);
				postingMap.put(currentWord, newTerm);
			}
			pos++;
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
		return false;
	}

}