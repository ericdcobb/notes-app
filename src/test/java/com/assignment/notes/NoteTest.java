package com.assignment.notes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.bson.Document;
import org.junit.Test;

public class NoteTest {

	@Test
	public void testDefaultConstructor() {
		final Note note = new Note();
		assertNotNull(note);
		assertEquals(note.getId(), Long.valueOf(-1));
		assertNull(note.getBody());
	}

	@Test
	public void testNoteBean() {
		final Note note = new Note(134L, "hello");
		assertNotNull(note);
		assertEquals(note.getId(), Long.valueOf(134));
		assertEquals(note.getBody(), "hello");
	}
	
	@Test
	public void testNoteToDocumentConversion() {
		final Long noteId = 123L;
		final String noteBody = "this is a test note.";
		final Note note = new Note(noteId, noteBody);
		final Document doc = note.toDocument();
		assertNotNull(doc);
		final Long docId = (Long) doc.get(Note.KEY_ID);
		final String docBody = (String) doc.get(Note.KEY_BODY);
		assertNotNull(docId);
		assertNotNull(docBody);
		assertEquals(noteBody, docBody);
		assertEquals(docId, noteId);
	}
	
	@Test
	public void testDocumentToNoteConversion() {
		final Long docId = 345L;
		final String docBody = "this is a test document.";
		final Document doc = new Document().append(Note.KEY_ID, docId).append(Note.KEY_BODY, docBody);
		final Note note = Note.fromDocument(doc);
		assertNotNull(note);
		assertEquals(docId, note.getId());
		assertEquals(docBody, note.getBody());
	}
}
