package com.kandidat.datx02_15_39.tok.model.diet;

import com.kandidat.datx02_15_39.tok.model.AddToActivity;

/**
 * Created by Lagerstedt on 2015-02-18.
 */
public class AddToDietActivity extends AddToActivity {
	public final Food food;

	public AddToDietActivity(Food food){
		this.food = food;
	}
}
