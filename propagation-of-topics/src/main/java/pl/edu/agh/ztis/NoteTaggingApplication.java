package pl.edu.agh.ztis;

import java.util.List;

import maui.stopwords.StopwordsEnglish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.components.TagNotes;
import pl.edu.agh.ztis.model.Note;
import pl.edu.agh.ztis.repositories.NoteRepository;


@Component
public class NoteTaggingApplication {

	private static final int MAUI_TAG_LIMIT_PER_TEXT = 3;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private TagNotes notesTagger;
	
	

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");

		NoteTaggingApplication app = context.getBean(NoteTaggingApplication.class);
		app.test();
		
	}


	private void test() throws Exception {
		List<Note> notes = noteRepository.findByText1Containing("Iraq");
		notesTagger.tagNotes(notes, MAUI_TAG_LIMIT_PER_TEXT);
	}
}
