package pl.edu.agh.ztis.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import pl.edu.agh.ztis.model.Note;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

}
