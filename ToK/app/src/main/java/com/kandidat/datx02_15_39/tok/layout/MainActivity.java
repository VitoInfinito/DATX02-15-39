package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.kandidat.datx02_15_39.tok.R;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hello_world, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

	/**
	 * Navigates to diet activity overview.
	 *
	 * @param view Not used.
	 */
    public void OnDietButtonClick(View view) {
        startActivity(new Intent(this, DietHomeActivity.class));
    }

    /**
     * Navigates to sleep activity overview.
     *
     * @param view Not used.
     */
    public void OnSleepButtonClick(View view) {
        startActivity(new Intent(this, SleepHomeActivity.class));
    }

	/**
	 * Creates a popup with a numberpicker inside it to register your weight for the day.
	 *
	 * @param view View to get context from for the alert dialog
	 */
	public void registerWeightOnClick(View view){
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setTitle("Weight");
		builder.setIcon(R.drawable.weigth_scale);

		NumberPicker weightPicker = new NumberPicker(this);

		weightPicker.setMinValue(25);
		weightPicker.setMaxValue(200);
		weightPicker.setValue(80);

		builder.setView(weightPicker);

		// 3. Add the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User clicked OK button
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});


		// 4. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();

		dialog.show();
	}
}
