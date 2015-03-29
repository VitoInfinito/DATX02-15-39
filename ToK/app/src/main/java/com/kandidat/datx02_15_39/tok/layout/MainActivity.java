package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.account.Account;


public class MainActivity extends CustomActionBarActivity {
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
		initMenu(R.layout.activity_main_activity);

        account = Account.getInstance();
        String accountName = account.getName();
        if(accountName != null) {
            ((TextView) findViewById(R.id.homeUsername)).setText(accountName);
        }else {
            startActivity(new Intent(this, CreateUserActivity.class));
        }


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
			View add = getLayoutInflater().inflate(R.layout.activity_add_all, null);
			AlertDialog ad = new AlertDialog.Builder(this, R.style.CustomDialog)
					.create();
			ad.setView(add);
			ad.setCanceledOnTouchOutside(true);
			ad.show();
        }

        return super.onOptionsItemSelected(item);
    }

	/**
	 * Navigates to diet activity overview.
	 *
	 * @param view Not used.
	 */
    public void onDietButtonClick(View view) {
        startActivity(new Intent(this, DietHomeActivity.class));
    }

    /**
     * Navigates to sleep activity overview.
     *
     * @param view Not used.
     */
    public void onSleepButtonClick(View view) {
        startActivity(new Intent(this, SleepHomeActivity.class));
    }

    public void onWorkOutButtonClick(View view){
        startActivity(new Intent(this, WorkoutHomeActivity.class));
    }


	/**
	 * Creates a popup with a numberpicker inside it to register your weight for the day.
	 *
	 * @param view View to get context from for the alert dialog
	 */
	public void registerWeightOnClick(View view){
		/*AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

		builder.setTitle("Weight");
		builder.setIcon(R.drawable.weigth_scale);

		NumberPicker weightPicker = new NumberPicker(this);

		weightPicker.setMinValue(25);
		weightPicker.setMaxValue(200);
		weightPicker.setValue(80);

		builder.setView(weightPicker);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});

		AlertDialog dialog = builder.create();

		dialog.show();*/

		startActivity(new Intent(this, WeightHomeActivity.class));
	}

	public void goalsButtonOnClick(View view){
		startActivity(new Intent(this, GoalActivity.class));
	}
}