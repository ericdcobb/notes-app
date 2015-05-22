package com.assignment.notes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * Spring Controller class.  This class handles the REST requests for the "/api/notes" resource.
 */
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Value("${mongodb.uri}")
    private String mongoUri;

    private final static Logger LOG = Logger.getLogger(NoteController.class);
    private static final String NOTES_COLLECTION = "notes";
    private final AtomicLong counter = new AtomicLong();
    private MongoConnection connection;


	/**
	 * Initialize a mongoDB connection. Using PostConstruct to make sure we can
	 * grab the property from application.properties.
	 */
    @PostConstruct
    public void initialize() {
        connection = new MongoConnection(mongoUri);
        updateCounter();	// find the max note id in the database and update the counter with it 
    }

    /* 
     * Clean up the mongoDB connection.
     * 
     * @see java.lang.Object#finalize()
     */
    protected void finalize() {
        connection.close();
    }
    
    /**
     * Handle GET requests that can return multiple notes (i.e. /api/notes and /api/notes?query=)
     * 
     * @param filter the query filter provided, if any.
     * @return list of notes
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Note> findAll(@RequestParam(value="query", required=false) String filter) {
        MongoDatabase db = connection.getDatabase();
        MongoCollection<Document> notes = db.getCollection(NOTES_COLLECTION);
        FindIterable<Document> docs = null;

        if (filter == null) {
            docs = notes.find();
        }
        else {
            Document regexQuery = new Document();
            // compose a regex case-insensitive expression
            regexQuery.put("body", new Document("$regex", filter).append("$options", "i"));
            docs = notes.find(regexQuery);
        }

        ArrayList<Note> list = new ArrayList<Note>();
        MongoCursor<Document> cursor = docs.iterator();
        // convert Document to Note for all results
        while (cursor.hasNext()) {
            final Note note = Note.fromDocument(cursor.next());
            list.add(note);
        }
        cursor.close();

        if (list.size() == 0) {
            throw new NoteNotFoundException(filter);
        }

        return list;
    }

    /**
     * Handle GET requests for individual notes (i.e.. /api/notes/{id})
     * 
     * @param id the note id to retrieve
     * @return retrieved note
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Note findOne(@PathVariable("id") Long id) {
        MongoDatabase db = connection.getDatabase();
        MongoCollection<Document> notes = db.getCollection(NOTES_COLLECTION);
        Note note = null;

        if (id > 0) {
            Document query = new Document("id", id);
            FindIterable<Document> docs = notes.find(query);
            Document doc = docs.first();
            if (doc != null) {
                note = Note.fromDocument(doc);
            }
        }

        if (note == null) {
            throw new NoteNotFoundException(id);
        }

        return note;
    }

    /**
     * Handle POST requests for creating new notes.  Request Body must include at least "body" attribute.
     * 
     * @param note the note supplied in the request body
     * @return the created note
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Note create(@RequestBody Note note) {
        if (note == null || note.getBody() == null || note.getBody().isEmpty()) {
            throw new NoBodySuppliedException();
        }

        MongoDatabase db = connection.getDatabase();
        MongoCollection<Document> notes = db.getCollection(NOTES_COLLECTION);
        note.setId(counter.incrementAndGet());
        Document newDoc = note.toDocument();
        notes.insertOne(newDoc);

        return note;
    }

    /**
     * Update the counter to the last available id when starting the app.
     */
    private void updateCounter() {
        MongoDatabase db = connection.getDatabase();
        MongoCollection<Document> notes = db.getCollection(NOTES_COLLECTION);

        Document sort = new Document("id", -1);

        FindIterable<Document> docs = notes.find().sort(sort).limit(1);
        if (docs != null && docs.first() != null) {
            Long maxId = (Long) docs.first().get(Note.KEY_ID);
            LOG.info("+++ Setting counter to: " + maxId);
            counter.set(maxId);
        }
    }
}
