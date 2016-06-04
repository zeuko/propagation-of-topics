package pl.edu.agh.ztis.components;

import java.util.Date;
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
