package com.kandidat.datx02_15_39.tok.layout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietDiary;
import com.kandidat.datx02_15_39.tok.model.diet.Recipe;
import com.kandidat.datx02_15_39.tok.utility.Utils;

import java.util.Calendar;


/**
 * Activity to handle input to the diet diary and create Diet activity.
 * This activity will change fragments to make it easier to keep the food item added before
 * adding them to the diary. You will also be able to change and modify recipes.
 */
public class AddDietActivity extends CustomActionBarActivity {

	Fragment currentFragement, earlierFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_diet);
		initMenu();
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x80FF6F00));
		currentFragement = new AddDietFragment();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, currentFragement).commit();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//This decides what should be displayed in the right upper corner
		if(currentFragement instanceof AddDietFragment||
				currentFragement instanceof RecipeViewFragment) {
			getMenuInflater().inflate(R.menu.menu_with_moveforward, menu);
		}else if (currentFragement instanceof  ViewAddDietFragment ||
				currentFragement instanceof EditRecipeFragment ){
			getMenuInflater().inflate(R.menu.menu_with_confirm, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//This is called when we press buttons on Actionbar
		//And the following if statements decides what do when i press the right corner button
		//depending on which frament that is activated
		if (id == R.id.right_corner_button_moveforward) {
			//Gör så att currentFragment kan returnera en DietActivity
			if(currentFragement instanceof AddDietFragment) {
				DietActivity dietActivity = ((DietFragment) currentFragement).getDietActivity();
				currentFragement = new ViewAddDietFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(Utils.dietActivityArgument, dietActivity);
				currentFragement.setArguments(bundle);
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, currentFragement)
						.commit();
			}else if(currentFragement instanceof RecipeViewFragment){
				DietActivity dietActivity = ((RecipeViewFragment) currentFragement).getDietActivity();
				currentFragement = new ViewAddDietFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(Utils.dietActivityArgument, dietActivity);
				currentFragement.setArguments(bundle);
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, currentFragement)
						.commit();
			}
		}else if (id == R.id.right_corner_button_confirm) {
			if(currentFragement instanceof ViewAddDietFragment){
				if(((Switch)findViewById(R.id.save_meal_created_as_recipe)).isChecked()){
					((ViewAddDietFragment)currentFragement).createDietActivity();
					DietActivity dietActivity = ((DietFragment) currentFragement).getDietActivity();
					Recipe recipe = new Recipe(dietActivity.getFoodList(), 1);
					currentFragement = new EditRecipeFragment();
					Bundle bundle = new Bundle();
					bundle.putSerializable(Utils.recipeArgument, recipe);
					currentFragement.setArguments(bundle);
					getSupportFragmentManager().beginTransaction()
							.replace(R.id.content_frame, currentFragement)
							.commit();
					invalidateOptionsMenu();
				}else {
					((ViewAddDietFragment) currentFragement).createDietActivity();
					startNewActivity(MainActivity.class);
				}
			}else if(currentFragement instanceof EditRecipeFragment){
				((EditRecipeFragment) currentFragement).saveRecipe();
				startNewActivity(MainActivity.class);
			}
		}
		//This will be called to be able to see if you pressed the menu
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		changeFragment(fragment);
	}

	/*
	Help method to keep track of the current fragment that is showing.
	Even if we change it from one of the fragment
	 */
	private void changeFragment(Fragment fragment){
		if(!(fragment instanceof AddDietToFragment)) {
			if(currentFragement instanceof AddDietFragment
					|| currentFragement instanceof ViewAddDietFragment){
				earlierFragment = currentFragement;
			}
			currentFragement = fragment;
			invalidateOptionsMenu();
		}
	}

	@Override
	public void onBackPressed() {
		//Method to be able to jump between some of the fragments under the activity
		// that is used when we add food from our databas or recipe.
		if(currentFragement instanceof ViewAddDietFragment ||
				(currentFragement instanceof EditRecipeFragment && earlierFragment instanceof AddDietFragment)
				){
			DietActivity dietActivity = ((DietFragment) currentFragement).getDietActivity();
			currentFragement = new AddDietFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable(Utils.dietActivityArgument, dietActivity);
			currentFragement.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, currentFragement)
					.commit();
		}else if (currentFragement instanceof ViewAddDietFragment) {
			if(earlierFragment instanceof AddDietFragment){
				DietActivity dietActivity = ((DietFragment) currentFragement).getDietActivity();
				currentFragement = new AddDietFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(Utils.dietActivityArgument, dietActivity);
				currentFragement.setArguments(bundle);
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, currentFragement)
						.commit();
			}else{
				super.onBackPressed();
			}
		}else{
			super.onBackPressed();
		}
	}
}
