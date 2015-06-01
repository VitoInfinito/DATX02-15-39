package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.account.Account;
import com.kandidat.datx02_15_39.tok.utility.JawboneUtils;
import com.kandidat.datx02_15_39.tok.utility.Utils;

/**
 * Main activity class for users home screen
 */
public class MainActivity extends CustomActionBarActivity {
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
		initMenu();


        //Fetching and/or setting up account name from saved preferences
        account = Account.getInstance();
        String accountName = account.getName();
        if(accountName != null) {
            ((TextView) findViewById(R.id.homeUsername)).setText(accountName);
        }else {
            SharedPreferences settings = getSharedPreferences(Utils.ACCOUNT_PREFS, 0);
            accountName = settings.getString("accountName", null);

            //Checking if name was a saved preference
            if(accountName != null) {
                Account.getInstance().setName(accountName);
                ((TextView) findViewById(R.id.homeUsername)).setText(accountName);
            }else {
                startNewActivity( CreateUserActivity.class);
            }
        }

        //Not used in this stage. Is to be implemented later if needed
        //checkFormerConnection();
    }

    private void checkFormerConnection() {
        JawboneUtils.checkConnectionToUP(this, this.getIntent());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_with_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.right_corner_button_add) {
			View add = getLayoutInflater().inflate(R.layout.add_all, null);
			AlertDialog ad = new AlertDialog.Builder(this, R.style.CustomDialog).create();
//            AlertDialog ad = new AlertDialog.Builder(this).create();
			ad.setView(add);
			ad.setCanceledOnTouchOutside(true);
			ad.show();
        }

        return super.onOptionsItemSelected(item);
    }


	/**
	 * Navigates to diet activity overview.
	 * @param view Not used.
	 */
    public void onDietButtonClick(View view) {
        startNewActivity( DietHomeActivity.class);
    }

    /**
     * Navigates to sleep activity overview.
     * @param view Not used.
     */
    public void onSleepButtonClick(View view) {
        startNewActivity( SleepHomeActivity.class);
    }

    /**
     * Navigates to workout activity overview.
     * @param view Not used.
     */
    public void onWorkOutButtonClick(View view){
        startNewActivity( WorkoutHomeActivity.class);
    }


	/**
	 * Navigates to the weight activity
	 * @param view not used
	 */
	public void registerWeightOnClick(View view){
		startNewActivity( WeightHomeActivity.class);
	}

    /**
     * Navigates to the goals activity
     * @param view not used
     */
	public void goalsButtonOnClick(View view){
		startNewActivity( GoalActivity.class);
	}
}