package com.kandidat.datx02_15_39.tok.layout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.AddToDietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.Food;
import com.kandidat.datx02_15_39.tok.utilies.Database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Lagerstedt on 2015-04-23.
 */
public class AddDietFragment extends DietFragment{

	private int activatedObject = R.id.food_button_view_diet;
	private ListView searchResultList;
	private ArrayList<Food> searchResultFood;
	private SearchResultAdapter sra;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_add_diet, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}

	private void init() {
		//Init listeners to button on fragment
		View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onDietSelectorClick(v);
			}
		};
		(getView().findViewById(R.id.food_button_view_diet)).setOnClickListener(listener);
		(getView().findViewById(R.id.scale_button_view_diet)).setOnClickListener(listener);
		(getView().findViewById(R.id.recipe_button_view_diet)).setOnClickListener(listener);
		(getView().findViewById(R.id.barcode_button_view_diet)).setOnClickListener(listener);
		searchForItems("");
		getView().findViewById(activatedObject).setActivated(true);
		Bundle bundle = getArguments();
		if(bundle != null){
			Object object = bundle.getSerializable(AddDietActivity2.dietActivityArgument);
			if (object instanceof DietActivity) {
			newActivity = (DietActivity) object;
			}else {
				newActivity = new DietActivity(Calendar.getInstance());
			}
		}else {
			newActivity = new DietActivity(Calendar.getInstance());
		}
	}



	private void searchForItems(String searchWord){
		searchResultFood = new ArrayList<>(Database.getInstance().searchForFood(searchWord));
		updateSearchList();
		((TextView)getView().findViewById(R.id.info_about_search_list)).setText("Search result for " + searchWord);
	}

	/**
	 * When the barcode button is pressed the Barcode app will open and we will ask for the EAN,
	 * this will not activate the button and therefore Barcode cant give a search result.
	 * The Scale button will try to connect to the the scale and recive some information
	 * if its connected or not.
	 * All but Barcode is activatable
	 * @param view
	 */
	public void onDietSelectorClick(View view) {
		if(view instanceof ImageButton) {
			ImageButton ib = (ImageButton) view;
			if (ib.getId() == R.id.barcode_button_view_diet) {
				//TODO Change View to a Barcode app
			}else{
				if(ib.getId() == R.id.scale_button_view_diet) {
					//this.startBluetooth();
					startActivity(new Intent(getView().getContext(), BluetoothActivity.class));
				}
				int amount = ((LinearLayout) getView().findViewById(R.id.button_container)).getChildCount();
				View child;
				for (int i = 0; i < amount; i++) {
					child = ((LinearLayout) getView().findViewById(R.id.button_container)).getChildAt(i);
					if (child instanceof ImageButton) {
						child.setActivated(false);
						child.setFocusableInTouchMode(false);
					}
				}
				ib.setActivated(true);
			}
		}
	}

	/*
	Updates the list that is show so that new items appear
	 */
	private void updateSearchList(){
		searchResultList = (ListView) getView().findViewById(R.id.food_search_item_container);
		searchResultList.removeAllViewsInLayout();
		sra = new SearchResultAdapter(getView().getContext());
		for (Food f: searchResultFood){
			sra.add(f);
		}
		if(searchResultList != null){
			searchResultList.setAdapter(sra);
		}
		searchResultList.setOnItemClickListener(new SearchItemClickListener());
	}

	private void updateSearchList(List<Food> foodList){
		searchResultFood = new ArrayList<Food>(foodList);
		updateSearchList();
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
	 * This class is handel all the clicks on the listview
	 */
	private class SearchItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			selectedItem(position);
		}
	}

	private void selectedItem(int position) {
		if(getView().findViewById(R.id.recipe_button_view_diet).isActivated()){
			//TODO when we implement so we have a database and can store food
			Toast.makeText(getView().getContext(), "Diet_button"
					+ searchResultFood.get(position).getName()
					, Toast.LENGTH_SHORT).show();
		}else if(getView().findViewById(R.id.scale_button_view_diet).isActivated()){
			//TODO Can only be made when we have connected with the scale
			Toast.makeText(getView().getContext(), "Scale_button" + searchResultFood.get(position).getName(), Toast.LENGTH_SHORT).show();
		}else if(getView().findViewById(R.id.food_button_view_diet).isActivated()){
			//foodItemAdded.add(sra.getItem(position));												//TODO
			newActivity.add(new AddToDietActivity(sra.getItem(position).clone()));

			Toast.makeText(getView().getContext(), "Diet_button"
					+ searchResultFood.get(position).getName()
					+ "Item added"
					, Toast.LENGTH_SHORT).show();
		}

	}
	void message(){
		Toast.makeText(getView().getContext(), "Hej", Toast.LENGTH_SHORT).show();
	}

}
