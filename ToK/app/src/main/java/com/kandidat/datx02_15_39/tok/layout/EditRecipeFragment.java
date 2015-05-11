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
import android.widget.Toast;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.Food;
import com.kandidat.datx02_15_39.tok.model.diet.Recipe;
import com.kandidat.datx02_15_39.tok.model.diet.RecipeCollection;
import com.kandidat.datx02_15_39.tok.utilies.SwipeableListAdapter;
import com.kandidat.datx02_15_39.tok.utility.Utils;

/**
 *
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
		}
		if(recipe == null){
			getActivity().finish();
		}
		if(!(getActivity() instanceof AddDietActivity2) && getView() != null){
			getView().setBackgroundResource(R.drawable.border_white_background);
		}
		initViews();
		updateList();
	}
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
					 //TODO lägg till så de visas upp på något snyggt sätt
				 }else{
					 v.setLayoutParams( new LinearLayout.LayoutParams(
							 v.getWidth(),
							 Utils.getDpToPixel(getActivity(), 20)));
					 //TODO lägg till så det bara skrivs typ klicka för mer info
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

	public void saveRecipe(){
		Recipe newRecipe = new Recipe(nameText.getText().toString()
				, recipe.getListOfFoodItem()
				, (int)amountOfRecipePortions);
		RecipeCollection.getInstance().getList().add(newRecipe);
	}

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

		//TODO Fix so you can add more Food
	}

	public void recipeUpdate(Food food){
		recipe.addFood(food);
		updateList();
	}

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
		recipe.removeFood(position);
//		updateList(); Kanske skall användas men inte säker!!
		recipeAdapter.remove(recipeAdapter.getItem(position));
	}

	private void openExtendedInfo(View v, int position) {
		//View view = searchResultList.getChildAt(position - searchResultList.getFirstVisiblePosition());
		LinearLayout extendedView = (LinearLayout)v.findViewById(R.id.extended_food_information);
		if(extendedView.getChildCount() == 0) {
			View convertExtendedView = LayoutInflater.from(v.getContext()).inflate(R.layout.change_amount_on_food_view, null);
			Button food_amount = (Button) convertExtendedView.findViewById(R.id.btn_food_amount);
			Button food_prefix = (Button) convertExtendedView.findViewById(R.id.btn_food_prefix);

			food_amount.setText( "");// Amount
			food_prefix.setText(""); // Prefix
			food_amount.setOnClickListener(new OnAmountClickListener(position));
			food_prefix.setOnClickListener(new OnPrefixClickListener(position));
			extendedView.addView(convertExtendedView);
		}
		Toast.makeText(getView().getContext(), "dragen" + recipeAdapter.getCount(), Toast.LENGTH_SHORT).show();
		//TODO Make so that the new adapter is a adapter to handle click events
	}

	private void closeExtendedInfo(View v, int position){
		LinearLayout extendedView = (LinearLayout)v.findViewById(R.id.extended_food_information);
		extendedView.removeAllViews();
		extendedView.removeAllViewsInLayout();
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


}
