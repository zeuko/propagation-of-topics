package pl.edu.agh.ztis.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class Topic {

    @Id
    private long id;

    private String name;
    private List<String> tags;
    private int size;
    private List<Note> notes;
    private int notes_size;
    private String algorithm;
    private int minCommonTopics;

    public Topic(String name, List<String> tags, int size, List<Note> notes, int notes_size, String algorithm, int minCommonTopics) {
        this.name = name;
        this.tags = tags;
        this.size = size;
        this.notes = notes;
        this.notes_size = notes_size;
        this.algorithm = algorithm;
        this.minCommonTopics = minCommonTopics;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public int getNotes_size() {
        return notes_size;
    }

    public void setNotes_size(int notes_size) {
        this.notes_size = notes_size;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public int getMinCommonTopics() {
        return minCommonTopics;
    }

    public void setMinCommonTopics(int minCommonTopics) {
        this.minCommonTopics = minCommonTopics;
    }
}
