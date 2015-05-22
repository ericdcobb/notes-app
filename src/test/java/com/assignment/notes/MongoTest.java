package com.assignment.notes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoTest {

    private static final String DB_NAME = "heroku_app21961127";
    private final static String TEST_URI = "mongodb://%s:aoi3g8p6r4u1o5uu442c616rc3@ds031812.mongolab.com:31812/%s";
    private final static String COLLECTION_NAME = "notes";

	private MongoConnection connection;

    @Before
    public void setUp() {
    	final String mongoUri = String.format(TEST_URI, DB_NAME, DB_NAME);
        connection = new MongoConnection(mongoUri);
    }

    @Test
    public void testMongoConnected() {
    	assertNotNull(connection);
    	final MongoDatabase db = connection.getDatabase();
    	assertNotNull(db);
    	assertEquals(DB_NAME, db.getName());
    }

    @Test
    public void testCollectionExists() {
    	final MongoDatabase db = connection.getDatabase();
    	MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME);
    	assertNotNull(collection);
    	final String expectedNamespace = String.format("%s.%s", DB_NAME, COLLECTION_NAME);
    	assertEquals(expectedNamespace, collection.getNamespace().toString());
    }

	@After
	public void tearDown() throws Exception {
		connection.close();
	}
}
