package pl.edu.agh.ztis.tagging;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.model.Language;
import pl.edu.agh.ztis.model.Note;
import pl.edu.agh.ztis.repositories.NoteRepository;

@Component
public class TagNotes {

    @org.kohsuke.args4j.Option(name = "-m", aliases = "--model", required = false, metaVar = "MODEL", usage = "language-model file")
    private String languageModelFile = "en-pos-maxent.bin";

    @Option(name = "-v", aliases = "--verbous", required = false, metaVar = "VERBOUS")
    private boolean verbous = false;
    @Option(name = "-l", aliases = "--language-code", metaVar = "LANGUAGE")
    private Language language = Language.EN;

    @Autowired
    private TaggingService taggingService;
    @Autowired
    private NoteRepository noteRepository;

    public static void main(String[] args) throws IOException, CmdLineException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        TagNotes app = context.getBean(TagNotes.class);
        CmdLineParser cmdLineParser = new CmdLineParser(app);
        cmdLineParser.parseArgument(args);
        app.loadFormDatabas();

        // do sth with app
    }

    private void loadFormDatabas() throws IOException {
        long start = System.currentTimeMillis();
        List<Note> all = Arrays.asList(noteRepository.findOne(70936l));
        taggingService.loadModel(languageModelFile, language);
        for (Note note : all) {
            Collection<String> tags = taggingService.tags(note);
            if (verbous)
                System.out.println(note.getText1() + "\n\t" + tags.stream().collect(Collectors.joining("\n\t")) + "\n\n");
        }
        long end = System.currentTimeMillis();
    }
}