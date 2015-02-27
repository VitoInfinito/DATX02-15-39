package com.kandidat.datx02_15_39.tok.model;

import java.util.Date;

/**
 * Created by Lagerstedt on 2015-02-16.
 */
public interface IDiaryActivity {

    public String getID();

	public void setDate(Date d);

	public Date getDate();

	public void edit(EditActivityParams eap);

	public void add(AddToActivity addToActivity);
}