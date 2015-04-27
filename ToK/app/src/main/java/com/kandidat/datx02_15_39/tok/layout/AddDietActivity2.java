package com.kandidat.datx02_15_39.tok.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietDiary;

public class AddDietActivity2 extends CustomActionBarActivity {

	DietFragment currentFragement;
	public static final String dietActivityArgument = "DIETACTIVITY";
	DietDiary myDiary = DietDiary.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_diet_activity2);
		initMenu(R.layout.activity_add_diet_activity2);
		currentFragement = new AddDietFragment();
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame, currentFragement).commit();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(currentFragement instanceof AddDietFragment) {
			getMenuInflater().inflate(R.menu.menu_with_moveforward, menu);
		}else{
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
			DietActivity dietActivity = currentFragement.getDietActivity();
			currentFragement = new ViewAddDietFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable(this.dietActivityArgument, dietActivity);
			currentFragement.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.content_frame, currentFragement)
					.addToBackStack(null)
					.commit();
			invalidateOptionsMenu();
		}else if (id == R.id.right_corner_button_confirm) {
			//TODO Fixa så den sparar allt och skickar vidare
			if(currentFragement instanceof ViewAddDietFragment){
				DietActivity tmp =	((ViewAddDietFragment)currentFragement).createDietActivity();
				if(!tmp.getFoodList().isEmpty()) {
					myDiary.addActivity(tmp.getDate(), tmp);
				}
				startActivity(new Intent(this, MainActivity.class));
				finish();
			}
		}
		//This will be called to be able to see if you pressed the menu
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		if(currentFragement instanceof ViewAddDietFragment){
			DietActivity dietActivity = currentFragement.getDietActivity();
			currentFragement = new AddDietFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable(this.dietActivityArgument, dietActivity);
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
