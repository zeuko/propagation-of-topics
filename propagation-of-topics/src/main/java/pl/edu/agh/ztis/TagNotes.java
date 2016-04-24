package pl.edu.agh.ztis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import maui.main.MauiTopicExtractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.helper.FileHelper;
import pl.edu.agh.ztis.model.Note;
import pl.edu.agh.ztis.repositories.NoteRepository;

import com.google.common.collect.Sets;

@Component
public class TagNotes {

	// org.kohsuke.args4j.Option
	@Autowired
	private ReadNotesService readNotesBean;
	@Autowired
	private NoteRepository noteRepository;

	public void lookupFiles(String name) throws FileNotFoundException {
		File dir = new File(name);
		File[] files = dir.listFiles();

		for (File file : files) {
			if (file.isDirectory()) {
				List<Note> notes = this.readNotesBean.getNotesFromFile(new File(file,
						"rss_unique_TAG_country_Ebola.csv"));
				noteRepository.save(notes);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// ApplicationContext context = new
		// ClassPathXmlApplicationContext("spring-config.xml");
		// TagNotes app = context.getBean(TagNotes.class);
		// app.lookupFiles("D:\\uczelnia\\ztis\\geomedia\\geomedia\\cist-sample_geomedia-db\\Sample_GeomediaDB");
		// app.loadFormDatabas();
		
		
		
		// do sth with app
	}

	private static File inputStreamToFile(InputStream inputStream) throws FileNotFoundException, IOException {
		File file = new File("test2.txt");
		FileOutputStream outputStream = new FileOutputStream(file);

		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = inputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}
		return file;
	}

	private void loadFormDatabas() {
		long start = System.currentTimeMillis();
		List<Note> all = noteRepository.findAll();
		long end = System.currentTimeMillis();
		System.out.println("Loaded " + all.size() + " notes in " + (end - start) + " ms.");
	}
}
