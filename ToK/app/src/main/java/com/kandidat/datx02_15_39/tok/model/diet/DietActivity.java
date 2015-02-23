package com.kandidat.datx02_15_39.tok.model.diet;

import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.Date;
import java.util.List;

/**
 * Created by Lagerstedt on 2015-02-16.
 */
public class DietActivity implements IDiaryActivity {

	private final String id;
	private List<Food> foodList;
	private float calorieCount, proteinCount, fatCount, carbCount;

	public DietActivity(){
		this.id ="";
	}

	public void addFood(EditActivityParams eap){
	}

	public List<Food> getFoodList(){
		return null;
	}

	private void update(){

	}

    @Override
    public String getID() {
		return "";
    }

	@Override
	public void setDate(Date d) {

	}

	@Override
	public Date getDate(Date d) {
		return null;
	}

	@Override
	public void edit(EditActivityParams eap) {

	}

	@Override
	public void add(AddToActivity addToActivity) {

	}
}
