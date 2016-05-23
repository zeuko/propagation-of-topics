package pl.edu.agh.ztis.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;

// TODO: add builder?
public class Note {

    @Id
    private long id;

    private String feedIdentifier;
    private Date time;
    private String text1;
    private String text2;
    private String tag;
    private List<String> tags = new ArrayList<>();
    private List<String> pythonTags = new ArrayList<>();
    private String country;

    private int nbTabDetected;
    private Language language;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFeedIdentifier() {
        return feedIdentifier;
    }

    public void setFeedIdentifier(String feedIdentifier) {
        this.feedIdentifier = feedIdentifier;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date date) {
        this.time = date;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getNbTabDetected() {
        return nbTabDetected;
    }

    public void setNbTabDetected(int nbTabDetected) {
        this.nbTabDetected = nbTabDetected;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }

    public List<String> getPythonTags() {
        return pythonTags;
    }

    public void setPythonTags(List<String> pythonTags) {
        this.pythonTags = pythonTags;
    }

    public Set<String> getAllTags() {
        Set<String> allTags = new HashSet<>(getPythonTags());
        allTags.addAll(getTags());
        return allTags;
    }
}
