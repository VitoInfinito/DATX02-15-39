package com.kandidat.datx02_15_39.tok.model.diet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a Recipe, this is usefull to keep track of total nutritional values in the meal.
 */
public class Recipe implements Serializable{

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

	/**
	 * Updates the recipe to have correct nutritional values according to all its food items
	 */
	private void update(){
		for(Food f: listOfFoodItem){
			calorieCount += f.getCalorieAmount();
			proteinCount += f.getProteinAmount();
			fatCount += f.getFatAmount();
			carbCount += f.getCarbAmount();
		}
	}

	/**
	 *
	 * @param numberPortion
	 * @return - A list containing Food items with the correct amout of all items with the relation of
	 * the choosen protions/recipes portions.
	 */
	public List<Food> getMealList(double numberPortion) {
		ArrayList<Food> tmp = new ArrayList<>();
		for(Food f: listOfFoodItem){
			Food food = f.clone();
			food.setAmount(f.getAmount() * (numberPortion / this.numberOfPortions ));
			tmp.add(food);
		}
		return tmp;
	}

	/**
	 *
	 * @return - the number of portions the Recipe is
	 */
	public double getNumberOfPortions(){
		return numberOfPortions;
	}

	/**
	 *
	 * @return - the list of all items in the recipe
	 */
	public List<Food> getListOfFoodItem(){
		return listOfFoodItem;
	}

	/**
	 * Removes a Food object from the recipe
	 * @param position - the position of the object that should be removed
	 */
	public void removeFood(int position){
		listOfFoodItem.remove(position);
		update();
	}

	/**
	 * Method to add a food object to the recipe
	 * @param food
	 */
	public void addFood(Food food){
		listOfFoodItem.add(food);
		update();
	}

	/**
	 * Method to change the number of portions of the recipe
	 * @param numberOfPortions - the new number of portions
	 */
	public void changeAmountOfPortions(double numberOfPortions){
		this.numberOfPortions = numberOfPortions;
		update();
	}

	/**
	 *
	 * @return - the number of total calorie amount
	 */
	public double getCalorieCount() {
		return calorieCount;
	}

	/**
	 *
	 * @return - the number of total Carbs amount
	 */
	public double getCarbCount() {
		return carbCount;
	}

	/**
	 *
	 * @return - the number of total fat amount
	 */
	public double getFatCount() {
		return fatCount;
	}

	/**
	 *
	 * @return - the number of total protein amount
	 */
	public double getProteinCount() {
		return proteinCount;
	}

	/**
	 *
	 * @return - the number of total calorie amount for a portion
	 */
	public double getCalorieCountPortion() {
		return calorieCount/numberOfPortions;
	}

	/**
	 *
	 * @return - the number of total carb amount for a portion
	 */
	public double getCarbCountPortion() {
		return carbCount/numberOfPortions;
	}

	/**
	 *
	 * @return - the number of total fat amount for a portion
	 */
	public double getFatCountPortion() {
		return fatCount/numberOfPortions;
	}

	/**
	 *
	 * @return - the number of total protein amount for a portion
	 */
	public double getProteinCountPortion() {
		return proteinCount/numberOfPortions;
	}

	/**
	 *
	 * @return - the name of the recipe
	 */
	public String getName() {
		return recipeName;
	}

	/**
	 * Method to set the recipe name.
	 * @param name - the name to be set
	 */
	public void setRecipeName(String name){
		this.recipeName = name;
	}
}
