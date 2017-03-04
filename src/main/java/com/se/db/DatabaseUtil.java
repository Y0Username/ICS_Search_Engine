package com.se.db;

/**
 * Created by Yathish on 3/2/17.
 */
import java.util.Collection;

import org.bson.BsonSerializationException;
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
	}

	public void insert(Collection<WordEntry> wordEntries) {
		datastore.save(wordEntries);
	}

	public void insert(WordEntry wordEntry) {
		try {
			datastore.save(wordEntry);
		} catch (BsonSerializationException exception) {
			exception.printStackTrace();
			System.err.print("Error while inserting " + wordEntry.getTerm()
					+ " to MongoDB");
			System.err.print("Error while inserting " + wordEntry.getDocFrq()
					+ " to MongoDB");
		}
	}
}
