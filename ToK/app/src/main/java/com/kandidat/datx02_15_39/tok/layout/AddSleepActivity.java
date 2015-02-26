package com.kandidat.datx02_15_39.tok.layout;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.sleep.Sleep;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepActivity;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepDiary;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddSleepActivity extends ActionBarActivity {

	private Sleep newSleep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sleep);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_sleep, menu);
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

	public void addNewSleep(){
		Calendar cal = Calendar.getInstance();

		TimePicker startTimePicker = (TimePicker) findViewById(R.id.startTimePicker);
		TimePicker endTimePicker = (TimePicker) findViewById(R.id.endTimePicker);

		int startHours = startTimePicker.getCurrentHour();
		int startMinutes = startTimePicker.getCurrentMinute();

		int stopHours = endTimePicker.getCurrentHour();
		int stopMinutes = endTimePicker.getCurrentMinute();

		Date startDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), startHours, startMinutes).getTime();
		Date stopDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), stopHours, stopMinutes).getTime();

		Sleep sleep = new Sleep(startDate, stopDate);

		SleepActivity sleepActivity = new SleepActivity("id4", sleep);

		SleepDiary sleepDiary = (SleepDiary) SleepDiary.getInstance();

		sleepDiary.addActivity(startDate, sleepActivity);
	}

    public void addButtonOnClick(View view){
		addNewSleep();
        startActivity(new Intent(this, SleepHomeActivity.class));
    }
}
