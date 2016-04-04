package pl.edu.agh.ztis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.exceptions.NotesParsingException;
import pl.edu.agh.ztis.helper.TimeHelper;
import pl.edu.agh.ztis.model.Language;
import pl.edu.agh.ztis.model.Note;


/**
 * Class for parsing notes from text files. 
 * 
 * @author Patrycja
 */
@Component
public class ReadNotesService {
	
	
	public List<Note> getNotesFromFile(String filepath) throws FileNotFoundException {
		File file = new File(filepath);
		return getNotesFromFile(file); 
	}
	
	public List<Note> getNotesFromFile(File file) throws FileNotFoundException {
		return getNotesFromStream(new FileInputStream(file));
	}
	
	public List<Note> getNotesFromStream(InputStream stream) {
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
			return readNotes(br);
			
		} catch (IOException e) {
			throw new NotesParsingException("Exception while parsing data from stream.", e);
		} 
		
	}

	private List<Note> readNotes(BufferedReader br) throws IOException {
		
		@SuppressWarnings("unused")
		String headerLine = br.readLine(); // can be ignored
		
		List<Note> notes = new LinkedList<Note>();

		String line;
		while((line = br.readLine()) != null){
			Note newNote = parseLine(line);
			notes.add(newNote);
		}
		
		return notes;
	}

	private Note parseLine(String line) {
		String array[] = Arrays.stream(line.split("\\t")).map(string -> string.replace("\"", "")).toArray(String[]::new);
		String id = array[0];
		String feed = array[1];
		String time = array[2];
		String text1 = array[3];
		String text2 = array[4];
		String country = array[5];
		String tagEbola = array[6];
		Note note = new Note();
		note.setId(Long.parseLong(id));
		note.setFeedIdentifier(feed);
		note.setTime(TimeHelper.parseStringToJavaDate(time));
		note.setText1(text1);
		note.setText2(text2);
		note.setCountry(country);
		note.setTag(tagEbola);
		if (feed.contains("_"))
			note.setLanguage(Language.valueOf(feed.substring(0, feed.indexOf('_')).toUpperCase()));
		return note;
	}
	
}