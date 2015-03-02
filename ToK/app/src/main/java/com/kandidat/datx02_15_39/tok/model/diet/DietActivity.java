package com.kandidat.datx02_15_39.tok.model.diet;

import com.kandidat.datx02_15_39.tok.model.AbstractDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Lagerstedt on 2015-02-16.
 */
public class DietActivity extends AbstractDiaryActivity {

	private List<Food> foodList;
	private double calorieCount, proteinCount, fatCount, carbCount;

	public DietActivity(Calendar calendar){
		this(new ArrayList<Food>(), calendar);
	}

	public DietActivity(List<Food> listOfFood, Calendar calendar){
		super();
		this.foodList = listOfFood;
		setDate(calendar.getTime());
		update();
	}

	private void addFood(Food food){
		foodList.add(food);
	}

	public List<Food> getFoodList(){
		ArrayList<Food> tmp = new ArrayList<Food>();
		for (Food f: foodList){
			tmp.add(f);
		}
		return tmp;
	}

	private void update(){
		calorieCount = 0;
		proteinCount = 0;
		fatCount = 0;
		carbCount = 0;
		for (Food f: foodList){
			calorieCount += f.getCalorieAmount();
			proteinCount += f.getProteinAmount();
			fatCount += f.getFatAmount();
			carbCount += f.getCarbAmount();
		}
	}

	@Override
	public void edit(EditActivityParams eap) {
		if(eap instanceof EditDietActivityParams) {
			EditDietActivityParams edap = (EditDietActivityParams) eap;
			if(!edap.list.isEmpty()){
				this.foodList = edap.list;
			}
			if(edap.date != null){
				setDate(edap.date);
			}
		}
		update();
	}

	@Override
	public void add(AddToActivity addToActivity) {
		if(addToActivity instanceof AddToDietActivity) {
			AddToDietActivity atda = (AddToDietActivity)addToActivity;
			if(atda.food != null)
				addFood(((AddToDietActivity) addToActivity).food);
		}
		update();
	}
}
