package com.kandidat.datx02_15_39.tok.layout;



import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietDiary;
import com.kandidat.datx02_15_39.tok.model.diet.EditDietActivityParams;
import com.kandidat.datx02_15_39.tok.model.diet.Food;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class AddDietActivity extends CustomActionBarActivity {

	private int activatedObject = R.id.food_button_view_diet;
	private ListView searchResultList;
	private ArrayList<Food> searchResultFood, foodItemAdded;
	private SearchResultAdapter sra;
	private DietDiary diary;
	public static String itemsList = "List";
	public static int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter mBluetoothAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_diet);
		initMenu(R.layout.activity_add_diet);
		findViewById(activatedObject).setActivated(true);
		searchResultFood = new ArrayList<Food>();
		foodItemAdded = new ArrayList<Food>();
		diary = DietDiary.getInstance();
		Calendar c = Calendar.getInstance();
		List<Food> tmp = new ArrayList<Food>();
		tmp.add(new Food(200, 300,400,500, "Gunnar", "höger lår på kyckling", Food.FoodPrefix.g, 100));
		searchResultFood.add(tmp.get(0));
		DietActivity da = new DietActivity(c);
		diary.addActivity(c.getTime(), da);
		EditDietActivityParams edap = new EditDietActivityParams(c.getTime(), tmp);
		diary.editActivity(c, "000001", edap);
		foodItemAdded.add(tmp.get(0));
		//updateSearchList();
	}

	@Override
	protected void onStart() {
		super.onStart();
		//BLUETOOTH CONNECT
		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter == null){

		}
		if(!mBluetoothAdapter.isEnabled()){
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}

		Toast.makeText(this, "" + mBluetoothAdapter.getName(), Toast.LENGTH_SHORT).show();
		bluetooth();
	}

	private void bluetooth(){
		ArrayAdapter mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

		ListView lv = (ListView) findViewById(R.id.food_search_item_container);

		if(pairedDevices.size() > 0){
			for (BluetoothDevice bd: pairedDevices){
				mArrayAdapter.add(bd.getName() + "\n" + bd.getAddress());
				if(bd.getName().equals("Beurer KS800")){
					Toast.makeText(this, "" + bd.describeContents(), Toast.LENGTH_SHORT).show();
				}
			}
		}
		lv.setAdapter(mArrayAdapter);

	}

	private void updateSearchList(List<Food> foodList){
		searchResultFood = new ArrayList<Food>(foodList);
		updateSearchList();
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

	private List<Food> searchForItems(String searchWord){
		ArrayList<Food> tmp = new ArrayList<Food>();
		//TODO the search
		return tmp;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}


	public void onDietSelectorClick(View view) {
		if(view instanceof ImageButton) {
			ImageButton ib = (ImageButton) view;
			if (ib.getId() == R.id.barcode_button_view_diet) {
				//TODO Change View to a Barcode app
			}else{
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
			bundle.putSerializable(this.itemsList, foodItemAdded);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		//This will be called to be able to see if you pressed the menu
		return super.onOptionsItemSelected(item);
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
			selectedItem(position);
		}
	}

	private void selectedItem(int position) {
		if(findViewById(R.id.recipe_button_view_diet).isActivated()){
			Toast.makeText(this, "Diet_button" + searchResultFood.get(position).getName(), Toast.LENGTH_SHORT).show();
		}else if(findViewById(R.id.scale_button_view_diet).isActivated()){
			Toast.makeText(this, "Scale_button" + searchResultFood.get(position).getName(), Toast.LENGTH_SHORT).show();
		}else if(findViewById(R.id.food_button_view_diet).isActivated()){
			Toast.makeText(this, "Food_button" + searchResultFood.get(position).getName(), Toast.LENGTH_SHORT).show();
		}

	}
}
