package pl.edu.agh.ztis.components;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.stereotype.Component;

import pl.edu.agh.ztis.model.Note;

@Component
public class AnalysisTool {

	public enum TimeAnalysisWindow {
		MONTH, WEEK
	}
	
	public Map<DateTime, Integer> getNotesTimeAnalysis(List<Note> notes, TimeAnalysisWindow window) {
		TreeMap<Date, Integer> notesByDate = getNotesByDate(notes);
		
		// clustering notes week by week, month by month etc
		DateTime intervalStartDate = null;
		Map<DateTime, Integer> resultMap = new TreeMap<DateTime, Integer>();
		
		for (Entry<Date, Integer> entry : notesByDate.entrySet()) {
			DateTime dateTimeJoda = new DateTime(entry.getKey());
			int count = entry.getValue();
			
			if (intervalStartDate == null) {
				intervalStartDate = dateTimeJoda;
			} else if (exceedsTimeWindow(intervalStartDate, dateTimeJoda, window)) {
				if (window == TimeAnalysisWindow.WEEK) {
					intervalStartDate = intervalStartDate.plusDays(7);
				} else if (window == TimeAnalysisWindow.MONTH) {
					intervalStartDate = intervalStartDate.plusMonths(1);
				}
			}
			
			Integer resultCount = resultMap.get(intervalStartDate);
			if (resultCount == null) {
				resultMap.put(intervalStartDate, 1);
			} else {
				resultMap.put(intervalStartDate, resultCount.intValue() + count);
			}
		}
		return resultMap;
		
	}

	public class CountryCount {
		public String country;
		public Integer count;
		
		public CountryCount(String country, int count) {
			this.count = count;
			this.country = country;
		}
		
		public void  add() {
			this.count = this.count +1;
		}
	}
	
	public Map<String, Integer> getNotesByCountry(List<Note> notes) {
		HashMap<String,Integer> hashMap = new HashMap<String, Integer>();
		
		for (Note note : notes) {
			String country = note.getFeedIdentifier().split("_")[1];
			Integer count = hashMap.get(country);
			if (count == null) {
				hashMap.put(country, 1);
			} else {
				hashMap.put(country, count.intValue() + 1);
			}
		}
		
		return hashMap;
		
	}
	
	// returning TreeMap on purpose
	private TreeMap<Date, List<CountryCount>> getNotesByDateAndCountry(List<Note> notes) {
		TreeMap<Date, List<CountryCount>> notesByDate =  new TreeMap<Date, List<CountryCount>>(); 
		for (Note note : notes) {
			Date time = note.getTime();
			List<CountryCount> integer = notesByDate.get(time);
			if (integer == null || integer.isEmpty()) {
				LinkedList<CountryCount> linkedList = new LinkedList<>();
				CountryCount cc = new CountryCount(note.getCountry(), 1);
				linkedList.add(cc);				
				notesByDate.put(time, linkedList);
			} else {
				boolean added = false;
				for (CountryCount cc : integer) {
					if (cc.country.equals(note.getCountry())) {
						cc.add();
						added = true;
						break;
					}
 				}
				if (!added) {
					integer.add(new CountryCount(note.getCountry(), 1));
				}
			}
		}
		return notesByDate;
	}
	
	// returning TreeMap on purpose
	private TreeMap<Date, Integer> getNotesByDate(List<Note> notes) {
		TreeMap<Date, Integer> notesByDate =  new TreeMap<Date, Integer>(); 
		for (Note note : notes) {
			Date time = note.getTime();
			Integer integer = notesByDate.get(time);
			if (integer == null) {
				notesByDate.put(time, 1);
			} else {
				notesByDate.put(time, integer.intValue() + 1);
			}
		}
		return notesByDate;
	}

	public boolean exceedsTimeWindow(DateTime initDate, DateTime dateTimeJoda, TimeAnalysisWindow window) {
		DateTime endTime = null;
		if (window == TimeAnalysisWindow.MONTH) {
			endTime = initDate.plusMonths(1);
		} else if (window == TimeAnalysisWindow.WEEK){
			endTime = initDate.plusDays(7);
		}
		Interval interval = new Interval(initDate, endTime);
		if (interval.contains(dateTimeJoda)) {
			return false;
		}
		return true;
	}
	
}
