package com.kandidat.datx02_15_39.tok.model.diet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lagerstedt on 2015-04-27.
 */
public class Recipe {

	List<Food> listOfFoodItem;
	double numberOfPortions;
	String recipeName;
	private double calorieCount = 0, proteinCount = 0, fatCount = 0, carbCount = 0;
	public Recipe(List<Food> listOfFoodItem, int numberOfPortions){
		this("Unnamed", listOfFoodItem, numberOfPortions);
	}

	public Recipe(String nameOfRecipe, List<Food> listOfFoodItem, int numberOfPortions){
		recipeName = nameOfRecipe;
		if(listOfFoodItem != null) {
			this.listOfFoodItem = listOfFoodItem;
			update();
		}else {
			this.listOfFoodItem = new ArrayList<>();
		}
		if(numberOfPortions > 0 ) {
			this.numberOfPortions = numberOfPortions;
		}else {
			this.numberOfPortions = 1;
		}
	}

	private void update(){
		for(Food f: listOfFoodItem){
			calorieCount += f.getCalorieAmount();
			proteinCount += f.getProteinAmount();
			fatCount += f.getFatAmount();
			carbCount += f.getCarbAmount();
		}
	}

	public List<Food> getMealList(double numberPortion) {
		ArrayList<Food> tmp = new ArrayList<>();
		for(Food f: listOfFoodItem){
			Food food = f.clone();
			food.setAmount(f.getAmount() * (this.numberOfPortions / numberPortion));
			tmp.add(food);
		}
		return tmp;
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

	public double getCalorieCountPortion() {
		return calorieCount/numberOfPortions;
	}

	public double getCarbCountPortion() {
		return carbCount/numberOfPortions;
	}

	public double getFatCountPortion() {
		return fatCount/numberOfPortions;
	}

	public double getProteinCountPortion() {
		return proteinCount/numberOfPortions;
	}


	public String getName() { return recipeName;  }
}
