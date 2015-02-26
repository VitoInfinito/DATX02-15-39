package com.kandidat.datx02_15_39.tok.layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.sleep.drawDiagram;
import com.kandidat.datx02_15_39.tok.model.sleep.Sleep;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepActivity;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepDiary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class SleepHomeActivity extends ActionBarActivity {
    private SleepDiary diary;
    private GregorianCalendar currentCalendar;
    private Date activeDate;
    private PointsGraphSeries<DataPoint> series;

    private SimpleDateFormat sdfShowDay = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdfShowTime = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdfShowHour = new SimpleDateFormat("HH");
    private SimpleDateFormat sdfShowMinutes = new SimpleDateFormat("mm");
    private SimpleDateFormat sdfShowFullTime = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        diary = (SleepDiary) SleepDiary.getInstance();

        //TODO change to not account for specific times i.e seconds and minutes
        Calendar cal = Calendar.getInstance();
        currentCalendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        activeDate = currentCalendar.getTime();
        //Used for testing
        produceFakeData();



        setContentView(R.layout.activity_sleep_home);

        //View viewGraph = findViewById(R.id.imageViewGraph);
        //DrawDiagram graphDiagram = new DrawDiagram(viewGraph.getContext());


        GraphView graph = (GraphView) findViewById(R.id.graph);

        series = new PointsGraphSeries<DataPoint>(fetchDataPoints(activeDate));



        graph.addSeries(series);
        //series.setColor(Color.BLUE);
        series.setTitle(sdfShowDay.format(activeDate));
        setGraphXBounds(activeDate, graph);
       // series.setSpacing(0);

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
                    int hours = (int) value;
                    int minutes = (int) ((value - hours)*100);
                    if(minutes > 60) {
                        minutes -= 60;
                        hours++;
                    }

                    return super.formatLabel(hours, isValueX) + ":" + (minutes < 10 ? '0' + minutes : minutes);
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        series.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y) {
                //Sleep drawSleep = ((SleepActivity) diary.getActivityFromDate(activeDate)).getSleepThatStarts(earlierDate);
                //System.out.println("DrawSleep at " + x + " starts at time " + drawSleep);

                paint.setStrokeWidth(10);
                canvas.drawLine(x-20, y-20, x+20, y+20, paint);
                canvas.drawLine(x+20, y-20, x-20, y+20, paint);
            }
        });


	    fillListWithDummyData();
       /* // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                //return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
                if(data.getY() == 3) {
                    return Color.rgb(255, 0, 0);
                }else if(data.getY() == 2) {
                    return Color.rgb(0, 255, 0);
                }

                return Color.rgb(0, 0, 255);
            }
        });*/


  /*      StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setVerticalLabels(new String[] {"V", "L", "D"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

*/

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Series1: On Data Point clicked: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sleep_home, menu);
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
     * Navigates to the add sleep activity.
     *
     * @param view Not used.
     */
    public void addSleepButtonOnClick(View view){
        startActivity(new Intent(this, AddSleepActivity.class));
    }

    /**
     * Navigates back to the main activity.
     *
     * @param view Not used.
     */
    public void backButtonOnClick(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Updates the graph with and earlier sleep date
     * @param view
     */
    public void gotoEarlierSleepDate(View view){
        updateGraphSeries(-1);
    }

    /**
     * Updates the graph with and later sleep date
     * @param view
     */
    public void gotoLaterSleepDate(View view){
        updateGraphSeries(1);
    }

    private void updateGraphSeries(int offset) {
        currentCalendar.set(Calendar.DAY_OF_MONTH, currentCalendar.get(Calendar.DAY_OF_MONTH) + offset);
        Date newDate = currentCalendar.getTime();

        series.resetData(fetchDataPoints(newDate));

        setGraphXBounds(newDate, (GraphView) findViewById(R.id.graph));

        String newDateSDFDay = sdfShowDay.format(newDate);
        if(newDateSDFDay.equals(sdfShowDay.format(Calendar.getInstance().getTime()))) {
            ((TextView) findViewById(R.id.textDay)).setText("Today");
        }else {
            ((TextView) findViewById(R.id.textDay)).setText(newDateSDFDay);
        }
    }



    public Context getActivity() {
        return this;
    }



    private DataPoint[] fetchDataPoints(Date date) {
        SleepActivity activity = (SleepActivity) diary.getActivityFromDate(date);
        if(activity != null) {
            Sleep sleep = activity.getSleep();
            Date startTime = sleep.getStartTime();
            Date stopTime = sleep.getStopTime();
            Date time = new Date(stopTime.getTime() - startTime.getTime());

            //Integer.parseInt(sdfShowHour.format(startTime))-1
            double merfelherder = Double.parseDouble(sdfShowMinutes.format(startTime)) / 100;
            merfelherder += Double.parseDouble(sdfShowHour.format(startTime));

            double herfelmerder = Double.parseDouble(sdfShowMinutes.format(stopTime)) / 100;
            herfelmerder += Double.parseDouble(sdfShowHour.format(stopTime));

            double pertedelherfer = Double.parseDouble(sdfShowMinutes.format(time)) / 100;
            pertedelherfer += Double.parseDouble(sdfShowHour.format(time));


            //Still purely for testing
            return new DataPoint[]{
                    new DataPoint(merfelherder - 1, 0),
                    new DataPoint(merfelherder, 3),
                    new DataPoint(herfelmerder, 2),
                    new DataPoint(herfelmerder + 5, 0)};
        }

        //If activity was not found we return an empty list.
        return new DataPoint[]{
                new DataPoint(0,0)
        };
    }

    private void setGraphXBounds(Date date, GraphView graph) {
        SleepActivity activity = (SleepActivity) diary.getActivityFromDate(date);
        if(activity != null) {
            Sleep sleep = activity.getSleep();
            Date startTime = sleep.getStartTime();
            Date stopTime = sleep.getStopTime();

            //Integer.parseInt(sdfShowHour.format(startTime))-1
            double merfelherder = Double.parseDouble(sdfShowMinutes.format(startTime))/100;
            merfelherder += Double.parseDouble(sdfShowHour.format(startTime));

            double herfelmerder = Double.parseDouble(sdfShowMinutes.format(stopTime))/100;
            herfelmerder += Double.parseDouble(sdfShowHour.format(stopTime));


            // set manual X bounds
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(merfelherder-1);
            graph.getViewport().setMaxX(herfelmerder+1);
        }
    }

	private void fillListWithDummyData(){
		ListView lv = (ListView) findViewById(R.id.sleepFeed);

		List<IDiaryActivity> acts = diary.showDaysActivities(Calendar.getInstance());
		List<String> sleepList = new ArrayList<String>();

		for(int i=0; i<acts.size(); i++) {
			SleepActivity CIDA = (SleepActivity) acts.get(i);
			sleepList.add("Started " + sdfShowFullTime.format(CIDA.getSleep().getStartTime()) + " and stopped " + sdfShowFullTime.format(CIDA.getSleep().getStopTime()));
		}


		// Instanciating an array list
		/*List<String> sleepList = new ArrayList<String>();
		sleepList.add("Badger");
		sleepList.add("Badger");
		sleepList.add("Badger");
		sleepList.add("Badger");
		sleepList.add("Mushroom");
		sleepList.add("Mushroom");
		sleepList.add("Sn-a-a-a-a-ke");*/

		// This is the array adapter, it takes the context of the activity as a
		// first parameter, the type of list view as a second parameter and your
		// array as a third parameter.
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
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

            diary.addActivity(todaysDate,
                    new SleepActivity("id1",
                            new Sleep(
                                    new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 7, cal.get(Calendar.MINUTE)).getTime(),
                                    new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 3, cal.get(Calendar.MINUTE)).getTime()
                            )
                    )
            );

            diary.addActivity(yesterdaysDate,
                    new SleepActivity("id2",
                            new Sleep(
                                    new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 1, cal.get(Calendar.HOUR_OF_DAY) - 6, cal.get(Calendar.MINUTE)).getTime(),
                                    new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 1, cal.get(Calendar.HOUR_OF_DAY) - 2, cal.get(Calendar.MINUTE)).getTime()
                            )
                    )
            );

            diary.addActivity(tomorrowsDate,
                    new SleepActivity("id3",
                            new Sleep(
                                    new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, cal.get(Calendar.HOUR_OF_DAY) - 5, cal.get(Calendar.MINUTE)).getTime(),
                                    new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + 1, cal.get(Calendar.HOUR_OF_DAY) + 3, cal.get(Calendar.MINUTE)).getTime()
                            )
                    )
            );
        }
    }
}


