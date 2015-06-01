package com.kandidat.datx02_15_39.tok.model.diet;

import com.kandidat.datx02_15_39.tok.utilies.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to collect all Recipes in a single to to keep track of all recipes
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


	/**
	 *
	 * @return - Returns the list of all recipes.
	 */
	public List<Recipe> getList(){
		return listOfRecipes;
	}

}
