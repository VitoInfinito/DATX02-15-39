package com.kandidat.datx02_15_39.tok.model.diet;

import com.kandidat.datx02_15_39.tok.model.AddToActivity;

/**
 * Class to be able to add items to Diet activity, This Class is a sub class for AddtoActivity to
 * be able to make a standard for adding in all diaries.
 */
public class AddToDietActivity extends AddToActivity {
	public final Food food;

	public AddToDietActivity(Food food){
		this.food = food;
	}
}
