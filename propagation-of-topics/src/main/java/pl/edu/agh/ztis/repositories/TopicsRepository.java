package pl.edu.agh.ztis.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import pl.edu.agh.ztis.model.Topic;

//@Repository 
public interface TopicsRepository extends MongoRepository<Topic, String> {

    public Topic findById(long id);

    public List<Topic> findByAlgorithm(String algorithm);

}
