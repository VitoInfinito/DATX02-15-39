package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.sleep.Sleep;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepActivity;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepDiary;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddSleepActivity extends CustomActionBarActivity {

	private Sleep newSleep;

	private Date startDate;
	private int startHours;
	private int startMinutes;

	private Date stopDate;
	private int stopHours;
	private int stopMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sleep);
		initMenu(R.layout.activity_add_sleep);

	    startHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	    startMinutes = Calendar.getInstance().get(Calendar.MINUTE);

	    Button fromButton = (Button) findViewById(R.id.fromButton);
	    fromButton.setText("From | " + startHours + ":" + startMinutes);

	    stopHours = startHours+1;
	    stopMinutes = startMinutes+20;

	    Button toButton = (Button) findViewById(R.id.toButton);
	    toButton.setText("To | " + stopHours + ":" + stopMinutes);
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
		/*Calendar cal = Calendar.getInstance();

		TimePicker startTimePicker = (TimePicker) findViewById(R.id.startTimePicker);
		TimePicker endTimePicker = (TimePicker) findViewById(R.id.endTimePicker);

		int startHours = startTimePicker.getCurrentHour();
		int startMinutes = startTimePicker.getCurrentMinute();

		int stopHours = endTimePicker.getCurrentHour();
		int stopMinutes = endTimePicker.getCurrentMinute();

		Date startDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), startHours, startMinutes).getTime();
		Date stopDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), stopHours, stopMinutes).getTime();*/

		Sleep sleep = new Sleep(startDate, stopDate);

		SleepActivity sleepActivity = new SleepActivity("id4", sleep);

		SleepDiary sleepDiary = (SleepDiary) SleepDiary.getInstance();

		sleepDiary.addActivity(startDate, sleepActivity);
	}

	public void fromButtonOnClick(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

		builder.setTitle("Började sova");
		builder.setIcon(R.drawable.zzz);

		//NumberPicker weightPicker = new NumberPicker(this);
		TimePicker startTimePicker = new TimePicker(this);

		startTimePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		startTimePicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));

		builder.setView(startTimePicker);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Calendar cal = Calendar.getInstance();
				startDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), startHours, startMinutes).getTime();
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});
		startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
			@Override
			public void onTimeChanged(TimePicker timepicker, int hours, int minutes){
				startHours = hours;
				startMinutes = minutes;

				Button fromButton = (Button) findViewById(R.id.fromButton);
				fromButton.setText("From | " + startHours + ":" + startMinutes);
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}


	public void toButtonOnClick(View view){
		//Date stopDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), stopHours, stopMinutes).getTime();

	}

    public void addButtonOnClick(View view){

		//addNewSleep();
        startActivity(new Intent(this, SleepHomeActivity.class));
    }
}