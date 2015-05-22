package com.assignment.notes;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;


public class MongoConnection {

	private MongoClientURI uri = null;
	private MongoClient client = null;
	
	
	MongoConnection(final String url) {
		uri = new MongoClientURI(url);
		client = new MongoClient(uri);
	}
	
	public MongoDatabase getDatabase() {
		final MongoDatabase db = client.getDatabase(uri.getDatabase());
		return db;
	}
	
	public void close() {
		client.close();
	}
}
