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
public class ReaderFromFiles {

    @Autowired
    private ReadNotesService readNotesBean;
    @Autowired
    private NoteRepository noteRepository;

    public long lookupFiles(String name) throws FileNotFoundException {
        File dir = new File(name);
        File[] files = dir.listFiles();

        long count = 0;
        for (File file : files) {
            if (file.isDirectory()) {
                List<Note> notes = this.readNotesBean.getNotesFromFile(new File(file, "rss_unique_TAG_country_Ebola.csv"));
                noteRepository.save(notes);
                count += notes.size();
            }
        }
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length < 1) {
            System.out.println("Usage: <path_to Geomedia_extract_AGENDA>");
        }
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        ReaderFromFiles app = context.getBean(ReaderFromFiles.class);
        long start = System.currentTimeMillis();
        long load = app.lookupFiles(args[0]);
        long end = System.currentTimeMillis();
        System.out.println("Loaded " + load + " notes from files in " + (end - start));

        // do sth with app
    }
}
