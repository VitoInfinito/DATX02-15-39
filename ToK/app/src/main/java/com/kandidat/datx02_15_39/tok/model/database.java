package com.kandidat.datx02_15_39.tok.model;

import com.kandidat.datx02_15_39.tok.model.diet.Food;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lagerstedt on 2015-03-26.
 */
public class Database {

	public ArrayList<Food> base = new ArrayList<>();
	private static Database instance;

	public static Database getInstance(){
		if(instance == null){
			instance = new Database();
		}
		return instance;
	}

	protected Database(){
		base.add(new Food(300, 10, 20, 10, "Mos", "mosad potatis", Food.FoodPrefix.g, 100));
		base.add(new Food(300, 10, 20, 10, "Fläsk", "mosad potatis", Food.FoodPrefix.g, 100));
		base.add(new Food(300, 34, 20, 10, "Kyckling", "mosad potatis", Food.FoodPrefix.g, 100));
		base.add(new Food(300, 22, 20, 50, "Mjölk", "mosad potatis", Food.FoodPrefix.ml, 100));
		base.add(new Food(300, 33, 50, 30, "Potatis", "mosad potatis", Food.FoodPrefix.g, 100));
		base.add(new Food(300, 45, 20, 50, "Ägg", "mosad potatis", Food.FoodPrefix.st, 1));
		base.add(new Food(300, 20, 70, 70, "Gurka", "mosad potatis", Food.FoodPrefix.g, 100));
		base.add(new Food(300, 10, 30, 43, "Pasta", "mosad potatis", Food.FoodPrefix.g, 100));
		base.add(new Food(100, 15, 3, 67, "Lax", "mosad potatis", Food.FoodPrefix.g, 100));
		base.add(new Food(1337, 100, 0, 0, "Drömmar", "mosad potatis", Food.FoodPrefix.g, 100));
	}

	public List<Food> searchForFood(String searchWord){
		return base;
	}
}
