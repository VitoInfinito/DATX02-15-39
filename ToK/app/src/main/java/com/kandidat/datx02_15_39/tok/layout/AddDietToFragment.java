package com.kandidat.datx02_15_39.tok.layout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.Food;
import com.kandidat.datx02_15_39.tok.utilies.Database;
import com.kandidat.datx02_15_39.tok.utility.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Lagerstedt on 2015-05-05.
 */
public class AddDietToFragment extends Fragment {

	private SearchResultAdapter sra;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_view_added_diet, container, false);
	}

	List<Food> newFood;
	private ListView searchResultList;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(newFood == null){
			//TODO Något men för tillfället skapar man en ny
			newFood = new ArrayList<Food>();
		}
		//TODO if getActivity instanceof not diary update time and name
		if(!(getActivity() instanceof AddDietActivity2) && getView() != null){
//			getView().setBackgroundColor(0xFFFFFFFF);
			getView().setBackgroundResource(R.drawable.border_white_background);
		}

		getActivity().setTitle(getResources().getString(R.string.AddDietToFragment));
	}

	public List<Food> getListOfFoodItems(){
		return newFood;
	}

	private void searchForItems(String searchWord){
		newFood = new ArrayList<>(Database.getInstance().searchForFood(searchWord));
		updateSearchList();
		((TextView)getView().findViewById(R.id.info_about_search_list)).setText("Search result for " + searchWord);
	}

	/*
	Updates the list that is show so that new items appear
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

	private class SearchItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {

		}
	}


}
