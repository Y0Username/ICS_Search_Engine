package com.se.db;

/**
 * Created by Yathish on 3/2/17.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.se.data.Posting;
import com.se.data.WordEntry;

public class DatabaseUtil {
	MongoClient mongoClient;
	MongoDatabase db;
	static final String COLLECTION_NAME = "inverted_index";
	static final String DATABASE_NAME = "SearchEngine";
	final Morphia morphia;
	final Datastore datastore;

	public DatabaseUtil() {
		mongoClient = new MongoClient("localhost", 27017);
		morphia = new Morphia();

		morphia.mapPackage("com.se.index");
		morphia.mapPackage("com.se.db");
		morphia.mapPackage("com.se.data");

		// create the Datastore connecting to the default port on the local host
		datastore = morphia.createDatastore(mongoClient, "SearchEngine");
		datastore.ensureIndexes();
		System.out.println("Connect to database successfully");
	}

	private void createConnection() {
		db.createCollection(COLLECTION_NAME);
	}

	public void insert(Map<String, List<Posting>> map) {
		List<WordEntry> wordEntries = new ArrayList<WordEntry>();
		for (Entry<String, List<Posting>> entry : map.entrySet()) {
			WordEntry wordEntry = new WordEntry();
			wordEntry.setPostings(entry.getValue());
			wordEntry.setTerm(entry.getKey());
			wordEntries.add(wordEntry);
		}
		datastore.save(wordEntries);
	}
}
