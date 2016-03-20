package pl.edu.agh.ztis.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.model.Note;
import pl.edu.agh.ztis.repositories.NoteRepository;

@Component
public class TestNotesBean {
	
	@Autowired
	private NoteRepository noteRepository;
	
	public void saveTestObject() {
		Note note = new Note();
		note.setId(10);
		note.setFeedIdentifier("en_GBR_dailyt_int");
		
		noteRepository.save(note);
		noteRepository.delete(note);
	}
	
	
	
}