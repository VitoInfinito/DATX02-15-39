package com.kandidat.datx02_15_39.tok.model.diet;

/**
 * Created by Lagerstedt on 2015-02-16.
 */
public class Food {
	private final double calorieAmount;
	private final double proteinAmount;
	private final double fatAmount;
	private final double carbAmount;
	private final String name, description;

	protected Food(){
		calorieAmount = 0;
		proteinAmount = 0;
		fatAmount = 0;
		carbAmount = 0;
		name ="default name";
		description = "default desc";
	}

	public Food(double calorieAmount, double proteinAmount, double fatAmount, double carbAmount, String name, String description){
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
		this.name = name;
		this.description = description;
	}

	public double getCalorieAmount() {
		return calorieAmount;
	}


	public double getProteinAmount() {
		return proteinAmount;
	}

	public double getFatAmount() {
		return fatAmount;
	}

	public double getCarbAmount() {
		return carbAmount;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
