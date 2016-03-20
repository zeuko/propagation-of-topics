package pl.edu.agh.ztis;

import java.util.List;

import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.model.Note;


/**
 * Class for parsing notes from text files. 
 * 
 * @author Patrycja
 */
@Component
public class ReadNotesBean {

	public List<Note> getNotesFromFile(String filePath) {
		throw new RuntimeException("Not implemented yet.");
	}
	
}