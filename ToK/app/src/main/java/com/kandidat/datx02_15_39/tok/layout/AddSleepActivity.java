package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
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

	ArrayAdapter<String> arrayAdapter;
	List<String> sleepData;
	private LineGraphSeries<DataPoint> series;

	@SuppressWarnings("SimpleDateFormat")
	private SimpleDateFormat sdfShowDate = new SimpleDateFormat("yyyy-MM-dd");
	@SuppressWarnings("SimpleDateFormat")
	private SimpleDateFormat sdfShowTime = new SimpleDateFormat("HH:mm");

	TimePicker startTimePicker;
	private Date startDate;
	private int startHours;
	private int startMinutes;

	private Date stopDate;
	private int stopHours;
	private int stopMinutes;
	TimePicker stopTimePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sleep);
		initMenu(R.layout.activity_add_sleep);

	    Calendar currentCalendar = Calendar.getInstance();

	    stopDate = currentCalendar.getTime();

	    startHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - 7;
	    startMinutes = Calendar.getInstance().get(Calendar.MINUTE);

	    currentCalendar.set(Calendar.HOUR_OF_DAY, startHours);
	    currentCalendar.set(Calendar.MINUTE, startMinutes);
		startDate = currentCalendar.getTime();

	    stopHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
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


	    GraphView graphView = (GraphView) findViewById(R.id.addNewSleepGraph);

	    series = new LineGraphSeries<>(fetchDataPoints());

	    graphView.addSeries(series);

	    //Y Axis bounds
	    graphView.getViewport().setYAxisBoundsManual(true);
	    graphView.getViewport().setMinY(0);
	    graphView.getViewport().setMaxY(4);

	    series.setColor(Color.BLUE);
	    series.setDrawBackground(true);
	    series.setBackgroundColor(Color.BLUE);

	    graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
		    @Override
		    public String formatLabel(double value, boolean isValueX) {
			    if (isValueX) {
				    // transform number to time
				    return sdfShowTime.format(new Date((long) value));
			    } else {
				    return super.formatLabel(value, false);
				    //return super.formatLabel(value, isValueX);
			    }
		    }
	    });

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
		Sleep sleep = new Sleep(startDate, stopDate, 2);
		SleepActivity sleepActivity = new SleepActivity("id4", sleep);
		SleepDiary sleepDiary = (SleepDiary) SleepDiary.getInstance();
		sleepDiary.addActivity(startDate, sleepActivity);
	}

	/**
	 * Creates an alertdialog and gives the user the option to change the date of which the
	 * new sleep should start.
	 *
	 * @param view Not used.
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
	 * @param view Not used, maybe, don't know.
	 */
	public void fromListItemOnClick(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

		builder.setTitle("Började sova");
		builder.setIcon(R.drawable.zzz);

		startTimePicker = new TimePicker(this);
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

				series.resetData(fetchDataPoints());
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
	 * @param view Not used, maybe, don't know.
	 */
	public void toListItemOnClick(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

		builder.setTitle("Slutade sova");
		builder.setIcon(R.drawable.zzz);

		stopTimePicker = new TimePicker(this);
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

				series.resetData(fetchDataPoints());
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
	 * Adds a new sleep and redirects to another activity.
	 *
	 * @param view Not used.
	 */
    public void addButtonOnClick(View view){
		addNewSleep();
        startActivity(new Intent(this, SleepHomeActivity.class));
    }

	/**
	 * Creates and returns datapoints that will be displayed in the graph.
	 *
	 * @return A list of datapoints to be displayed in the graph.
	 */
	private DataPoint[] fetchDataPoints() {
		List<DataPoint> datapoints = new ArrayList<>();

		Calendar tmpCal = Calendar.getInstance();

		tmpCal.setTime(startDate);
		tmpCal.set(Calendar.HOUR_OF_DAY, startHours);
		tmpCal.set(Calendar.MINUTE, startMinutes);

		Date startTime = tmpCal.getTime();
		//System.out.println(startTime);

		tmpCal.setTime(stopDate);
		tmpCal.set(Calendar.HOUR_OF_DAY, stopHours);
		tmpCal.set(Calendar.MINUTE, stopMinutes);

		Date stopTime = tmpCal.getTime();
		//System.out.println(stopTime);

		datapoints.add(new DataPoint(startTime.getTime(), 0));
		datapoints.add(new DataPoint(startTime.getTime(), 2));
		datapoints.add(new DataPoint(stopTime.getTime(), 2));
		datapoints.add(new DataPoint(stopTime.getTime(), 0));

		/*for(int i = 0; i<datapoints.size(); i++){
			System.out.println(datapoints.get(i));
		}
		System.out.println("------------------------------------------------No thanks----------------------------------------------------");*/
		return datapoints.toArray(new DataPoint[]{});
	}
}