package com.kandidat.datx02_15_39.tok.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.database.DataBaseHelper;
import com.kandidat.datx02_15_39.tok.model.diet.Food;
import com.kandidat.datx02_15_39.tok.utility.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to make it possible to add new food items to any meal or recipe.
 */
public class AddDietToFragment extends DietFragment {

	private SearchResultAdapter sra;

	List<Food> newFood;
	private ListView searchResultList;

	private EditRecipeFragment listenerFragment;

	private FragmentActivity listener;

	private DataBaseHelper mDbHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_add_diet_item_to, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (FragmentActivity) activity;
		mDbHelper = new DataBaseHelper(activity.getApplicationContext(), Utils.DATABASE_VERSION);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(newFood == null){
			newFood = new ArrayList<>();
		}
	}

	/**
	 * Method used to get the new added items. This is used by the class that opens this fragment
	 * and can then handle the newly added items.
	 * @return - A list of Food items
	 */
	public List<Food> getListOfFoodItems(){
		return newFood;
	}

	/**
	 * This method is a second hand to search in the Database aswell as updating the result
	 * of the search
	 */
	private void searchForItems(String searchWord){
		newFood = (ArrayList<Food>) mDbHelper.searchFoodItems(searchWord);
		updateSearchList();
		((TextView)getView().findViewById(R.id.info_about_search_list)).setText("Search result for " + searchWord);
	}

	/*
	Updates the list that is shown with the new food item
	 */
	private void updateSearchList(){
		searchResultList = (ListView) getView().findViewById(R.id.food_search_item_container);
		searchResultList.removeAllViewsInLayout();
		sra = new SearchResultAdapter(getView().getContext());
		for (Food f: newFood){
			sra.add(f);
		}
		if(searchResultList != null){
			searchResultList.setAdapter(sra);
		}
		searchResultList.setOnItemClickListener(new SearchItemClickListener());
	}

	/**
	 * Method to set the listenerFragment to be able to give that frament food items.
	 * @param fragment
	 */
	public void setFromFargment(EditRecipeFragment fragment) {
		listenerFragment = fragment;
	}

	/**
	 * This Class is added and extends ArrayAdapter and it lets me draw what i want to the list item
	 */
	private class SearchResultAdapter extends ArrayAdapter<Food>
	{

		public SearchResultAdapter  (Context context)
		{
			super(context,0);
		}

		public View getView (int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_food_item, null);

			}
			// Lookup view for data population
			TextView food_item_name = (TextView) convertView.findViewById(R.id.food_item_name);
			TextView food_item_calorie = (TextView) convertView.findViewById(R.id.food_calorie_amount);
			// Populate the data into the template view using the data object
			food_item_name.setHint(getItem(position).getName());
			food_item_calorie.setHint(getItem(position).getCalorieAmount() + "");
			// Return the completed view to render on screen

			return convertView;
		}
	}


	/**
	 * A listener to the list view to handle input
	 */
	private class SearchItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			addFood(newFood.get(position));
		}
	}

	/**
	 * After adding a food item, the fragment will be removed and if it has an listener fragment,
	 * that fragment will get a food item.
	 * @param food
	 */
	private void addFood(Food food){
		if(listenerFragment != null){
			listenerFragment.recipeUpdate(food);
		}
		listener.getSupportFragmentManager().beginTransaction().remove(this).commit();
	}
}
