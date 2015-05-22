package com.assignment.notes;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

/**
 * class representing a mongoDB connection
 */
public class MongoConnection {

	private MongoClientURI uri = null;
	private MongoClient client = null;	// only one client need be open
	
	
	MongoConnection(final String url) {
		uri = new MongoClientURI(url);
		client = new MongoClient(uri);
	}
	
	public MongoDatabase getDatabase() {
		final MongoDatabase db = client.getDatabase(uri.getDatabase());
		return db;
	}
	
	/**
	 * Clean up client
	 */
	public void close() {
		client.close();
	}
}
