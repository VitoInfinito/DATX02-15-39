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
}
