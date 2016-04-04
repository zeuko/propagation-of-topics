package pl.edu.agh.ztis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.model.Note;
import pl.edu.agh.ztis.repositories.NoteRepository;

@Component
public class TagNotes {

//    org.kohsuke.args4j.Option
    @Autowired
    private ReadNotesService readNotesBean;
    @Autowired
    private NoteRepository noteRepository;

    public void lookupFiles(String name) throws FileNotFoundException {
        File dir = new File(name);
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                List<Note> notes = this.readNotesBean.getNotesFromFile(new File(file, "rss_unique_TAG_country_Ebola.csv"));
                noteRepository.save(notes);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        TagNotes app = context.getBean(TagNotes.class);
        app.loadFormDatabas();

        // do sth with app
    }

    private void loadFormDatabas() {
        long start = System.currentTimeMillis();
        List<Note> all = noteRepository.findAll();
        long end = System.currentTimeMillis();
        System.out.println("Loaded " + all.size() + " notes in " + (end - start) + " ms.");
    }
}
