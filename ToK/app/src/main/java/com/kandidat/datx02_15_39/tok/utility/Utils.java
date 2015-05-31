package com.kandidat.datx02_15_39.tok.utility;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

/**
 * Class used for all static utility functions used in the application
 */
public final class Utils {

    //account prefs is the referenced value of the saved account preferences
    public static final String ACCOUNT_PREFS = "account_prefs";
	public static final int DATABASE_VERSION = 1;
    public static final String recipeArgument = "RECIPEARGUMENT";
    public static final String dietActivityArgument = "DIETACTIVITYARGUMENT";

    private Utils() {}

    /**
     * Method used to convert a date to a calendar
     * @param date is the date that is to be converted
     * @return a calendar containing the information the date contained
     */
    public static Calendar DateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * Method used to create a calendar from milliseconds
     * @param millis the milliseconds since January 1st 1970
     * @return the calendar represented by the milliseconds
     */
    public static Calendar MillisToCalendar(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal;
    }

	/**
     * Sets upp a calender that can be compared only between 2 dates!
     * The Hour, Minute, Seconds, and milliseconds is set to 0.
     * @return - A Calendar that is Comparable between days
     */
	public static Calendar setupCalendar(){
		Calendar tmp = Calendar.getInstance();
		tmp.set(Calendar.HOUR_OF_DAY, 0);
		tmp.set(Calendar.MINUTE, 0);
		tmp.set(Calendar.SECOND,0);
		tmp.set(Calendar.MILLISECOND,0);
		return tmp;
	}

    /**
     * Method used for converting DP to pixels
     * @param context is the context of the activity
     * @param dp is the dp to be converted
     * @return an int containing the pixels the dp was converted to
     */
	public static int getDpToPixel(Context context, int dp){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
}
