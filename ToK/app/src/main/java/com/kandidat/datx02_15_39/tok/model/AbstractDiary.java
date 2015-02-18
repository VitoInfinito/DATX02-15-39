package com.kandidat.datx02_15_39.tok.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

/**
 * Created by Lagerstedt on 2015-02-16.
 */
public abstract class AbstractDiary implements IDiary {
	/*
		 * This will have the date as StringDateFormat yyyyMMdd and be a key to
		 * one days activities
		 */
	private Hashtable<String,IDiaryActivity> activitys;


}
