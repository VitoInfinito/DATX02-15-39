package com.kandidat.datx02_15_39.tok.layout;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.kandidat.datx02_15_39.tok.utilies.Database;
import com.kandidat.datx02_15_39.tok.model.diet.AddToDietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.Food;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddDietActivity extends CustomActionBarActivity {

	private int activatedObject = R.id.food_button_view_diet;
	private ListView searchResultList;
	private ArrayList<Food> searchResultFood, foodItemAdded;
	private SearchResultAdapter sra;
	public static String itemsList = "List";
	DietActivity newActivity;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_diet);
		initMenu(R.layout.activity_add_diet);
		findViewById(activatedObject).setActivated(true);
		//foodItemAdded = new ArrayList<Food>();
		savedInstanceState = this.getIntent().getExtras();
		if(savedInstanceState != null){
			if (savedInstanceState.getSerializable(itemsList) != null)
				newActivity = (DietActivity) savedInstanceState.getSerializable(AddDietActivity.itemsList);
		}
		if(newActivity == null) {
			Calendar c = Calendar.getInstance();
			newActivity = new DietActivity(c);
		}
		searchForItems("");
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(savedInstanceState != null){
			if (savedInstanceState.getSerializable(itemsList) != null)
				newActivity = (DietActivity) savedInstanceState.getSerializable(AddDietActivity.itemsList);
		}
		if(newActivity == null) {
			Calendar c = Calendar.getInstance();
			newActivity = new DietActivity(c);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Intent intent = new Intent(this, ViewAddDietActivity.class);
		Bundle bundle = new Bundle();
		//bundle.putSerializable(this.itemsList, foodItemAdded);									//TODO
		bundle.putSerializable(this.itemsList, newActivity);
		intent.putExtras(bundle);
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_with_moveforward, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//noinspection SimplifiableIfStatement
		if (id == R.id.right_corner_button_moveforward) {
			Intent intent = new Intent(this, ViewAddDietActivity.class);
			Bundle bundle = new Bundle();
			//bundle.putSerializable(this.itemsList, foodItemAdded);									//TODO
			bundle.putSerializable(this.itemsList, newActivity);
			intent.putExtras(bundle);
			startActivity(intent);
			this.finish();
		}
		//This will be called to be able to see if you pressed the menu
		return super.onOptionsItemSelected(item);
	}

	private void searchForItems(String searchWord){
		searchResultFood = new ArrayList<>(Database.getInstance().searchForFood(searchWord));
		updateSearchList();
		((TextView)findViewById(R.id.info_about_search_list)).setText("Search result for " + searchWord);
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
					startActivity(new Intent(this, BluetoothActivity.class));
				}
				int amount = ((LinearLayout) findViewById(R.id.button_container)).getChildCount();
				View child;
				for (int i = 0; i < amount; i++) {
					child = ((LinearLayout) findViewById(R.id.button_container)).getChildAt(i);
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
		searchResultList = (ListView) findViewById(R.id.food_search_item_container);
		searchResultList.removeAllViewsInLayout();
		sra = new SearchResultAdapter(this);
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
		if(findViewById(R.id.recipe_button_view_diet).isActivated()){
			//TODO when we implement so we have a database and can store food
			Toast.makeText(this, "Diet_button"
					+ searchResultFood.get(position).getName()
					, Toast.LENGTH_SHORT).show();
		}else if(findViewById(R.id.scale_button_view_diet).isActivated()){
			//TODO Can only be made when we have connected with the scale
			Toast.makeText(this, "Scale_button" + searchResultFood.get(position).getName(), Toast.LENGTH_SHORT).show();
		}else if(findViewById(R.id.food_button_view_diet).isActivated()){
			//foodItemAdded.add(sra.getItem(position));												//TODO
			newActivity.add(new AddToDietActivity(sra.getItem(position)));

			Toast.makeText(this, "Diet_button"
					+ searchResultFood.get(position).getName()
					+ "Item added"
					, Toast.LENGTH_SHORT).show();
		}

	}
	void message(){
		Toast.makeText(this, "Hej", Toast.LENGTH_SHORT).show();
	}

}
