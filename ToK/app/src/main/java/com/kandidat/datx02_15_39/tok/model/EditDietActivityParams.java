package com.kandidat.datx02_15_39.tok.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Lagerstedt on 2015-02-18.
 */
public class EditDietActivityParams extends EditActivityParams{
	public final List<Food> list;

	public EditDietActivityParams(Date d,List<Food> list) {
		super(d);
		this.list = list;
	}
}
