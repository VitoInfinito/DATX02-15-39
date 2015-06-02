package com.kandidat.datx02_15_39.tok.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.Food;
import com.kandidat.datx02_15_39.tok.model.diet.Recipe;
import com.kandidat.datx02_15_39.tok.model.diet.RecipeCollection;
import com.kandidat.datx02_15_39.tok.utility.SwipeableListAdapter;
import com.kandidat.datx02_15_39.tok.utility.Utils;

import java.util.ArrayList;

/**
 *	Class that is a Fragment that is used when adding food, this is also available to
 *	use when you need to edit a recipe that already exist or is going to be created.
 */
public class EditRecipeFragment extends Fragment {

	private Recipe recipe;

	private RecipeAdapter recipeAdapter;

	private int extendedInfoOpenPosition = -1;

	private ListView displayRecipe;

	private FragmentActivity listener;

	private double amountOfRecipePortions = 1;
	private LinearLayout showIngredienceFrame;
	private Button changeRecipePortion;
	private EditText nameText;

	public EditRecipeFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_edit_recipe, container, false);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (FragmentActivity) activity;
		getActivity().setTitle(getResources().getString(R.string.EditRecipeFragment));
	}


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		if(bundle != null) {
			Object object = bundle.getSerializable(Utils.recipeArgument);
			if (object instanceof Recipe) {
				recipe = (Recipe) object;
			}
		}else{
			recipe = new Recipe(new ArrayList<Food>(), 1);
		}
		if(recipe == null){
			getActivity().finish();
		}
		// This is used if you want to get a popup version of this fragment in any other
		// activity the AddDietActivity
		if(!(getActivity() instanceof AddDietActivity) && getView() != null){
			getView().setBackgroundResource(R.drawable.border_white_background);
		}
		initViews();
		updateList();
	}

	/**
	 * This method is used to init all views on the fragment with listeners,
	 * this is needed because the use of fragments.
	 */
	 private void initViews(){
		 showIngredienceFrame = (LinearLayout) getView().findViewById(R.id.display_amount_value_of_recipe);
		 changeRecipePortion = (Button) getView().findViewById(R.id.portion_button);
		 nameText = (EditText) getView().findViewById(R.id.recipe_name_textview);
		 showIngredienceFrame.setOnClickListener(new View.OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 if(v.getHeight() < Utils.getDpToPixel(getActivity(), 21)){
					 v.setLayoutParams( new LinearLayout.LayoutParams(
							 v.getWidth(),
							 Utils.getDpToPixel(getActivity(), 100)));
				 }else{
					 v.setLayoutParams( new LinearLayout.LayoutParams(
							 v.getWidth(),
							 Utils.getDpToPixel(getActivity(), 20)));
				 }
			 }
		 });
		 changeRecipePortion.setText(amountOfRecipePortions + "");
		 changeRecipePortion.setOnClickListener(
				 new View.OnClickListener() {
					 @Override
					 public void onClick(View v) {
						 changeAmountOfPortions(v);
					 }
				 });
	 }


	/**
	 * Used as listener of when the amount of portions is changed.
	 * @param v - The view that was pressed.
	 */
	private void changeAmountOfPortions(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
		View view = getLayoutInflater(null).inflate(R.layout.number_picker_layout, null);
		NumberPicker np = (NumberPicker)view.findViewById(R.id.number_picker_amount);
		np.setMinValue(0);
		np.setMaxValue(10000);
		np.setValue((int) amountOfRecipePortions);
		builder.setPositiveButton("Spara", new ChangedAmountListener(v, np));
		AlertDialog dialog = builder.create();
		dialog.setView(view);
		dialog.setCancelable(true);
		dialog.show();
	}

	/**
	 * Method to save the recipe to your RecipeBook.
	 */
	public void saveRecipe(){
		if(recipe.getListOfFoodItem().size() > 0) {
			Recipe newRecipe = new Recipe(nameText.getText().toString()
					, recipe.getListOfFoodItem()
					, (int) amountOfRecipePortions);
			RecipeCollection.getInstance().getList().add(newRecipe);
		}
	}

	/**
	 * updates teh list of all the items in the recipe with amount of every single item.
	 */
	private void updateList() {
		displayRecipe = (ListView) getView().findViewById(R.id.display_ingredient_listView);
		if(displayRecipe.getChildCount() > 0) {
			displayRecipe.removeAllViewsInLayout();
			//searchResultList.removeAllViews();
		}
		recipeAdapter = new RecipeAdapter(getView().getContext());
		for (Food f: recipe.getListOfFoodItem()){//TODO
			recipeAdapter.add(f);
		}
		if(displayRecipe != null) {
			displayRecipe.setAdapter(recipeAdapter);
			if(displayRecipe.getFooterViewsCount() == 0){
				View view = LayoutInflater.from(getView().getContext()).inflate(R.layout.add_button_listview, null);
				displayRecipe.addFooterView(view);
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						openFoodSearch();
					}
				});
			}
		}
	}

	/**
	 * Update the focused recipe with Food item.
	 * @param food - the new food item.
	 */
	public void recipeUpdate(Food food){
		recipe.addFood(food);
		updateList();
	}

	/**
	 * Method to open a fragment where you can search and add more Food objects.
	 */
	private void openFoodSearch(){
		AddDietToFragment currentFragement = new AddDietToFragment();
		currentFragement.setFromFargment(this);
		listener.getSupportFragmentManager().beginTransaction()
				.add(R.id.content_frame, currentFragement)
				.commit();
	}


	/**
	 * This Class is added and extends ArrayAdapter and it lets me draw what i want to the list item
	 */
	private class RecipeAdapter extends SwipeableListAdapter<Food>
	{

		public RecipeAdapter(Context context) {
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
			closeExtendedInfo(recipeAdapter.getViewByPosition(extendedInfoOpenPosition, displayRecipe),
					extendedInfoOpenPosition);
			extendedInfoOpenPosition = -1;
		}else {
			int y = position - displayRecipe.getFirstVisiblePosition();
			if(this.extendedInfoOpenPosition > -1
					&& y >= -1
					&& y <= displayRecipe.getChildCount()){
				closeExtendedInfo(recipeAdapter.getViewByPosition(extendedInfoOpenPosition, displayRecipe),
						extendedInfoOpenPosition);
			}
			openExtendedInfo(
					displayRecipe.getChildAt(position - displayRecipe.getFirstVisiblePosition()),
					position);
			this.extendedInfoOpenPosition = position;
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
		recipe.removeFood(position);
		recipeAdapter.remove(recipeAdapter.getItem(position));
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

			food_amount.setText(recipeAdapter.getItem(position).getAmount() +  "");// Amount
			food_prefix.setText(recipeAdapter.getItem(position).getPrefix() +  ""); // Prefix
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
		View view = getActivity().getLayoutInflater().inflate(R.layout.number_picker_layout, null);
		NumberPicker np = (NumberPicker)view.findViewById(R.id.number_picker_amount);
		np.setMinValue(0);
		np.setMaxValue(10000);
		np.setValue((int) recipe.getListOfFoodItem().get(position).getAmount()); // Set The amount of the food item
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

		public ChangedAmountListener(View v, NumberPicker np){
			this(v, np, -1);
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(position == -1 ){
				amountOfRecipePortions = np.getValue();

			}else {
				recipe.getListOfFoodItem().get(position).setAmount(np.getValue());
			}
			((Button) v).setText((float) np.getValue() + "");
		}
	}
}
