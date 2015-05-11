package com.kandidat.datx02_15_39.tok.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lagerstedt on 2015-02-16.
 */
public interface IDiaryActivity {

    public String getID();

	public void setDate(Calendar date);

	public Calendar getDate();

	public void edit(EditActivityParams eap);

	public void add(AddToActivity addToActivity);
}