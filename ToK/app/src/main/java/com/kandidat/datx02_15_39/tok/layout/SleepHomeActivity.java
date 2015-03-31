package com.kandidat.datx02_15_39.tok.layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.sleep.Sleep;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepActivity;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepDiary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SleepHomeActivity extends CustomActionBarActivity {

    private static final String TAG = SleepHomeActivity.class.getSimpleName();

    private String mAccessToken;
    private String mClientSecret;


    private SleepDiary diary;
    private GregorianCalendar currentCalendar;

    private LineGraphSeries<DataPoint> lightSleepSeries;
    private LineGraphSeries<DataPoint> deepSleepSeries;
    //For lack of a better way to do this
    private LineGraphSeries<DataPoint> coverSleepSeries;

    private SimpleDateFormat sdfShowDay = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdfShowTime = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdfShowFullTime = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleep_home);
		initMenu(R.layout.activity_sleep_home);
        diary = (SleepDiary) SleepDiary.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            mClientSecret = intent.getStringExtra(UpPlatformSdkConstants.CLIENT_SECRET);
        }

        //TODO change to not account for specific times i.e seconds and minutes
        Calendar cal = Calendar.getInstance();
        currentCalendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        Date activeDate = currentCalendar.getTime();
        //Used for testing
        produceFakeData();
        //View viewGraph = findViewById(R.id.imageViewGraph);
        //DrawDiagram graphDiagram = new DrawDiagram(viewGraph.getContext());


        GraphView graph = (GraphView) findViewById(R.id.graph);
        ((TextView) findViewById(R.id.textDay)).setText("Idag");

        List<DataPoint[]> sleepList = fetchDataPoints(activeDate);
        DataPoint[] lightSleepDps = sleepList.get(0);
        DataPoint[] deepSleepDps = sleepList.get(1);
        DataPoint[] coverSleepDps = sleepList.get(2);

        lightSleepSeries = new LineGraphSeries<>(lightSleepDps);
        deepSleepSeries = new LineGraphSeries<>(deepSleepDps);
        coverSleepSeries = new LineGraphSeries<>(coverSleepDps);

        graph.addSeries(lightSleepSeries);
        graph.addSeries(deepSleepSeries);
        graph.addSeries(coverSleepSeries);
        //lightSleepSeries.setTitle(sdfShowDay.format(activeDate));
        setGraphXBounds(coverSleepDps, graph);



        lightSleepSeries.setColor(Color.rgb(0, 0, 153));
        lightSleepSeries.setDrawBackground(true);
        lightSleepSeries.setBackgroundColor(Color.rgb(0, 0, 153));
        lightSleepSeries.setThickness(0);

        deepSleepSeries.setColor(Color.rgb(204, 82, 0));
        deepSleepSeries.setDrawBackground(true);
        deepSleepSeries.setBackgroundColor(Color.rgb(204, 82, 0));
        deepSleepSeries.setThickness(0);

        coverSleepSeries.setColor(Color.rgb(0, 0, 0));
        coverSleepSeries.setDrawBackground(true);
        coverSleepSeries.setBackgroundColor(Color.rgb(0, 0, 0));
        coverSleepSeries.setThickness(0);


        //Y Axis bounds
        graph.getViewport().setScalable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(2);



        graph.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles clicks on the graph.
             *
             * @param v The view to reference as current.
             */
            @Override
            public void onClick(View v) {

            }
        });

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // transform number to time
                    return sdfShowTime.format(new Date((long) value));
                } else {
                    if(value == 0) {
                        return "V";
                    }else if(value == 1) {
                        return "L";
                    }else if(value == 2) {
                        return "D";
                    }else {
                        return "";
                    }
                }
            }
        });

        //TODO make a better solution than simply making grid white (Either transparent, remove it or make background white as well)
        graph.getGridLabelRenderer().setGridColor(Color.argb(0,255,255,255));


        fillListWithDummyData();

        lightSleepSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Series1: On Data Point clicked: " + sdfShowFullTime.format(new Date((long) dataPoint.getX())), Toast.LENGTH_SHORT).show();
            }
        });
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
            startActivity(new Intent(this, AddSleepActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Updates the graph with and earlier sleep date
     * @param view Not used.
     */
    public void gotoEarlierSleepDate(View view){
        updateGraphSeries(-1);
    }

    /**
     * Updates the graph with and later sleep date
     * @param view Not used.
     */
    public void gotoLaterSleepDate(View view){
        updateGraphSeries(1);
    }

    private void updateGraphSeries(int offset) {
        currentCalendar.set(Calendar.DAY_OF_MONTH, currentCalendar.get(Calendar.DAY_OF_MONTH) + offset);
        Date newDate = currentCalendar.getTime();

        List<DataPoint[]> updatedSleep = fetchDataPoints(newDate);
        lightSleepSeries.resetData(updatedSleep.get(0));
        deepSleepSeries.resetData(updatedSleep.get(1));
        coverSleepSeries.resetData(updatedSleep.get(2));

        setGraphXBounds(updatedSleep.get(2), (GraphView) findViewById(R.id.graph));

        String newDateSDFDay = sdfShowDay.format(newDate);
        if(newDateSDFDay.equals(sdfShowDay.format(Calendar.getInstance().getTime()))) {
            //TODO change setText to use strings.xml
            ((TextView) findViewById(R.id.textDay)).setText("Idag");
        }else {
            ((TextView) findViewById(R.id.textDay)).setText(newDateSDFDay);
        }
    }

    public Context getActivity() {
        return this;
    }

    private List<DataPoint[]> fetchDataPoints(Date date) {
        List<DataPoint> lightSleep = new ArrayList<>();
        List<DataPoint> deepSleep = new ArrayList<>();
        List<DataPoint> coverSleep = new ArrayList<>();
        //SleepActivity activity = (SleepActivity) diary.getActivityFromDate(date);
        //List<IDiaryActivity> activities = diary.getActivitiesFromDate(date);
        List<Sleep> sleepList = diary.getSleepListFromDate(date);
        //if(activity != null) {
        //if(activities != null) {
        if(!sleepList.isEmpty()) {
            //List<Sleep> sleepList = activity.getSleepList();
            //List<Sleep> sleepList = new ArrayList<Sleep>();



            if(sleepList.size() > 0) {
                coverSleep.add(new DataPoint(sleepList.get(0).getStartTime().getTime(), 0));
                coverSleep.add(new DataPoint(sleepList.get(sleepList.size() - 1).getStopTime().getTime(), 0));

                for (int i = 0; i < sleepList.size(); i++) {
                    Sleep sleep = sleepList.get(i);
                    List<DataPoint> addList;
                    if (sleep.getSleepLevel() == 1) {
                        addList = lightSleep;
                    } else {
                        addList = deepSleep;
                    }

                    Date startTime = sleep.getStartTime();
                    Date stopTime = sleep.getStopTime();

                    addList.add(new DataPoint(startTime.getTime(), 0));
                    addList.add(new DataPoint(startTime.getTime(), sleep.getSleepLevel()));
                    addList.add(new DataPoint(stopTime.getTime(), sleep.getSleepLevel()));
                    addList.add(new DataPoint(stopTime.getTime(), 0));

                }
            }
        }

        if(coverSleep.isEmpty() || lightSleep.isEmpty() || deepSleep.isEmpty()) {
            Calendar dpCal = Calendar.getInstance();
            dpCal.setTime(date);

            if (lightSleep.isEmpty()) {
                lightSleep.add(new DataPoint(dpCal.getTime(), -1));
                dpCal.set(Calendar.MINUTE, dpCal.get(Calendar.MINUTE) + 1);
                lightSleep.add(new DataPoint(dpCal.getTime(), -1));
            }

            if (deepSleep.isEmpty()) {
                deepSleep.add(new DataPoint(dpCal.getTime(), -1));
                dpCal.set(Calendar.MINUTE, dpCal.get(Calendar.MINUTE) + 1);
                deepSleep.add(new DataPoint(dpCal.getTime(), -1));
            }

            if (coverSleep.isEmpty()) {
                dpCal.set(Calendar.HOUR_OF_DAY, 0);
                coverSleep.add(new DataPoint(dpCal.getTime(), 0));
                dpCal.set(Calendar.HOUR_OF_DAY, 24);
                coverSleep.add(new DataPoint(dpCal.getTime(), 0));
            }
        }

        List<DataPoint[]> dataPointsList = new ArrayList<>();
        dataPointsList.add(lightSleep.toArray(new DataPoint[]{}));
        dataPointsList.add(deepSleep.toArray(new DataPoint[]{}));
        dataPointsList.add(coverSleep.toArray(new DataPoint[]{}));

        return new ArrayList<>(dataPointsList);

        //If activity was not found we return an empty list.
        /*return new DataPoint[]{
                new DataPoint(0,0)
        };*/
    }

    private void setGraphXBounds(DataPoint[] list, GraphView graph) {
        if(list.length == 2) {
            // set manual X bounds
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(list[0].getX());
            graph.getViewport().setMaxX(list[1].getX());
        }
    }

	private void fillListWithDummyData(){
		ListView lv = (ListView) findViewById(R.id.sleepFeed);

		List<IDiaryActivity> acts = diary.showDaysActivities(Calendar.getInstance());
		List<String> sleepList = new ArrayList<>();

		for(int i=0; i<acts.size(); i++) {
            List<Sleep> list = ((SleepActivity) acts.get(i)).getSleepList();
            for(int j=0; j<list.size(); j++) {
                sleepList.add("Started " + sdfShowFullTime.format(list.get(j).getStartTime()) + " and stopped " + sdfShowFullTime.format(list.get(j).getStopTime()));
            }
		}

		// This is the array adapter, it takes the context of the activity as a
		// first parameter, the type of list view as a second parameter and your
		// array as a third parameter.
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
				this,
				android.R.layout.simple_list_item_1,
				sleepList );

		lv.setAdapter(arrayAdapter);
	}

    /*Temporary for testing*/
    private void produceFakeData() {
        Calendar cal = Calendar.getInstance();
        if(diary.getActivityFromDate(cal.getTime()) == null) {

            Date todaysDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime();
            Date yesterdaysDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 1, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime();
            Date tomorrowsDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime();

            List<Sleep> todaysSleep = (new ArrayList<>());
            todaysSleep.addAll(Arrays.asList(
                    new Sleep(
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 7, cal.get(Calendar.MINUTE)).getTime(),
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 6, cal.get(Calendar.MINUTE)).getTime(),
                            1),
                    new Sleep(
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 6, cal.get(Calendar.MINUTE)).getTime(),
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 3, cal.get(Calendar.MINUTE)).getTime(),
                            2),
                    new Sleep(
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 3, cal.get(Calendar.MINUTE)).getTime(),
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 2, cal.get(Calendar.MINUTE)).getTime(),
                            1),
                    new Sleep(
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 2, cal.get(Calendar.MINUTE)).getTime(),
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime(),
                            2)
                    ));
            diary.addActivity(new SleepActivity("id1", todaysSleep, todaysDate));


            List<Sleep> yesterdaysSleep = (new ArrayList<>());
            yesterdaysSleep.addAll(Arrays.asList(
                    new Sleep(
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 1, cal.get(Calendar.HOUR_OF_DAY) - 6, cal.get(Calendar.MINUTE)).getTime(),
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 1, cal.get(Calendar.HOUR_OF_DAY) - 2, cal.get(Calendar.MINUTE)).getTime(),
                            1),
                    new Sleep(
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 1, cal.get(Calendar.HOUR_OF_DAY) - 2, cal.get(Calendar.MINUTE)).getTime(),
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 1, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime(),
                            1)
                    ));
            diary.addActivity(new SleepActivity("id2", yesterdaysSleep ,yesterdaysDate));

            List<Sleep> tomorrowsSleep = (new ArrayList<>());
            tomorrowsSleep.addAll(Arrays.asList(
                    new Sleep(
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, cal.get(Calendar.HOUR_OF_DAY) - 5, cal.get(Calendar.MINUTE)).getTime(),
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, cal.get(Calendar.HOUR_OF_DAY) - 3, cal.get(Calendar.MINUTE)).getTime(),
                            1),
                    new Sleep(
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, cal.get(Calendar.HOUR_OF_DAY) - 2, cal.get(Calendar.MINUTE)).getTime(),
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, cal.get(Calendar.HOUR_OF_DAY) + 3, cal.get(Calendar.MINUTE)).getTime(),
                            2),
                    new Sleep(
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, cal.get(Calendar.HOUR_OF_DAY) + 4, cal.get(Calendar.MINUTE)).getTime(),
                            new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, cal.get(Calendar.HOUR_OF_DAY) + 5, cal.get(Calendar.MINUTE)).getTime(),
                            2)
                    ));
            diary.addActivity(new SleepActivity("id3", tomorrowsSleep, tomorrowsDate));




        }
    }
}


