package com.kandidat.datx02_15_39.tok.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Lagerstedt on 2015-02-18.
 */
public abstract class EditActivityParams {
	public final Calendar date;

	public EditActivityParams(Calendar date){
		this.date = date;
	}

}
