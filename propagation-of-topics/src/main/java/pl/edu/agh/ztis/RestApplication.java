package pl.edu.agh.ztis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.ztis.model.Note;
import pl.edu.agh.ztis.repositories.NoteRepository;

/**
 * Simple application providing REST service for accessing notes. 
 * 
 */
@RestController
@SpringBootApplication
public class RestApplication {

	@Autowired
	private NoteRepository noteRepository;

	public static void main(String[] args) {
		 SpringApplication.run(RestApplication.class, args);
	}
	
	@RequestMapping("/")
	public String home() {
		return "REST interface for notes";
	}
	
	@RequestMapping(value = "/id/{noteId}")
	public Note getNoteByID(@PathVariable("noteid") long noteId) {
		return noteRepository.findById(noteId);
	}
	
	@RequestMapping(value="/text/{text}")
	public List<Note> getNotesByContent(@PathVariable("text") String text) {
		return noteRepository.findByText1Containing(text);
	}
	
}
