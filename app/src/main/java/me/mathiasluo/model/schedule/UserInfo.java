package me.mathiasluo.model.schedule;

import java.util.*;
import java.text.*;

/**
 *Represent user info
 */
public class UserInfo {
	private String id;
	private String firstName;
	private String middleInitial;
	private String lastName;
	private Calendar dumpDate;
	private final static SimpleDateFormat dumpDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm aa");

	public UserInfo(String rawUserInfo) {
		String[] fields = rawUserInfo.split(" ");
		//TODO:What if user doesn't have a middle name? screws up field parsing
		id = fields[0];
		firstName = fields[1];
		middleInitial = fields[2];
		lastName = fields[3];
		dumpDate = Calendar.getInstance();
		try {
			dumpDate.setTime(dumpDateFormat.parse(fields[4] + " " + fields[5] + " " + fields[6] + " " + fields[7] + " " + fields[8]));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "ID: " + id + "\nFirst Name: " + firstName + "\nMiddle Initial: " + middleInitial + "\nLast Name: " + lastName + "\nDump Date: " + dumpDateFormat.format(dumpDate.getTime()); 
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return firstName + " " + middleInitial + " " + lastName;
	}

	public Calendar getDumpDate() {
		return (Calendar) dumpDate.clone();
	}
}
