package com.kandidat.datx02_15_39.tok.layout;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietDiary;
import com.kandidat.datx02_15_39.tok.model.diet.EditDietActivityParams;
import com.kandidat.datx02_15_39.tok.model.diet.Food;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddDietActivity extends CustomActionBarActivity {

	private int activatedObject = R.id.food_button;
	private ListView searchResultList;
	private SearchResultAdapter sra;
	private DietDiary diary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_diet);
		initMenu(R.layout.activity_add_diet);
		findViewById(activatedObject).setActivated(true);
		diary = DietDiary.getInstance();
		Calendar c = Calendar.getInstance();
		List<Food> tmp = new ArrayList<Food>();
		tmp.add(new Food(200, 300,400,500, "Gunnar", "höger lår på kyckling"));
		DietActivity da = new DietActivity(c);
		diary.addActivity(c.getTime(), da);
		EditDietActivityParams edap = new EditDietActivityParams(c.getTime(), tmp);
		diary.editActivity(c, "000001", edap);
		addItemToSearchResult(((DietActivity) diary.getActivity(c, "000001")).getFoodList().get(0).getName(), ((DietActivity) diary.getActivity(c, "000001")).getFoodList().get(0).getCalorieAmount() + "");
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	public void addItemToSearchResult(String name, String kcal){
		searchResultList = (ListView) findViewById(R.id.food_search_item_container);
		 sra = new SearchResultAdapter(this);
		sra.add(new SearchItems(name, kcal));
		if(searchResultList != null){
			searchResultList.setAdapter(sra);
		}
		searchResultList.setOnItemClickListener(new SearchItemClickListener());
	}

	public void onDietSelectorClick(View view) {
		if(view instanceof ImageButton) {
			ImageButton ib = (ImageButton) view;
			if (ib.getId() == R.id.barcode_button) {
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
		if (id == R.id.right_corner_button) {
			View add = getLayoutInflater().inflate(R.layout.activity_add_all, null);
			AlertDialog ad = new AlertDialog.Builder(this, R.style.CustomDialog)
					.create();
			ad.setView(add);
			ad.show();
		}
		//This will be called to be able to see if you pressed the menu
		return super.onOptionsItemSelected(item);
	}

	public class SearchItems{
		public String name;
		public String kcal;
		public SearchItems(String name, String kcal)
		{
			this.name = name;
			this.kcal = kcal;
		}
	}

	public class SearchResultAdapter extends ArrayAdapter<SearchItems>
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
			food_item_name.setHint(getItem(position).name);
			food_item_calorie.setHint(getItem(position).kcal);
			// Return the completed view to render on screen

			return convertView;
		}
	}

	private class SearchItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			//TODO
		}
	}
}
