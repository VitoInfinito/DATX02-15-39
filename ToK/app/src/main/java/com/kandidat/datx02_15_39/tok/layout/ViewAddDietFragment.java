package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietDiary;
import com.kandidat.datx02_15_39.tok.model.diet.EditDietActivityParams;
import com.kandidat.datx02_15_39.tok.model.diet.Food;
import com.kandidat.datx02_15_39.tok.utilies.SwipeableListAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Lagerstedt on 2015-04-23.
 */
public class ViewAddDietFragment extends DietFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_view_added_diet, container, false);
	}

	Calendar calendar, today;
	private ListView searchResultList;
	private SearchResultAdapter sra;
	private int extendedInfoOpenPosition = -1;
	private DietActivity.MEALTYPE mealType = DietActivity.MEALTYPE.SNACK;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initButton();
		initCalendar();
		Bundle bundle = getArguments();
		if(bundle != null) {
			Object object = bundle.getSerializable(AddDietActivity2.dietActivityArgument);
			if (object instanceof DietActivity) {
				newActivity = (DietActivity) object;
			}
		}
		if(newActivity == null){
			//TODO Något men för tillfället skapar man en ny
			newActivity = new DietActivity(today);
		}
		//TODO if getActivity instanceof not diary update time and name

		updateList();
	}

	private void initButton() {
		getView().findViewById(R.id.diet_choose_date_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onDietDateClick(v);
					}
				}
		);
		getView().findViewById(R.id.meal_selector_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onMealSelectorClick(v);
					}
				}
		);
	}

	private void initCalendar(){
		calendar = setupCalendar();
		today = setupCalendar();
	}

	public void editActivity(){
		DietDiary dietDiary = DietDiary.getInstance();
		EditDietActivityParams editDietActivityParams = new EditDietActivityParams(newActivity.getDate() , newActivity.getFoodList());
		dietDiary.editActivity(newActivity.getDate(), newActivity.getID(), editDietActivityParams);
	}


	public DietActivity createDietActivity() {
		//DietActivity tmp = new DietActivity(itemsAdded, calendar);
		newActivity.setName(((EditText) getView().findViewById(R.id.mealname)).getText().toString());
		newActivity.setMealtype(mealType);
		newActivity.setDate(calendar.getTime());
		Toast.makeText(getView().getContext(), "name of meal: " + newActivity.getName() + "/" + mealType, Toast.LENGTH_SHORT).show();
		return newActivity;
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
		DatePickerDialog picker = new DatePickerDialog(getView().getContext(), 0, new DatePickerListener(),
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
			((Button)getView().findViewById(R.id.diet_choose_date_button)).setText("Igår");
			return true;
		}else if(calendar.equals(today)){
			((Button)getView().findViewById(R.id.diet_choose_date_button)).setText("Idag");
			return true;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		((Button) getView().findViewById(R.id.diet_choose_date_button)).setText(sdf.format(calendar.getTime()));
		return true;
	}

	/*
	Updates the list that is show so that new items appear
	 */
	private void updateList(){
		searchResultList = (ListView) getView().findViewById(R.id.food_item_added_container);
		if(searchResultList.getChildCount() > 0) {
			searchResultList.removeAllViewsInLayout();
			//searchResultList.removeAllViews();
		}
		sra = new SearchResultAdapter(getView().getContext());
		for (Food f: newActivity.getFoodList()){//TODO
			sra.add(f);
		}
		if(searchResultList != null){
			searchResultList.setAdapter(sra);
		}
	}

	private void mealSelector(MenuItem item){
		((Button)getView().findViewById(R.id.meal_selector_button)).setText(item.getTitle());
		mealType = DietActivity.MEALTYPE.values()[item.getItemId()];
	}

	/**
	 * This method will give you a popup menu under the meal selector on the view
	 * @param view
	 */
	public void onMealSelectorClick(View view){
		//Creating the instance of PopupMenu
		PopupMenu popup = new PopupMenu(getView().getContext(), (Button)view);
		//Inflating the Popup using xml file
		int i = 0;
		for(DietActivity.MEALTYPE m: DietActivity.MEALTYPE.values()){
			popup.getMenu().add(i, i, i, m.getString(view.getContext()));
			++i;
		}
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
	private class SearchResultAdapter extends SwipeableListAdapter<Food>
	{

		public SearchResultAdapter(Context context) {
			super(context);
		}

		public View getView (int position, View convertView, ViewGroup parent)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_meal_item_editable, null);
			addSwipeDetection(this.getContext(), convertView, position);
			//convertView.setOnTouchListener(new ItemSwipeListener(position,
			//		getResources().getDisplayMetrics().density));
			convertView.setClickable(true);
			// Lookup view for data population
			TextView food_item_name = (TextView) convertView.findViewById(R.id.food_item_name);
			ImageButton food_more = (ImageButton) convertView.findViewById(R.id.food_item_more_information);
			Button delete_food = (Button) convertView.findViewById(R.id.btn_remove_item_from_meal);
			// Populate the data into the template view using the data object
			food_item_name.setHint(getItem(position).getName());
			food_more.setFocusable(false);
			food_more.setOnClickListener(new OnMoreInfoClickListener(position));
			delete_food.setOnClickListener(new OnDeleteClickListener(position));
			if(extendedInfoOpenPosition == position){
				openExtendedInfo(convertView, position);
			}
			// Return the completed view to render on screen
			return convertView;
		}

		public View getViewByPosition(int pos, ListView listView) {
			final int firstListItemPosition = listView.getFirstVisiblePosition();
			final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

			if (pos < firstListItemPosition || pos > lastListItemPosition ) {
				return listView.getAdapter().getView(pos, null, listView);
			} else {
				final int childIndex = pos - firstListItemPosition;
				return listView.getChildAt(childIndex);
			}
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
		//TODO should i be able to do this ?
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
		AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
		View view = getLayoutInflater(null).inflate(R.layout.number_picker_layout, null);
		NumberPicker np = (NumberPicker)view.findViewById(R.id.number_picker_amount);
		np.setMinValue(0);
		np.setMaxValue(10000);
		np.setValue((int) newActivity.getFoodList().get(position).getAmount());
		builder.setPositiveButton("Spara", new ChangedAmountListener(v, np, position));
		AlertDialog dialog = builder.create();
		dialog.setView(view);
		dialog.setCancelable(true);
		dialog.show();
	}

	private class ChangedAmountListener implements DialogInterface.OnClickListener {

		private NumberPicker np;
		private int position;
		private View v;

		public ChangedAmountListener(View v, NumberPicker np, int position){
			this.np = np;
			this.position = position;
			this.v = v;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			newActivity.getFoodList().get(position).setAmount(np.getValue());
			((Button)v).setText((float)np.getValue() + "");
		}
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
		List<Food> list = newActivity.getFoodList();
		Food food = list.remove(position);
//		updateList(); Kanske skall användas men inte säker!!
		sra.remove(sra.getItem(position));
	}

	private class OnMoreInfoClickListener implements View.OnClickListener {

		private final int position;

		public OnMoreInfoClickListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			onMoreInfoClick(v, position);
		}
	}


	private void onMoreInfoClick(View v, int position){
		if( this.extendedInfoOpenPosition == position){
			closeExtendedInfo(sra.getViewByPosition(extendedInfoOpenPosition, searchResultList),
					extendedInfoOpenPosition);
			extendedInfoOpenPosition = -1;
		}else {
			int y = position - searchResultList.getFirstVisiblePosition();
			if(this.extendedInfoOpenPosition > -1
					&& y >= -1
					&& y <= searchResultList.getChildCount()){
				closeExtendedInfo(sra.getViewByPosition(extendedInfoOpenPosition, searchResultList),
						extendedInfoOpenPosition);
			}
			openExtendedInfo(
					searchResultList.getChildAt(position - searchResultList.getFirstVisiblePosition()),
					position);
			this.extendedInfoOpenPosition = position;
		}
	}
	private void closeExtendedInfo(View v, int position){
		//TODO Make the opened information close
		//View view = sra.getViewByPosition(position, searchResultList);
		LinearLayout extendedView = (LinearLayout)v.findViewById(R.id.extended_food_information);
		extendedView.removeAllViews();
		extendedView.removeAllViewsInLayout();
	}

	private void openExtendedInfo(View v, int position) {
		//View view = searchResultList.getChildAt(position - searchResultList.getFirstVisiblePosition());
		LinearLayout extendedView = (LinearLayout)v.findViewById(R.id.extended_food_information);
		if(extendedView.getChildCount() == 0) {
			View convertExtendedView = LayoutInflater.from(v.getContext()).inflate(R.layout.change_amount_on_food_view, null);
			Button food_amount = (Button) convertExtendedView.findViewById(R.id.btn_food_amount);
			Button food_prefix = (Button) convertExtendedView.findViewById(R.id.btn_food_prefix);

			food_amount.setText(newActivity.getFoodList().get(position).getAmount() + "");
			food_prefix.setText(newActivity.getFoodList().get(position).getPrefix() + "");
			food_amount.setOnClickListener(new OnAmountClickListener(position));
			food_prefix.setOnClickListener(new OnPrefixClickListener(position));
			extendedView.addView(convertExtendedView);
		}
		Toast.makeText(getView().getContext(), "dragen"+ sra.getCount(), Toast.LENGTH_SHORT).show();
		//TODO Make so that the new adapter is a adapter to handle click events
	}

}
