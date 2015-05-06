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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.EditDietActivityParams;
import com.kandidat.datx02_15_39.tok.model.diet.Food;
import com.kandidat.datx02_15_39.tok.model.diet.Recipe;
import com.kandidat.datx02_15_39.tok.model.diet.RecipeCollection;
import com.kandidat.datx02_15_39.tok.utilies.SwipeableListAdapter;
import com.kandidat.datx02_15_39.tok.utility.Utils;

import java.util.Calendar;

/**
 * Created by Lagerstedt on 2015-05-05.
 */
public class RecipeViewFragment extends DietFragment{

	private Recipe recipe;

	private RecipeAdapter recipeAdapter;

	private ListView displayRecipe;

	private double amountPortions;

	private FragmentActivity listener;

	public RecipeViewFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_recipe_view, container, false);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (FragmentActivity) activity;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Bundle bundle = getArguments();
		newActivity = new DietActivity(Utils.setupCalendar());
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
		amountPortions = recipe.getNumberOfPortions();
		initButtons();
		updateRecipeList();
	}

	@Override
	protected DietActivity getDietActivity() {
		newActivity.edit( new EditDietActivityParams(null, "", recipe.getMealList(amountPortions), null));
		return super.getDietActivity();
	}

	private void updateRecipeList() {
		displayRecipe = (ListView) getView().findViewById(R.id.food_item_added_container);
		if(displayRecipe.getCount() > 0)
			displayRecipe.removeAllViewsInLayout();
		recipeAdapter = new RecipeAdapter(getView().getContext());
		for (Food f: recipe.getMealList(amountPortions)){
			recipeAdapter.add(f);
		}
		if(displayRecipe != null){
			displayRecipe.setAdapter(recipeAdapter);
		}
	}

	private void initButtons(){
		Button btn = (Button)getView().findViewById(R.id.edit_recipe_amount_button);
		btn.setText(amountPortions + "");
		btn.setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						changeAmount(v);
					}
				});
		getView().findViewById(R.id.btn_change_recipe).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment currentFragement = new EditRecipeFragment();
				listener.onAttachFragment(currentFragement);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Utils.recipeArgument, recipe);
				currentFragement.setArguments(bundle);
				listener.getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, currentFragement)
						.commit();
			}
		});
	}

	private void changeAmount(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
		View view = getLayoutInflater(null).inflate(R.layout.number_picker_layout, null);
		NumberPicker np = (NumberPicker)view.findViewById(R.id.number_picker_amount);
		np.setMinValue(0);
		np.setMaxValue(10000);
		np.setValue((int) recipe.getNumberOfPortions());
		builder.setPositiveButton("Spara",  new ChangedAmountListener(v, np));
		AlertDialog dialog = builder.create();
		dialog.setView(view);
		dialog.setCancelable(true);
		dialog.show();
	}

	private class ChangedAmountListener implements DialogInterface.OnClickListener {

		private NumberPicker np;
		private View v;

		public ChangedAmountListener(View v, NumberPicker np){
			this.np = np;
			this.v = v;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			amountPortions = np.getValue();
			((Button)v).setText((float)np.getValue() + "");
			updateRecipeList();
		}
	}

	/**
	 * This Class is added and extends ArrayAdapter and it lets me draw what i want to the list item
	 */
	private class RecipeAdapter extends ArrayAdapter<Food>{

		public RecipeAdapter(Context context) {
			super(context,0);
		}

		public View getView (int position, View convertView, ViewGroup parent)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_food_item, null);
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
}
