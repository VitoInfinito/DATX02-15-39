package com.kandidat.datx02_15_39.tok.model.diet;

import com.kandidat.datx02_15_39.tok.utilies.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lagerstedt on 2015-04-27.
 */
public class RecipeCollection {

	private static RecipeCollection instance;
	private List<Recipe> listOfRecipes;

	public static RecipeCollection getInstance(){
		if(instance == null){
			instance = new RecipeCollection();
		}
		return instance;
	}

	protected RecipeCollection(){
		listOfRecipes = new ArrayList<>();
		listOfRecipes.add(new Recipe("Mosbricka", Database.getInstance().searchForFood(""), 10));
	}



	public List<Recipe> getList(){
		return listOfRecipes;
	}

}
