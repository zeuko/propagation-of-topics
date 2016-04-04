package pl.edu.agh.ztis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
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
    @Option(name = "-c", aliases = "--clean", usage = "clean database before loading files")
    boolean clean = false;
    @Argument(usage = "path to Geomedia_extract_AGENDA", required = true)
    String geomediaPath;

    public long lookupFiles() throws FileNotFoundException {
        File dir = new File(geomediaPath);
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

    public static void main(String[] args) throws FileNotFoundException, CmdLineException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        ReaderFromFiles app = context.getBean(ReaderFromFiles.class);
        CmdLineParser cmdLineParser = new CmdLineParser(app);
        cmdLineParser.parseArgument(args);

        if (app.clean){
            app.cleanDb();
        }
        long start = System.currentTimeMillis();
        long load = app.lookupFiles();
        long end = System.currentTimeMillis();
        System.out.println("Loaded " + load + " notes from files in " + (end - start) + " ms");

        // do sth with app
    }

    private void cleanDb() {
        System.out.println("Cleaning database...");
        noteRepository.deleteAll();
        System.out.println("Database cleaned.");
    }
}
