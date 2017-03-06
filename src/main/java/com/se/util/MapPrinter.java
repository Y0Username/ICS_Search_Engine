package com.se.util;

import java.util.List;
import java.util.Map;

import com.se.data.Posting;

public class MapPrinter {

	public static void print(Map<String, List<Posting>> postingListMap) {
		for (String terms : postingListMap.keySet()) {
			System.out.println("Term: " + terms);
			List<Posting> postingList = postingListMap.get(terms);
			for (Posting posting : postingList) {
				System.out.println(posting);
			}
			System.out.println();
		}
	}

}
