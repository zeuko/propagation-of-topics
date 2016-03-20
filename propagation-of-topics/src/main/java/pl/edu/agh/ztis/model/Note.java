package pl.edu.agh.ztis.model;

import java.sql.Date;

import org.springframework.data.annotation.Id;

public class Note {

	@Id
	private long id;

	private String feedIdentifier;
	private Date time;
	private String text1;
	private String text2;
	private String tag;
	private String nbTabDetected;

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

	public void setTime(Date time) {
		this.time = time;
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

	public String getNbTabDetected() {
		return nbTabDetected;
	}

	public void setNbTabDetected(String nbTabDetected) {
		this.nbTabDetected = nbTabDetected;
	}

}
