package com.se.db;

/**
 * Created by Yathish on 3/2/17.
 */
import java.util.Collection;
import java.util.List;

import org.bson.BsonSerializationException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;
import com.se.data.Document;
import com.se.data.InvertedIndex;

public class DatabaseUtil {
	private static final String DATABASE_NAME = "SearchEngine";
	private final Datastore datastore;
	private static DatabaseUtil databaseUtil = null;

	private DatabaseUtil() {
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		Morphia morphia = new Morphia();

		morphia.mapPackage("com.se.index");
		morphia.mapPackage("com.se.db");
		morphia.mapPackage("com.se.data");

		datastore = morphia.createDatastore(mongoClient, DATABASE_NAME);
		datastore.ensureIndexes();
	}

	public void insert(Collection<Object> objects) {
		datastore.save(objects);
	}

	public <T> T searchById(Class<T> cls, Object term) {
		return datastore.get(cls, term);
	}

	public InvertedIndex searchInvertedIndex(String term) {
		return searchById(InvertedIndex.class, term);
	}

	public Document searchDocument(Integer docID) {
		return searchById(Document.class, docID);
	}

	public void insert(Object object) {
		try {
			datastore.save(object);
		} catch (BsonSerializationException exception) {
			System.err.print("Error while inserting to MongoDB");
			System.err.println(exception);
		}
	}

	public <T> List<T> search(Class<T> tClass, String key, Object value) {
		Query<T> query = datastore.createQuery(tClass);
		query.field(key).equals(value);
		return query.asList();
	}
	
	public static DatabaseUtil create(){
		if(databaseUtil == null){
			databaseUtil = new DatabaseUtil();
		}
		return databaseUtil;
	}
	
}
