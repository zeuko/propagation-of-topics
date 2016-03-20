package pl.edu.agh.ztis.model;

import java.util.Date;

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
	private int nbTabDetected;

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

}
