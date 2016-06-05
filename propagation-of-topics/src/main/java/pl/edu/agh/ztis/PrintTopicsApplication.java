package pl.edu.agh.ztis;

import java.io.File;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.model.Language;
import pl.edu.agh.ztis.model.Note;
import pl.edu.agh.ztis.model.Topic;
import pl.edu.agh.ztis.repositories.NoteRepository;
import pl.edu.agh.ztis.repositories.TopicsRepository;

@Component
public class PrintTopicsApplication {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TopicsRepository topicsRepository;

    @Option(name = "-l", aliases = "--language", required = true)
    Language language;
    @Option(name = "-a", aliases = "--algorithm", required = true)
    String algorithm;
    @Option(name = "-f", aliases = "--file", required = false)
    String outFile;

    public static void main(String[] args) throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");

        PrintTopicsApplication app = context.getBean(PrintTopicsApplication.class);
        CmdLineParser cmdLineParser = new CmdLineParser(app);
        cmdLineParser.parseArgument(args);

        app.test();

    }

    private void test() throws Exception {
        List<Topic> allTopics = topicsRepository.findAll();
        List<Note> notes = noteRepository.findAllByLanguage(language);
        Map<Long, Note> notesMap = notes.stream().collect(Collectors.toMap(Note::getId, Function.identity()));

        PrintStream out;
        if (outFile != null) {
            out = new PrintStream(new File(outFile));
        } else {
            out = System.out;
        }
        allTopics.stream()
                .filter(topic -> topic.getTags().size() > 1)
                .filter(topic -> topic.getNotesSize() > 1)
                .sorted(Comparator.comparingLong(Topic::getNotesSize))
                .forEach(topic -> {
                    out.println(topic.getTags());

                    out.println("\t" + topic.getNotes()
                            .stream()
                            .map(notesMap::get)
                            .map(Note::getTime)
                            .map(Object::toString)
                            .collect(Collectors.joining("\n\t"))
                    );
                });
    }
}
