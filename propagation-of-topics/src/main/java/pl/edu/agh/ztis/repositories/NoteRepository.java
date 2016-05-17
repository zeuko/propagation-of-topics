package pl.edu.agh.ztis.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.edu.agh.ztis.model.Language;
import pl.edu.agh.ztis.model.Note;

//@Repository 
public interface NoteRepository extends MongoRepository<Note, String> {

	public Note findById(long id);
	
	public List<Note> findByText1Containing(String partOfText1);

	List<Note> findAllByLanguage(Language language);
}
