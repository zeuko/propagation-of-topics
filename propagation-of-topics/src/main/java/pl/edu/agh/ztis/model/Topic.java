package pl.edu.agh.ztis.model;

import java.util.List;

import org.springframework.data.annotation.Id;

public class Topic {

    @Id
    private long id;

    private String name;
    private List<String> tags;
    private int size;
    private List<Long> notes;
    private int notesSize;
    private String algorithm;
    private int minCommonTopics;

    public Topic(String name, List<String> tags, int size, List<Long> notes, int notesSize, String algorithm, int minCommonTopics) {
        this.name = name;
        this.tags = tags;
        this.size = size;
        this.notes = notes;
        this.notesSize = notesSize;
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

    public List<Long> getNotes() {
        return notes;
    }

    public void setNotes(List<Long> notes) {
        this.notes = notes;
    }

    public int getNotesSize() {
        return notesSize;
    }

    public void setNotesSize(int notesSize) {
        this.notesSize = notesSize;
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
