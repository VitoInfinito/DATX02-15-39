package com.kandidat.datx02_15_39.tok.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lagerstedt on 2015-02-28.
 */
public abstract class AbstractDiaryActivity implements IDiaryActivity{

	private final String id;
	private static int idCount = 0;
	private Calendar date;

	public AbstractDiaryActivity(){ id = generateID();}

	private String generateID() {
		return String.format("st%06d", idCount++);
	}
	
	@Override
	public String getID(){
		return id;
	}
	@Override
	public void setDate(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		this.date = c;
	}

	@Override
	public Date getDate() {
		return date.getTime();
	}
}
