package pl.edu.agh.ztis;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.kohsuke.args4j.CmdLineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.components.AnalysisTool;
import pl.edu.agh.ztis.components.AnalysisTool.TimeAnalysisWindow;
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
		List<Note> notes = getNotes();
		System.out.println(notes.size());
		Map<DateTime, Integer> result = analysisTool.getNotesTimeAnalysis(notes, TimeAnalysisWindow.WEEK);
		System.out.println(result);
	}

	private List<Note> getNotes() {
		return noteRepository.findByText1Containing("Obama");
		
	}
	
	// zwraca notki zawierajace tagi z listy tags
	private List<Note> getCluster(List<String> tags, List<Note> notes) {
		return notes.stream().filter(note -> note.getAllTags().containsAll(tags)).collect(Collectors.toList());
	}
}
