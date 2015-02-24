package com.kandidat.datx02_15_39.tok.layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.sleep.Sleep;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepActivity;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepDiary;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class SleepHomeActivity extends ActionBarActivity {
    private SleepDiary diary;

    private SimpleDateFormat sdfShowDay = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdfShowTime = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdfShowHour = new SimpleDateFormat("HH");
    private SimpleDateFormat sdfShowFullTime = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        diary = (SleepDiary) SleepDiary.getInstance();
        Calendar cal = Calendar.getInstance();

        //TODO change to not account for specific times i.e seconds and minutes
        Date activeDate = cal.getTime();

        //Temporary for testing
        Date earlierDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 7, cal.get(Calendar.MINUTE)).getTime();
        Date laterDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime();
        Sleep sleep = new Sleep(earlierDate, laterDate);
        SleepActivity activity = new SleepActivity("masterID", sleep);
        diary.addActivity(activeDate, activity);


        setContentView(R.layout.activity_sleep_home);

        GraphView graph = (GraphView) findViewById(R.id.graph);

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(fetchDataPoints(activeDate));

        graph.addSeries(series);
        series.setColor(Color.BLACK);
        series.setTitle(sdfShowDay.format(activeDate));

        graph.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles clicks on the graph.
             *
             * @param v The view to reference as current.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), detailedSleepActivity.class));
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

    public Context getActivity() {
        return this;
    }



    private DataPoint[] fetchDataPoints(Date date) {
        SleepActivity activity = (SleepActivity) diary.getActivityFromDate(date);
        Sleep sleep = activity.getSleep();
        Date startTime = sleep.getStartTime();
        Date stopTime = sleep.getStopTime();


        System.out.println(sdfShowFullTime.format(startTime));
        System.out.println(sdfShowFullTime.format(stopTime));



        //Still purely for testing
        return new DataPoint[] {
                new DataPoint(Integer.parseInt(sdfShowHour.format(startTime))-1, 0),
                new DataPoint(Integer.parseInt(sdfShowHour.format(startTime)), 3),
                new DataPoint(Integer.parseInt(sdfShowHour.format(stopTime)), 3),
                new DataPoint(Integer.parseInt(sdfShowHour.format(stopTime))+1, 0)};
    }
}
