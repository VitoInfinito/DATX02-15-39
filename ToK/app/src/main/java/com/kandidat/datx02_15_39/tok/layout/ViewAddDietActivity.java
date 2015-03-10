package com.kandidat.datx02_15_39.tok.layout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietDiary;
import com.kandidat.datx02_15_39.tok.model.diet.Food;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ViewAddDietActivity extends CustomActionBarActivity {

	Calendar calendar, today;
	ArrayList<Food> itemsAdded;
	private ListView searchResultList;
	private SearchResultAdapter sra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_add_diet);
		initMenu(R.layout.activity_view_add_diet);
		initCalendar();
		savedInstanceState = this.getIntent().getExtras();
		if(savedInstanceState != null){
			itemsAdded = (ArrayList<Food>)savedInstanceState.getSerializable(AddDietActivity.itemsList);
		}else{
			itemsAdded = new ArrayList<Food>();
		}
		updateList();
	}

	private void initCalendar(){
		calendar = setupCalendar();
		today = setupCalendar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_with_confirm, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.right_corner_button_confirm) {
			DietActivity da = createDietActivity();
			DietDiary.getInstance().addActivity(da.getDate(), da);
			startActivity(new Intent(this, MainActivity.class));
		}

		return super.onOptionsItemSelected(item);
	}

	private DietActivity createDietActivity() {
		DietActivity tmp = new DietActivity(itemsAdded, calendar);
		//TODO Make the enum mealtype in DietActivity that can be set!
		return tmp;
	}

	/*
	 * Sets upp a calender that can be compared only between 2 dates!
	 * The Hour, Minute, Seconds, and milliseconds is set to 0.
	 * @return
	 */
	private Calendar setupCalendar(){
		Calendar tmp = Calendar.getInstance();
		tmp.set(Calendar.HOUR_OF_DAY, 0);
		tmp.set(Calendar.MINUTE, 0);
		tmp.set(Calendar.SECOND,0);
		tmp.set(Calendar.MILLISECOND,0);
		return tmp;
	}

	private class DatePickerListener implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			Calendar tmp = setupCalendar();
			tmp.set(year, monthOfYear, dayOfMonth);
			if(!today.before(tmp))
				calendar.set(year, monthOfYear, dayOfMonth);
			setDateOnButton();
		}
	}

	/**
	 * Call this when A date button is pressed and you want to show a DatePicker
	 * to add a new date (The show date will be the choosen date, so first it will be today)
	 * @param view
	 */
	public void onDietDateClick(View view){
		DatePickerDialog picker = new DatePickerDialog(this, 0, new DatePickerListener(),
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));

		picker.show();
	}

	/*
	 * This method sets the current choosen date to a specific button
	 * @return
	 */
	private boolean setDateOnButton(){
		Calendar tmp = setupCalendar();
		tmp.set(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
		tmp.add(Calendar.DAY_OF_MONTH, -1);
		if(calendar.equals(tmp)){
			((Button)findViewById(R.id.diet_choose_date_button)).setText("Igår");
			return true;
		}else if(calendar.equals(today)){
			((Button)findViewById(R.id.diet_choose_date_button)).setText("Idag");
			return true;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		((Button) findViewById(R.id.diet_choose_date_button)).setText(sdf.format(calendar.getTime()));
		return true;
	}

	/*
	Updates the list that is show so that new items appear
	 */
	private void updateList(){
		searchResultList = (ListView) findViewById(R.id.food_item_added_container);
		searchResultList.removeAllViewsInLayout();
		sra = new SearchResultAdapter(this);
		for (Food f: itemsAdded){
			sra.add(f);
		}
		if(searchResultList != null){
			searchResultList.setAdapter(sra);
		}
	}

	private void mealSelector(MenuItem item){
		((Button)findViewById(R.id.meal_selector_button)).setText(item.getTitle());
	}

	/**
	 * This method will give you a popup menu under the meal selector on the view
	 * @param view
	 */
	public void onMealSelectorClick(View view){
		//Creating the instance of PopupMenu
		PopupMenu popup = new PopupMenu(this, (Button)view);
		//Inflating the Popup using xml file
		popup.getMenuInflater()
				.inflate(R.menu.popup_menu_meal, popup.getMenu());

		//registering popup with OnMenuItemClickListener
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				mealSelector(item);
				return true;
			}
		});
		popup.show(); //showing popup menu
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
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_meal_item_editable, null);
			}
			convertView.setOnTouchListener(new ItemSwipeListener(position,
					getResources().getDisplayMetrics().density));
			convertView.setClickable(true);
			// Lookup view for data population
			TextView food_item_name = (TextView) convertView.findViewById(R.id.food_item_name);
			Button food_amount = (Button) convertView.findViewById(R.id.btn_food_amount);
			Button food_prefix = (Button) convertView.findViewById(R.id.btn_food_prefix);
			ImageButton food_more = (ImageButton) convertView.findViewById(R.id.food_item_more_information);
			Button delete_food = (Button) convertView.findViewById(R.id.btn_remove_item_from_meal);
			// Populate the data into the template view using the data object
			food_item_name.setHint(getItem(position).getName());
			food_amount.setText(getItem(position).getAmount() + "");
			food_prefix.setText(getItem(position).getPrefix() + "");
			food_amount.setOnClickListener(new OnAmountClickListener(position));
			food_prefix.setOnClickListener(new OnPrefixClickListener(position));
			food_more.setFocusable(false);
			food_more.setOnClickListener(new OnMoreInfoClickListener(position));
			delete_food.setOnClickListener(new OnDeleteClickListener(position));
			// Return the completed view to render on screen
			return convertView;
		}
	}

	private class OnPrefixClickListener implements View.OnClickListener{

		private final int position;

		public OnPrefixClickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			changePrefix(v, position);
		}
	}

	private void changePrefix(View v, int position) {
		//TODO
	}

	private class OnAmountClickListener implements View.OnClickListener{

		private final int position;

		public OnAmountClickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			changeAmount(v, position);
		}
	}

	private void changeAmount(View v, int position) {
		//TODO
	}

	private class OnDeleteClickListener implements View.OnClickListener{


		private final int position;

		public OnDeleteClickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			deleteItem(v, position);
		}
	}

	private void deleteItem(View v, int position) {
		//TODO
	}

	private class OnMoreInfoClickListener implements View.OnClickListener {

		private final int position;

		public OnMoreInfoClickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			openExtendedInfo(v);
		}
	}

	private void openExtendedInfo(View v) {
		Toast.makeText(this, "dragen", Toast.LENGTH_SHORT).show();
		//TODO
	}

	private class ItemSwipeListener implements AdapterView.OnTouchListener{

		private static final int MIN_DISTANCE = 20;
		private float downX, upX;
		private int position;
		private float scale;

		public ItemSwipeListener(int position, float scale) {
			super();
			this.position = position;
			this.scale = scale;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					downX = event.getX();
					return false; // allow other events like Click to be processed
				}
				case MotionEvent.ACTION_MOVE: {
					upX = event.getX();

					float deltaX = downX - upX;

					// horizontal swipe detection
					if (Math.abs(deltaX / scale) > MIN_DISTANCE) {
						// left or right
						if (deltaX < 0 && (deltaX/scale) >(-96)) {
							v.setLeft((96) + (int)(deltaX));
							return false;
						}
						if (deltaX > 0 && (deltaX/scale) < 96) {
							v.setLeft((int)(deltaX) *(-1));
							return false;
						}
					}
					return true;
				}
				case MotionEvent.ACTION_CANCEL:{
					if((v.getLeft()/scale) <= (-47)){
						v.setLeft((int) ((-88) * scale));
					}else{
						v.setLeft(0);
					}
					return true;
				}
			}
			return false;
		}
	}
}
