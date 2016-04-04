package me.mathiasluo.model.schedule;

import java.util.*;
import java.text.*;

/**
 *
 *Represent a class a student is taking
 */
public class Class {
	private String subj;
	private String name;
	private String crn;
	private Calendar startTime;
	private Calendar endTime;
	private String location;
	private final static SimpleDateFormat timeFormat = new SimpleDateFormat("EEE hh:mm aa");
	/**
	 * Make a class from a week at a glance description
	 * @param day Day class takes place
	 * @param rawClass Raw class string from week at a glance
	 */
	public Class(int day, String rawClass) {
		String[] fields = rawClass.split(" ");
		subj = fields[0];
		name = fields[1];
		crn = fields[2];
		location = fields[7] + " " + fields[8];
		//Manually break up the fields with a scanner. No sscanf to help here
		Scanner readTime = new Scanner(fields[4]);
		readTime.useDelimiter(":");
		int startHour = readTime.nextInt() % 12;
		int startMinute = readTime.nextInt();
		readTime = new Scanner(fields[5]);
		readTime.useDelimiter("-");
		String startTimeOfDay = readTime.next();
		readTime.useDelimiter(":");
		int endHour = -readTime.nextInt() % 12;
		int endMinute = readTime.nextInt();
		String endTimeOfDay = fields[6];
		startTime = setupTime(day, startHour, startMinute, startTimeOfDay);
		endTime = setupTime(day, endHour, endMinute, endTimeOfDay);
	}

	private Calendar setupTime(int day, int hour, int minute, String timeOfDay) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.AM_PM, timeOfDay.equals("am") ? Calendar.AM : Calendar.PM);
		cal.set(Calendar.DAY_OF_WEEK, day);	
		return cal;
	}

	public String getSubject() {
		return subj;
	}

	public String getName() {
		return name;
	}

	public String getCRN() {
		return crn;
	}

	public String getLocation() {
		return location;
	}

	public Calendar getStartTime() {
		return (Calendar) startTime.clone();
	}

	public Calendar getEndTime() {
		return (Calendar) endTime.clone();
	}

	@Override
	public String toString() {
		return "Subj: " + subj + "\nName: " + name + "\nCrn: " + crn + "\nLocation: " + location + "\nStart Time: " + timeFormat.format(startTime.getTime()) +
			"\nEnd Time: " + timeFormat.format(endTime.getTime());
	}
}
