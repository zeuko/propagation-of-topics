package pl.edu.agh;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.ztis.components.ParseNotesService;
import pl.edu.agh.ztis.helper.FileHelper;
import pl.edu.agh.ztis.helper.TimeHelper;
import pl.edu.agh.ztis.model.Note;

public class ReadNotesTest {

	
	ParseNotesService sut;
	
	@Before
	public void init() throws Exception {
		sut = new ParseNotesService();
		System.out.println();
	}
	
	@Test(expected = FileNotFoundException.class)
	public void testThrowFileNotFoundException() throws Exception {
		sut.getNotesFromFile("notexistentfile.txt");
		Assertions.fail("Expected exception was not thrown.");
	}
	
	@Test
	public void testReadExistingFile() throws Exception {
		String filepath = "test_file_unique_tagged.csv";
		InputStream stream = FileHelper.loadResource(filepath);
		List<Note> list = sut.getNotesFromStream(stream);
	}

	@Test
	public void testReadTestFile() throws Exception {
		String filepath = "test_file_unique_tagged.csv";
		InputStream stream = FileHelper.loadResource(filepath);
		List<Note> list = sut.getNotesFromStream(stream);
		Assertions.assertThat(list).hasSize(9);
	}
	
	@Test
	public void testReadTestFileCheckValue() throws Exception {

		// given
		Note templateNote = new Note();
		templateNote.setId(3117024);
		templateNote.setFeedIdentifier("en_USA_nytime_int");
		templateNote.setTime(getDate("2014-10-01 00:34:10"));
		templateNote.setText1("Airline Passenger With Ebola Is Under Treatment in Dallas");
		templateNote.setText2("The man, who took a commercial flight from Liberia, is said to be the first traveler to have brought the virus to the United States on a passenger plane.");
		templateNote.setTag("LBR");
		templateNote.setNbTabDetected(1);
		
		// when
		String filepath = "test_file_unique_tagged_single_row.csv";
		InputStream stream = FileHelper.loadResource(filepath);
		List<Note> list = sut.getNotesFromStream(stream);
		
		
		// then
		Assertions.assertThat(list).hasSize(1);
		Assertions.assertThat(list.get(0)).isEqualToComparingFieldByField(templateNote);
		
	}

	private Date getDate(String timeAsString) {
		return TimeHelper.parseStringToJavaDate(timeAsString);
	}
	
	@After
	public void cleanUp() {
		sut = null;
	}
}
