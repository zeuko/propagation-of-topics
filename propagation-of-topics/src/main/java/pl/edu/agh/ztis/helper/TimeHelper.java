package pl.edu.agh.ztis.helper;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeHelper {

	public static Date parseStringToJavaDate(String time) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime dt = DateTime.parse(time, formatter);
		return dt.toDate();
	}

}
