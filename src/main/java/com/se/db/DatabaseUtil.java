package com.se.db;

/**
 * Created by Yathish on 3/2/17.
 */
import java.util.Collection;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.se.data.WordEntry;

public class DatabaseUtil {
	private static final String DATABASE_NAME = "SearchEngine";
	private final Datastore datastore;

	public DatabaseUtil() {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		Morphia morphia = new Morphia();

		morphia.mapPackage("com.se.index");
		morphia.mapPackage("com.se.db");
		morphia.mapPackage("com.se.data");

		datastore = morphia.createDatastore(mongoClient, DATABASE_NAME);
		datastore.ensureIndexes();
		System.out.println("Connect to database successfully");
	}

	public void insert(Collection<WordEntry> wordEntries) {
		datastore.save(wordEntries);
	}
}
