package pl.edu.agh.ztis;

import java.util.List;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.components.TopicExtractor;
import pl.edu.agh.ztis.model.Language;
import pl.edu.agh.ztis.model.Note;
import pl.edu.agh.ztis.model.Topic;
import pl.edu.agh.ztis.repositories.NoteRepository;
import pl.edu.agh.ztis.repositories.TopicsRepository;

@Component
public class TopicSearchApplication {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private TopicExtractor topicExtractor;
    @Autowired
    private TopicsRepository topicsRepository;

    @Option(name = "-l", aliases = "--language", required = true)
    Language language;
    @Option(name = "-w", aliases = "--weight", required = true)
    int weight;

    public static void main(String[] args) throws Exception {
        @SuppressWarnings("resource")
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");

        TopicSearchApplication app = context.getBean(TopicSearchApplication.class);
        CmdLineParser cmdLineParser = new CmdLineParser(app);
        cmdLineParser.parseArgument(args);

        app.test();

    }

    private void test() throws Exception {
        List<Note> notes = noteRepository.findAllByLanguage(Language.EN);
        List<Topic> topics = topicExtractor.extractTopics(notes, weight);
        topicsRepository.save(topics);
    }
}
