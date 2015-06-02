package com.kandidat.datx02_15_39.tok.model.diet;

import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.diet.Food;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Class Added to be able to standardize the edit function to a diary item, This class will help to
 * edit a DietActivity.
 */
public class EditDietActivityParams extends EditActivityParams {
	public final List<Food> list;
	public final String name;
	public final DietActivity.MEALTYPE mealtype;

	public EditDietActivityParams(Calendar newDate, String name, List<Food> list, DietActivity.MEALTYPE mealtype) {
		super(newDate);
		this.list = list;
		this.name = name;
		this.mealtype = mealtype;
	}
}
