package com.kandidat.datx02_15_39.tok.model.diet;

import android.content.Context;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.AbstractDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Lagerstedt on 2015-02-16.
 */
public class DietActivity extends AbstractDiaryActivity implements Serializable {

	private List<Food> foodList;
	private double calorieCount, proteinCount, fatCount, carbCount;
    private String name;
	private MEALTYPE mealtype = MEALTYPE.SNACK;

	public enum MEALTYPE implements Serializable{
		BREAKFAST,
		LUNCH,
		DINNER,
		NIGHTMEAL,
		SNACK;

		public String getString(Context context){
			String[] array = context.getResources().getStringArray(R.array.ENUM_NAME);
			String tmp = "";
			switch(this){
				case BREAKFAST:
					tmp = array[0];
					break;
				case LUNCH:
					tmp = array[1];
					break;
				case DINNER:
					tmp = array[2];
					break;
				case NIGHTMEAL:
					tmp = array[3];
					break;
				case SNACK:
					tmp = array[4];
					break;
			}
			return tmp;
		}
	};

	public DietActivity(Calendar calendar){
		this(new ArrayList<Food>(), calendar);
	}

	public DietActivity(List<Food> listOfFood, Calendar calendar){
        this("Unidentified", listOfFood, calendar);
	}

    public DietActivity(String name, List<Food> listOfFood, Calendar calendar) {
        this.foodList = listOfFood;
        this.name = name;
        setDate(calendar);
        update();
    }

	public DietActivity(Recipe recipe, int numberOfPortions, String name, Calendar calendar){
		this(name, recipe.getMealList(numberOfPortions), calendar);
	}

	private void addFood(Food food){
		foodList.add(food);
	}

	public List<Food> getFoodList(){
//		ArrayList<Food> tmp = new ArrayList<Food>();
//		for (Food f: foodList){
//			tmp.add(f);
//		}
//		return tmp;
		return foodList;
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
			if(edap.list != null && !edap.list.isEmpty()){
				this.foodList = edap.list;
			}
			if(edap.date != null && edap.date != null){
				setDate(edap.date);
			}
			if(edap.name != null){
				setName(edap.name);
			}
			if(edap.mealtype != null){
				setMealtype(edap.mealtype);
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

	public double getCalorieCount() {
		return calorieCount;
	}

	public double getCarbCount() {
		return carbCount;
	}

	public double getFatCount() {
		return fatCount;
	}

	public double getProteinCount() {
		return proteinCount;
	}

    public String getName() { return name;  }

    public void setName(String name) {
        this.name = name;
    }

	public void setMealtype(MEALTYPE mealtype) {
		this.mealtype = mealtype;
	}

	public MEALTYPE getMealtype() {
		return mealtype;
	}
}
