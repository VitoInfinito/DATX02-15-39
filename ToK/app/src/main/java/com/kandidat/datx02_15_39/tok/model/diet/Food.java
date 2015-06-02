package com.kandidat.datx02_15_39.tok.model.diet;

import java.io.Serializable;

/**
 * Class to represent Food Items you can eat whit the nutritional values with the prefix and amount.
 * This is a way to have these food obejcts standardize in our application.
 */
public class Food implements Serializable {
	private final double calorieAmount;
	private final double proteinAmount;
	private final double fatAmount;
	private final double carbAmount;
	private final String name, description;
	private double amount;
	private final FoodPrefix prefix;

	public Food(double calorieAmount, double proteinAmount, double fatAmount, double carbAmount, String name, String description, FoodPrefix prefix, double amount) {
		this(calorieAmount,proteinAmount,fatAmount,carbAmount,name,description,prefix);
	this.amount = amount;
	}

	public static enum FoodPrefix{
		ml,
		g,
		st,
	};

	/**
	 * Method to clone a food object to always have a one copy of a food object.
	 * @return
	 */
	public Food clone(){
		return new Food(calorieAmount, proteinAmount, fatAmount
				, carbAmount, name, description
				, prefix, amount);
	}

	protected Food(){
		calorieAmount = 0;
		proteinAmount = 0;
		fatAmount = 0;
		carbAmount = 0;
		name ="default name";
		description = "default desc";
		prefix = FoodPrefix.g;
	}

	public Food(double calorieAmount, double proteinAmount, double fatAmount, double carbAmount, String name, String description, FoodPrefix prefix){
		if(calorieAmount >= 0)
			this.calorieAmount = calorieAmount;
		else
			this.calorieAmount = 0;
		if (proteinAmount >= 0)
			this.proteinAmount = proteinAmount;
		else
			this.proteinAmount = 0;
		if (fatAmount >= 0)
			this.fatAmount = fatAmount;
		else
			this.fatAmount = 0;
		if (carbAmount >= 0)
			this.carbAmount = carbAmount;
		else
			this.carbAmount = 0;
		this.prefix = prefix;
		this.name = name;
		this.description = description;
	}

	public void setAmount(double amount){
		if(amount < 0){
			this.amount = 0;
		}else {
			this.amount = amount;
		}
	}

	/**
	 * To convert all nutrition value according to the amount
	 * @param d - the amount
	 * @return
	 */
	private double getAmountMultiplier(double d){
		if(this.prefix == FoodPrefix.g || this.prefix == FoodPrefix.ml){
			return (d/100.0);
		}else{
			return d;
		}
	}

	/**
	 * Getter to get the amount of calorie of a food object
	 * @return
	 */
	public double getCalorieAmount() {
		return calorieAmount * getAmountMultiplier(amount);
	}


	/**
	 * Getter to get the amount of protein of a food object
	 * @return
	 */
	public double getProteinAmount() {
		return proteinAmount * getAmountMultiplier(amount);
	}

	/**
	 * Getter to get the amount of fat of a food object
	 * @return
	 */
	public double getFatAmount() {
		return fatAmount* getAmountMultiplier(amount);
	}

	/**
	 * Getter to get the amount of Carbs of a food object
	 * @return
	 */
	public double getCarbAmount() {
		return carbAmount* getAmountMultiplier(amount);
	}


	/**
	 * Getter to get the name of a food object
	 * @return
	 */
	public String getName() {
		return name;
	}


	/**
	 * Getter to get the Descriptions
	 * @return
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * Getter to get the amount of the food object
	 * @return
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Getter to get the prefix of the food object
	 * @return
	 */
	public FoodPrefix getPrefix() {
		return prefix;
	}
}
