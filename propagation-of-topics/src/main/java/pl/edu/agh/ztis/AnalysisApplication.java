package pl.edu.agh.ztis;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.kohsuke.args4j.CmdLineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.components.AnalysisTool;
import pl.edu.agh.ztis.components.AnalysisTool.TimeAnalysisWindow;
import pl.edu.agh.ztis.model.Language;
import pl.edu.agh.ztis.model.Note;
import pl.edu.agh.ztis.repositories.NoteRepository;

@Component
public class AnalysisApplication {

	@Autowired
	AnalysisTool analysisTool;
	@Autowired
	NoteRepository noteRepository;
	
	public static void main(String[] args) throws FileNotFoundException, CmdLineException {
	        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
	        AnalysisApplication analysis = context.getBean(AnalysisApplication.class);
	        analysis.run();
	  
	}

	private void run() {
		List<Note> allNotes = getNotes();
		
		List<Note> notes = getCluster(Arrays.asList(new String[]{"paris charlie hebdo","charlie hebdo", "hebdo"}), allNotes);
		executeCountryAnalysisAndSaveResults(notes, "analysis_sc_charlie_hebdor.txt");
		
		
		List<Note> notesBomber = getTotalCluster(Arrays.asList(new String[]{"obama","syria"}), allNotes);
		executeCountryAnalysisAndSaveResults(notesBomber, "analysis_sc_obama_syria.txt");
		
		
		List<Note> notesEbola = getCluster(Arrays.asList(new String[]{"ebola", "ebola epidemic", "ebola virus", "ebola outbreak", "deadly ebola virus"}), allNotes);
		executeCountryAnalysisAndSaveResults(notesEbola, "analysis_sc_ebola.txt");
		
		
		 List<Note> notesPistorius = getCluster(Arrays.asList(new String[]{"pistorius", "girlfriend", "murder trial", "athlete", "paralympic athlete oscar"}), allNotes);		
		 executeCountryAnalysisAndSaveResults(notesPistorius, "analysis_sc_pistorius.txt");
	}

	private void executeCountryAnalysisAndSaveResults(List<Note> notes, String filename) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			Map<String, Integer> result = analysisTool.getNotesByCountry(notes);
			writer.write(notes.size() + "\n");
			for (Entry<String, Integer> entry : result.entrySet()) {
				writer.write(entry.getKey().toString()+ " " +entry.getValue() +"\n" );
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	
	private void executeTimeAnalysisAndSaveResults(List<Note> notes, String filename) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(filename, "UTF-8");
			Map<DateTime, Integer> result = analysisTool.getNotesTimeAnalysis(notes, TimeAnalysisWindow.WEEK);
			writer.write(notes.size() + "\n");
			for (Entry<DateTime, Integer> entry : result.entrySet()) {
				writer.write(entry.getKey().toString()+ " " +entry.getValue() +"\n" );
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private List<Note> getNotes() {
		return noteRepository.findByLanguage(Language.EN);
		
	}
	
	// zwraca notki zawierajace tagi z listy tags
	private List<Note> getTotalCluster(List<String> tags, List<Note> notes) {
		return notes.stream().filter(note -> note.getAllTags().containsAll(tags)).collect(Collectors.toList());
	}
	
	private List<Note> getCluster(List<String> tags, List<Note> notes) {
		return notes.stream().filter(note -> hasAnyOfTags(tags, note.getAllTags())).collect(Collectors.toList());
	}

	private boolean hasAnyOfTags(List<String> tags, Set<String> allTags) {
		for (String string : allTags) {
			if (tags.contains(string)) {
				return true;
			}
		}
		return false;
	}
}
