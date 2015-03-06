package com.kandidat.datx02_15_39.tok.model.diet;

import java.io.Serializable;

/**
 * Created by Lagerstedt on 2015-02-16.
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

	private double getAmountMultiplier(double d){
		if(this.prefix == FoodPrefix.g || this.prefix == FoodPrefix.ml){
			return (d/100.0);
		}else{
			return d;
		}
	}

	public double getCalorieAmount() {
		return calorieAmount * getAmountMultiplier(amount);
	}

	public double getProteinAmount() {
		return proteinAmount * getAmountMultiplier(amount);
	}

	public double getFatAmount() {
		return fatAmount* getAmountMultiplier(amount);
	}

	public double getCarbAmount() {
		return carbAmount* getAmountMultiplier(amount);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
