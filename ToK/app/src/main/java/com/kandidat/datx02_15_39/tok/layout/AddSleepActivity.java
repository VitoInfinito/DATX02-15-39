package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Activity class for adding a sleep
 */
public class AddSleepActivity extends CustomActionBarActivity {

    private static final String TAG = AddSleepActivity.class.getSimpleName();

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
		initMenu();

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



	    sleepData.add("Start Datum: " + sdfShowDate.format(startDate));
	    sleepData.add("Startade: " + sdfShowTime.format(startDate));
	    sleepData.add("Slutade: " + sdfShowTime.format(stopDate));

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
	    graphView.getViewport().setMaxY(2);
/*
	    series.setColor(Color.BLUE);
	    series.setDrawBackground(true);
	    series.setBackgroundColor(Color.BLUE);*/

        series.setColor(Color.rgb(188, 188, 153));
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.rgb(188, 188, 153));
        series.setThickness(0);

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
        graphView.getGridLabelRenderer().setNumVerticalLabels(0);

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

        //TODO make a better solution than simply making grid white (Either transparent, remove it or make background white as well)
        graphView.getGridLabelRenderer().setGridColor(Color.argb(0,255,255,255));
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
        //TODO just fix plis
		Sleep sleep = new Sleep(startDate, stopDate, Sleep.SleepState.DEEP);
		SleepActivity sleepActivity = new SleepActivity("id4", sleep);
		SleepDiary sleepDiary = (SleepDiary) SleepDiary.getInstance();
		sleepDiary.addActivity(sleepActivity);

        //Code for connecting to and sending the data to UP. Currently not used
        /*Calendar tmpCal = Calendar.getInstance();

        tmpCal.setTime(startDate);
        tmpCal.set(Calendar.HOUR_OF_DAY, startHours);
        tmpCal.set(Calendar.MINUTE, startMinutes);
        int startSeconds = (int) (tmpCal.getTimeInMillis()/1000);

        tmpCal.setTime(stopDate);
        tmpCal.set(Calendar.HOUR_OF_DAY, stopHours);
        tmpCal.set(Calendar.MINUTE, stopMinutes);
        int stopSeconds = (int) (tmpCal.getTimeInMillis()/1000);*/

        /*Log.e(TAG, "making Create Sleep Event api call ...");
        ApiManager.getRestApiInterface().createSleepEvent(
                UpPlatformSdkConstants.API_VERSION_STRING,
                getCreateSleepEventRequestParams(startSeconds, stopSeconds),
                createSleepCallbackListener);*/
	}

    /**
     * Used to create the request query for creating a sleep event with Jawbone
     * @param time_created shows when the sleep was created
     * @param time_completed shows when the sleep was completed
     * @return a HashMap containing the queries for the request to Jawbone
     */
    private static HashMap<String, Object> getCreateSleepEventRequestParams(int time_created, int time_completed) {
        HashMap<String, Object> queryHashMap = new HashMap<String, Object>();
        queryHashMap.put("time_created", time_created);
        queryHashMap.put("time_completed", time_completed);
        return queryHashMap;
    }

    /**
     * Callback function when creating a new sleep event with Jawbone
     */
    private Callback createSleepCallbackListener = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            Log.e(TAG,  "api call successful, json output: " + o.toString());
            Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_LONG).show();
            returnToSleepHome();
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.e(TAG,  "api call failed, error message: " + retrofitError.getMessage());
            Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    /**
     * Method to call when returning to sleep home
     */
    private void returnToSleepHome() {
        startActivity(new Intent(this, SleepHomeActivity.class));
    }

	/**
	 * Creates an alertdialog and gives the user the option to change the date of which the
	 * new sleep should start.
	 *
	 * @param view Not used.
	 */
	private void dateListItemOnClick(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

		builder.setTitle(R.string.date);
		builder.setIcon(R.drawable.zzz);

		final DatePicker datePicker = new DatePicker(view.getContext());
		datePicker.setSpinnersShown(false);

		builder.setView(datePicker);

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Calendar cal = Calendar.getInstance();

				cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

				startDate = cal.getTime();

				ListView listview = (ListView) findViewById(R.id.sleepProperties);
				//sleepData.set(0, R.string.date + sdfShowDate.format(startDate));
                sleepData.set(0, "Start Datum: " + sdfShowDate.format(startDate));
				listview.setAdapter(arrayAdapter);
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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

		builder.setTitle(R.string.sleep_started);
		builder.setIcon(R.drawable.zzz);

		startTimePicker = new TimePicker(this);
		startTimePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		startTimePicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));

		startTimePicker.setIs24HourView(true);

		builder.setView(startTimePicker);

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Calendar cal = Calendar.getInstance();
				startDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), startHours, startMinutes).getTime();

				ListView listview = (ListView) findViewById(R.id.sleepProperties);
				//sleepData.set(1, R.string.sleep_started + sdfShowTime.format(startDate));
                sleepData.set(1, "Startade: " + sdfShowTime.format(startDate));
				listview.setAdapter(arrayAdapter);

				series.resetData(fetchDataPoints());
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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

		builder.setTitle(R.string.sleep_ended);
		builder.setIcon(R.drawable.zzz);

		stopTimePicker = new TimePicker(this);
		stopTimePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1);
		stopTimePicker.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));

		stopTimePicker.setIs24HourView(true);

		builder.setView(stopTimePicker);

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Calendar cal = Calendar.getInstance();
				stopDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), stopHours, stopMinutes).getTime();

				ListView listview = (ListView) findViewById(R.id.sleepProperties);
				//sleepData.set(2, R.string.sleep_ended + sdfShowTime.format(stopDate));
                sleepData.set(2, "Slutade: " + sdfShowTime.format(stopDate));
				listview.setAdapter(arrayAdapter);

				series.resetData(fetchDataPoints());
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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

        if(startHours <= stopHours && startMinutes <= stopMinutes) {
            stopDate = new Date(startDate.getTime());
        }else {
            tmpCal.set(Calendar.DAY_OF_MONTH, tmpCal.get(Calendar.DAY_OF_MONTH) + 1);
            stopDate = tmpCal.getTime();
        }

		tmpCal.set(Calendar.HOUR_OF_DAY, stopHours);
		tmpCal.set(Calendar.MINUTE, stopMinutes);

		Date stopTime = tmpCal.getTime();

		datapoints.add(new DataPoint(startTime.getTime(), 0));
		datapoints.add(new DataPoint(startTime.getTime(), 2));
		datapoints.add(new DataPoint(stopTime.getTime(), 2));
		datapoints.add(new DataPoint(stopTime.getTime(), 0));

		return datapoints.toArray(new DataPoint[]{});
	}
}