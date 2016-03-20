package pl.edu.agh.ztis;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.model.Note;


@Component
public class Application {

	@Autowired
	private ReadNotesService readNotesBean;
	
	public void readNotes() {
		String filepath = "D:/uczelnia/ztis/geomedia/geomedia/cist-sample_geomedia-db/Sample_GeomediaDB/es_MEX_univer_int/rss_unique_tagged.csv";
		try {
			List<Note> notesFromFile = readNotesBean.getNotesFromFile(filepath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		Application app = context.getBean(Application.class);
		
		// do sth with app
	}
}
