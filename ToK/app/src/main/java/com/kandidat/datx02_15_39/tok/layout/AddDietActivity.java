package com.kandidat.datx02_15_39.tok.layout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.Recipe;
import com.kandidat.datx02_15_39.tok.utility.Utils;

public class AddDietActivity extends CustomActionBarActivity {

	Fragment currentFragement, earlierFragment; //TODO implement this

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_diet);
		initMenu(R.layout.activity_add_diet);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0x80FF6F00));
		currentFragement = new AddDietFragment();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame, currentFragement).commit();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		//noinspection SimplifiableIfStatement
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
						.addToBackStack(null)
						.commit();
				invalidateOptionsMenu();
			}else if(currentFragement instanceof RecipeViewFragment){
				//Gör så att currentFragment kan returnera en DietActivity
				DietActivity dietActivity = ((RecipeViewFragment) currentFragement).getDietActivity();
				currentFragement = new ViewAddDietFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable(Utils.dietActivityArgument, dietActivity);
				currentFragement.setArguments(bundle);
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, currentFragement)
						.addToBackStack(null)
						.commit();
				invalidateOptionsMenu();
			}
		}else if (id == R.id.right_corner_button_confirm) {
			//TODO Fixa så den sparar allt och skickar vidare
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
							.addToBackStack(null)
							.commit();
					invalidateOptionsMenu();
				}else {
					((ViewAddDietFragment) currentFragement).createDietActivity();
					startActivity(new Intent(this, MainActivity.class));
					finish();
				}
			}else if(currentFragement instanceof EditRecipeFragment){
				((EditRecipeFragment) currentFragement).saveRecipe();
				startActivity(new Intent(this, MainActivity.class));
				finish();
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
		if(currentFragement instanceof ViewAddDietFragment){
			DietActivity dietActivity = ((DietFragment) currentFragement).getDietActivity();
			currentFragement = new AddDietFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable(Utils.dietActivityArgument, dietActivity);
			currentFragement.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, currentFragement)
					.commit();
			invalidateOptionsMenu();
		}else{
			super.onBackPressed();
		}
	}
}
