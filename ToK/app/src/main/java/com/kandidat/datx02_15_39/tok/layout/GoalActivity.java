package com.kandidat.datx02_15_39.tok.layout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kandidat.datx02_15_39.tok.R;

public class GoalActivity extends CustomActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goal);
		initMenu();

		//Just a wierd thing that I'm trying to fix so I'm writing something super wierd right now.
		//Food sliders and textview
		SeekBar foodSlider = (SeekBar) findViewById(R.id.foodSlider);
		final TextView foodText = (TextView) findViewById(R.id.nbrOfCalsText);
		foodSlider.setMax(140);


		//Workout sliders and textview
		SeekBar workoutSlider = (SeekBar) findViewById(R.id.workoutSlider);
		final TextView workoutText = (TextView) findViewById(R.id.nbrOfCalsBurnedText);
		workoutSlider.setMax(100);

		//Sleep sliders and textview
		SeekBar sleepSlider = (SeekBar) findViewById(R.id.sleepSlider);
		final TextView sleepText = (TextView) findViewById(R.id.nbrOfHoursText);
		sleepSlider.setMax(24);

		foodSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int foodValue = seekBar.getProgress()*100;
				foodText.setText("Äta " + Integer.toString(foodValue) + " kalorier om dagen");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
		workoutSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int workoutValue = seekBar.getProgress()*100;
				workoutText.setText("Bränna " + Integer.toString(workoutValue) + " kalorier om dagen");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
		sleepSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				int sleepValue = seekBar.getProgress();
				sleepText.setText("Sova " + Integer.toString(sleepValue) + " timmar varje natt");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_goal, menu);
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
}
