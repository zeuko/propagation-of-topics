package pl.edu.agh.ztis.tagging;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import pl.edu.agh.ztis.model.Language;
import pl.edu.agh.ztis.model.Note;

@Component
public class TaggingService {

    private ChunkerME posTagger;
    private Language language;

    public Collection<String> tags(Note node) {
        assert node.getLanguage() == language;
        return Arrays.asList(posTagger.(node.getText1()));
    }

    public void loadModel(String languageModelFile, Language language) throws IOException {
        InputStream modelIn = getModelAsInputStream(languageModelFile);
        ChunkerModel model = new ChunkerModel(modelIn);
        posTagger = new ChunkerME(model);
        this.language = language;
    }

    private InputStream getModelAsInputStream(String languageModelFile) throws IOException {
        InputStream modelIn = null;
        try {
            modelIn = new FileInputStream(languageModelFile);
        } catch (FileNotFoundException fne) {
            modelIn = new FileInputStream(new ClassPathResource(languageModelFile).getFile());
        }
        return modelIn;
    }

}
