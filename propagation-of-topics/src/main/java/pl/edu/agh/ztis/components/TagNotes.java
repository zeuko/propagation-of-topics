package pl.edu.agh.ztis.components;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import maui.stopwords.StopwordsEnglish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.model.Note;
import pl.edu.agh.ztis.repositories.NoteRepository;
import pl.edu.agh.ztis.topics.MauiTopicExtractor;
import pl.edu.agh.ztis.topics.custom.CustomTopicTags;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Component
public class TagNotes {

	@Autowired
	private NoteRepository noteRepository;

	private MauiTopicExtractor mauiTopics;

	private CustomTopicTags customTopicTags;

	private StopwordsEnglish stopwords;

	@PostConstruct
	private void initComponent() {

		String vocabularyName = "agrovoc_en";
		String modelName = "fao30";
		String dataDirectory = "./Maui1.2/";
		
		String englishStopwords = dataDirectory + "data/stopwords/stopwords_en.txt";
		stopwords = new StopwordsEnglish(englishStopwords);
		mauiTopics = new MauiTopicExtractor(dataDirectory, vocabularyName, modelName);
		
		customTopicTags = new CustomTopicTags();
	}

	/**
	 * Tags all notes from list passed as param. EXECUTES DATABASE OPERATIONS.
	 * 
	 */
	public void tagNotes(List<Note> notes, int tagsPerNotePartForMaui) {

		for (Note note : notes) {
			try {
				if (note.getTags() != null && !note.getTags().isEmpty()) {
					continue;
				}
				Set<String> keywords = getMauiTagsForNote(note, tagsPerNotePartForMaui);
				keywords.addAll(getCustomTagsForNote(note));
				keywords.addAll(getCapitalLetterWordsFromNote(note));
				System.out.println(keywords);
				
				List<String> tags = note.getPythonTags();
				if (tags != null && !tags.isEmpty()) {
					keywords.addAll(tags);
				}
				note.setTags(Lists.newArrayList(keywords));
				noteRepository.save(note);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Maximum size of returned set is 2*tagsPerNotePart, because method
	 * extracts topics from both note title (Note.text1) and note content
	 * (Note.text2).
	 */
	public Set<String> getMauiTagsForNote(Note note, int tagsPerNotePart) throws Exception {
		String text1 = note.getText1();
		String text2 = note.getText2();
		List<String> topics1 = mauiTopics.extractTopicsFromText(text1, tagsPerNotePart);
		List<String> topics2 = mauiTopics.extractTopicsFromText(text2, tagsPerNotePart);
		Set<String> keywords = Sets.newHashSet(topics1);
		keywords.addAll(topics2);
		return keywords;
	}
	
	
	public Set<String> getCustomTagsForNote(Note note) {
		String text1 = note.getText1().toLowerCase();
		String text2 = note.getText2().toLowerCase();
		
		List<String> topics = customTopicTags.getCustomTopicTags();
		
		Set<String> customTags = Sets.newHashSet();
		for (String topicTag : topics) {
			List<String> keywords = customTopicTags.getKeywordsForTopic(topicTag);
			for (String keyword : keywords) {
				if (text1.contains(keyword) || text2.contains(keyword)) {
					customTags.add(topicTag);
				}
			}
		}
		
		return customTags;
		
	}

	
	public Set<String> getCapitalLetterWordsFromNote(Note note) {
		String noteText = note.getText1() + " " + note.getText2();
		String[] words = noteText.split("[ \\.,]");
		
		return Arrays.stream(words)
				.map(word -> word.trim())
				.filter(word -> isLongEnough(word))
				.filter(word -> startsWithCapitalLetter(word))
				.filter(word -> isNotStopword(word))
				.map(word -> fixWord(word))
				.collect(Collectors.toSet());
		
	}

	private String fixWord(String word) {
		return word.replaceAll("'[Ss]", "");
	}

	private boolean isLongEnough(String word) {
		return word.length() > 1;
	}

	private boolean isNotStopword(String word) {
		return !stopwords.isStopword(word);
	}

	private boolean startsWithCapitalLetter(String word) {
		return word.charAt(0) >= 'A' && word.charAt(0) <= 'Z';
	}
}
