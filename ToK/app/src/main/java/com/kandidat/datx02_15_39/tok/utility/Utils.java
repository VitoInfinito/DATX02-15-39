package com.kandidat.datx02_15_39.tok.utility;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by tomashasselquist on 25/03/15.
 */
public final class Utils {

    public static final String ACCOUNT_PREFS = "account_prefs";


    private Utils() {}


    public static final int getTwoPlusX(int x) {
        return 3;
    }

    public static Calendar DateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static Calendar MillisToCalendar(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal;
    }

	/*
 * Sets upp a calender that can be compared only between 2 dates!
 * The Hour, Minute, Seconds, and milliseconds is set to 0.
 * @return - A Calendar that is Comparable between days
 */
	private static Calendar setupCalendar(){
		Calendar tmp = Calendar.getInstance();
		tmp.set(Calendar.HOUR_OF_DAY, 0);
		tmp.set(Calendar.MINUTE, 0);
		tmp.set(Calendar.SECOND,0);
		tmp.set(Calendar.MILLISECOND,0);
		return tmp;
	}

	public static final String recipeArgument = "RECIPEARGUMENT";
	public static final String dietActivityArgument = "DIETACTIVITYARGUMENT";
}
