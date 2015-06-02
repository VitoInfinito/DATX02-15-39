package com.kandidat.datx02_15_39.tok.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.kandidat.datx02_15_39.tok.utility.SwipeableListAdapter;
import com.kandidat.datx02_15_39.tok.utility.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Fragment to display all the added items and give a name, date, mealtype to the dietactivity
 * and save it as a meal if the user wants to. The user will also be giving the option to save the
 * dietactivity as a recipe in the future.
 */
public class ViewAddDietFragment extends DietFragment{

	private FragmentActivity listener;
	Calendar calendar, today;
	private ListView searchResultList;
	private DisplayAddedAdapter sra;
	private int extendedInfoOpenPosition = -1;
	private DietActivity.MEALTYPE mealType = DietActivity.MEALTYPE.SNACK;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_view_added_diet, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (FragmentActivity) activity;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initButton();
		initCalendar();
		Bundle bundle = getArguments();
		if(bundle != null) {
			Object object = bundle.getSerializable(Utils.dietActivityArgument);
			if (object instanceof DietActivity) {
				newActivity = (DietActivity) object;
				((EditText) getView().findViewById(R.id.mealname)).setText(newActivity.getName());
				calendar = Utils.setupCalendar(newActivity.getDate());
				mealType = newActivity.getMealtype();
				((Button)getView().findViewById(R.id.meal_selector_button))
						.setText(mealType.getString(getView().getContext()));
				setDateOnButton();
			}
		}
		if(newActivity == null){
			newActivity = new DietActivity(today);
		}
		// If the fragment is created from another activity then AddDietActivity the
		// background becomes solid white.
		if(!(getActivity() instanceof AddDietActivity) && getView() != null){
			getView().setBackgroundResource(R.drawable.border_white_background);
			getActivity().setTitle(getResources().getString(R.string.ViewAddDietFragment));
		}
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
		((EditText)getView().findViewById(R.id.mealname)).addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				newActivity.edit(new EditDietActivityParams(null, s.toString(), null,null));
			}
		});
	}

	private void initCalendar(){
		calendar = Utils.setupCalendar();
		today = Utils.setupCalendar();
	}


	/**
	 * Call this methode if you want to edit an Already existing DietActivity that exist!
	 */
	public void editActivity(){
		DietDiary dietDiary = DietDiary.getInstance();
		EditDietActivityParams editDietActivityParams = new EditDietActivityParams(
				calendar != newActivity.getDate()? calendar: null,
				((EditText) getView().findViewById(R.id.mealname)).getText().toString()
				, newActivity.getFoodList(), mealType);
		dietDiary.editActivity(newActivity.getDate(), newActivity.getID(), editDietActivityParams);
	}

	/**
	 * Method to call if you want to create the diet activity and add it to the Diary.
	 */
	public void createDietActivity() {
		newActivity.setName(((EditText) getView().findViewById(R.id.mealname)).getText().toString());
		newActivity.setMealtype(mealType);
		newActivity.setDate(calendar);
		Toast.makeText(getView().getContext(), "name of meal: " + newActivity.getName() + "/" + mealType, Toast.LENGTH_SHORT).show();
		DietDiary.getInstance().addActivity(newActivity.getDate(), newActivity);
	}

	/**
	 * Listener for the datepicker to update view and activity with the new date.
	 */
	private class DatePickerListener implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			Calendar tmp = Utils.setupCalendar();
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

	/**
	 * This method sets the current choosen date to a specific button and to activity
	 * @return - will always return true.
	 */
	private boolean setDateOnButton(){
		Calendar tmp = Utils.setupCalendar();
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

	/**
	* Updates the list that is show so that new items appear
	*/
	private void updateList(){
		searchResultList = (ListView) getView().findViewById(R.id.food_item_added_container);
		if(searchResultList.getChildCount() > 0) {
			searchResultList.removeAllViewsInLayout();
		}
		sra = new DisplayAddedAdapter(getView().getContext());
		for (Food f: newActivity.getFoodList()){
			sra.add(f);
		}
		if(searchResultList != null){
			searchResultList.setAdapter(sra);
		}
	}

	/**
	 * Method to change the meal type of the DietActivity object.
	 * @param item
	 */
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
	private class DisplayAddedAdapter extends SwipeableListAdapter<Food>
	{

		public DisplayAddedAdapter(Context context) {
			super(context);
		}

		public View getView (int position, View convertView, ViewGroup parent)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item_editable, null);
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
		/**
		 * Method to get the exakt position of the element you want, and not update the wrong item.
		 * This is a problem with listview that the size is only the listitems that you can see.
		 * this method fixes this problem.
		 * @param pos
		 * @param listView
		 * @return
		 */
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

	/**
	 * Listener that handles the amount change by the user on a specific item.
	 */
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

	/**
	 * Helper method to change the amount on a specific item in the list.
	 * @param v
	 * @param position
	 */
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

	/**
	 * Listener for the Dialog that is given to the user to decied what amount a specific list
	 * should have.
	 */
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

	/**
	 * Listener to handle deletions
	 */
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

	/**
	 * Method that delete a specific item in the list from input of the user.
	 * @param v
	 * @param position - Deleted items position
	 */
	private void deleteItem(View v, int position) {
		newActivity.getFoodList().remove(position);
		sra.remove(sra.getItem(position));
	}


	/**
	 * Class(Listener) to handle the input to a list view items more information button.
	 */
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

	/**
	 * Helper method to close and open more information about product, this is to be able to change
	 * its amount.
	 * This method also handles that there is not two more information sections open at the same time.
	 * @param v
	 * @param position
	 */
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
	/**
	 * Helper method to open a new extend information with listeners and the right information.
	 * @param v
	 * @param position - which item it is
	 */
	private void openExtendedInfo(View v, int position) {
		LinearLayout extendedView = (LinearLayout)v.findViewById(R.id.extended_food_information);
		if(extendedView.getChildCount() == 0) {
			View convertExtendedView = LayoutInflater.from(v.getContext()).inflate(R.layout.change_amount_on_food_view, null);
			Button food_amount = (Button) convertExtendedView.findViewById(R.id.btn_food_amount);
			TextView food_prefix = (TextView) convertExtendedView.findViewById(R.id.btn_food_prefix);

			food_amount.setText(sra.getItem(position).getAmount() + "");// Amount
			food_prefix.setText(sra.getItem(position).getPrefix() + ""); // Prefix
			food_amount.setOnClickListener(new OnAmountClickListener(position));
			extendedView.addView(convertExtendedView);
		}
	}

	/**
	 * Helper method to close the open section and remove alla the added views and fragments.
	 * @param v
	 * @param position - which item it is
	 */
	private void closeExtendedInfo(View v, int position){
		LinearLayout extendedView = (LinearLayout)v.findViewById(R.id.extended_food_information);
		extendedView.removeAllViews();
		extendedView.removeAllViewsInLayout();
	}

}
