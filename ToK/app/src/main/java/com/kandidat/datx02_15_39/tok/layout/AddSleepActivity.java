package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.sleep.Sleep;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepActivity;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepDiary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AddSleepActivity extends CustomActionBarActivity {

	private Sleep newSleep;

	ArrayAdapter<String> arrayAdapter;
	List<String> sleepData;

	private SimpleDateFormat sdfShowDate = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfShowTime = new SimpleDateFormat("HH:mm");

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

	    Calendar currentCalendar = Calendar.getInstance();

		startDate = currentCalendar.getTime();
	    stopDate = currentCalendar.getTime();

	    startHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	    startMinutes = Calendar.getInstance().get(Calendar.MINUTE);

	    stopHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1;
	    stopMinutes = Calendar.getInstance().get(Calendar.MINUTE);

	    ListView listview = (ListView) findViewById(R.id.sleepProperties);
	    sleepData = new ArrayList<>();


	    sleepData.add("Datum: " + sdfShowDate.format(startDate));
	    sleepData.add("Start: " + sdfShowTime.format(startDate));
	    sleepData.add("Slut: " + sdfShowTime.format(stopDate));

	    arrayAdapter = new ArrayAdapter<>(
			    this,
			    android.R.layout.simple_list_item_1,
			    sleepData );

	    listview.setAdapter(arrayAdapter);

	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			    switch(position){
				    case 0:
					    dateListItemOnClick(view);
				        break;
				    case 1:
					    fromListItemOnClick(view);
					    break;
				    case 2:
					    toListItemOnClick(view);
					    break;
				    default:
					    break;
			    }
		    }
	    });
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

	/**
	 * Adds a new sleep entry into the diary such that it can be viewed in other activities
	 */
	public void addNewSleep(){
		Sleep sleep = new Sleep(startDate, stopDate);
		SleepActivity sleepActivity = new SleepActivity("id4", sleep);
		SleepDiary sleepDiary = (SleepDiary) SleepDiary.getInstance();
		sleepDiary.addActivity(startDate, sleepActivity);
	}

	/**
	 * Creates an alertdialog and gives the user the option to change the date of which the
	 * new sleep should start.
	 *
	 * @param view
	 */
	private void dateListItemOnClick(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

		builder.setTitle("Datum");
		builder.setIcon(R.drawable.zzz);

		final DatePicker datePicker = new DatePicker(view.getContext());
		datePicker.setSpinnersShown(false);

		builder.setView(datePicker);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Calendar cal = Calendar.getInstance();

				cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

				startDate = cal.getTime();

				ListView listview = (ListView) findViewById(R.id.sleepProperties);
				sleepData.set(0, "Datum: " + sdfShowDate.format(startDate));
				listview.setAdapter(arrayAdapter);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * Creates an alertdialog and gives the user the option to change the time of which the
	 * new sleep should start.
	 *
	 * @param view
	 */
	public void fromListItemOnClick(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

		builder.setTitle("Började sova");
		builder.setIcon(R.drawable.zzz);

		TimePicker startTimePicker = new TimePicker(this);
		startTimePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		startTimePicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));

		startTimePicker.setIs24HourView(true);

		builder.setView(startTimePicker);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Calendar cal = Calendar.getInstance();
				startDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), startHours, startMinutes).getTime();

				ListView listview = (ListView) findViewById(R.id.sleepProperties);
				sleepData.set(1, "Start: " + sdfShowTime.format(startDate));
				listview.setAdapter(arrayAdapter);
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
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * Creates an alertdialog and gives the user the option to change the time of which the
	 * new sleep should stop.
	 *
	 * @param view
	 */
	public void toListItemOnClick(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

		builder.setTitle("Slutade sova");
		builder.setIcon(R.drawable.zzz);

		TimePicker stopTimePicker = new TimePicker(this);
		stopTimePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1);
		stopTimePicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));

		stopTimePicker.setIs24HourView(true);

		builder.setView(stopTimePicker);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Calendar cal = Calendar.getInstance();
				stopDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), stopHours, stopMinutes).getTime();

				ListView listview = (ListView) findViewById(R.id.sleepProperties);
				sleepData.set(2, "Slut: " + sdfShowTime.format(stopDate));
				listview.setAdapter(arrayAdapter);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});
		stopTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
			@Override
			public void onTimeChanged(TimePicker timepicker, int hours, int minutes){
				stopHours = hours;
				stopMinutes = minutes;
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	/**
	 * Adds a new sleep and redirects to another actitity.
	 *
	 * @param view
	 */
    public void addButtonOnClick(View view){
		addNewSleep();
        startActivity(new Intent(this, SleepHomeActivity.class));
    }
}