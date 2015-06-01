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
 * A class that represent a DietActivity that holds all information about a meal or snack you have eaten.
 */
public class DietActivity extends AbstractDiaryActivity implements Serializable {

	private List<Food> foodList;
	private double calorieCount, proteinCount, fatCount, carbCount;
    private String name;
	private MEALTYPE mealtype = MEALTYPE.SNACK;

	/**
	 * Mealtype is used to discribe what type of meal that you eat to categorizing you DietActivity
	 */
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
        this("", listOfFood, calendar);
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

	/**
	 *
	 * @return - A list containing all food items in a 'Meal'
	 */
	public List<Food> getFoodList(){
		// This Commented code is the original structur
		// But due to some problems this is not the way anymore.
//		ArrayList<Food> tmp = new ArrayList<Food>();
//		for (Food f: foodList){
//			tmp.add(f);
//		}
//		return tmp;
		return foodList;
	}

	/**
	 * Used to alway update the number of carb, cal, fat and protein in a meal when something change.
	 */
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

	/**
	 *
	 * @return - the calorie count of all food objects in the dietactivity
	 */
	public double getCalorieCount() {
		return calorieCount;
	}

	/**
	 *
	 * @return - the carbs count of all food objects in the dietactivity
	 */
	public double getCarbCount() {
		return carbCount;
	}

	/**
	 *
	 * @return - the fat count of all food objects in the dietactivity
	 */
	public double getFatCount() {
		return fatCount;
	}

	/**
	 *
	 * @return - the protein count of all food objects in the dietactivity
	 */
	public double getProteinCount() {
		return proteinCount;
	}

	/**
	 *
	 * @return - the name of the meal(DietActivity)
	 */
    public String getName() { return name;  }

	/**
	 * Method to set the name of the meal(DietActivity)
	 * @param name
	 */
    public void setName(String name) {
        this.name = name;
    }

	/**
	 * Method to set a mealtype of this meal(DietActivity)
	 * @param mealtype
	 */
	public void setMealtype(MEALTYPE mealtype) {
		this.mealtype = mealtype;
	}

	/**
	 *
	 * @return - the mealtype of the meal (DietActivity)
	 */
	public MEALTYPE getMealtype() {
		return mealtype;
	}
}
