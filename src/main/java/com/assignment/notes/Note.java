package com.assignment.notes;

import java.io.Serializable;

import org.bson.Document;


public class Note implements Serializable {

    private static final long serialVersionUID = -1;
    public static final String KEY_ID = "id";
    public static final String KEY_BODY = "body";

	private Long id;
	private String body;
	
    /**
     * Need a default constructor for Spring to be able to instantiate an object
     */
    public Note() {
	    this.id = -1L;
	    this.body = null;
	}

	/**
	 * Convenience constructor
	 * @param id
	 * @param body
	 */
	public Note(Long id, String body) {
		this.id = id;
		this.body = body;
	}
	
	/**
	 * Convert a Note to a MongoDB Document
	 * @return document
	 */
	public Document toDocument() {
	    return new Document().append(KEY_ID, this.getId()).append(KEY_BODY,  this.getBody());
	}
	
	/**
	 * Convert a MongoDB Document to a Note, (assuming the Document contains KEY_ID and KEY_BODY)
	 * @param doc
	 * @return note
	 */
	public static Note fromDocument(Document doc) {
	    return new Note(doc.getLong(KEY_ID), doc.getString(KEY_BODY));
	}

	public Long getId() {
		return id;
	}

	public String getBody() {
		return body;
	}
	
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
}
