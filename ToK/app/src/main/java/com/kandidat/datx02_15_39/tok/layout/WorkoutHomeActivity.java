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
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.workout.AddWorkoutDialogFragment;
import com.kandidat.datx02_15_39.tok.model.workout.Workout;
import com.kandidat.datx02_15_39.tok.model.workout.WorkoutActivity;
import com.kandidat.datx02_15_39.tok.model.workout.WorkoutDiary;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WorkoutHomeActivity extends CustomActionBarActivity {
    private WorkoutDiary diary;


    private SimpleDateFormat sdfShowDay = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdfShowTime = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdfShowHour = new SimpleDateFormat("HH");
    private SimpleDateFormat sdfShowFullTime = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workout_home);
		initMenu(R.layout.activity_workout_home);

        diary = (WorkoutDiary) WorkoutDiary.getInstance();

        Calendar cal = Calendar.getInstance();
        Date activeDate = cal.getTime();

        Date start = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 1, cal.get(Calendar.MINUTE)).getTime();
        Date end = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime();

        Workout workout = new Workout (start, end, 5.0);
        WorkoutActivity activity = new WorkoutActivity("id", workout);
        diary.addActivity(activeDate, activity);

        GraphView graph = (GraphView) findViewById(R.id.workout_graph);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(fetchDataPoints(activeDate));

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

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workout_home, menu);
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

    public void onAddWorkoutButtonClick(View view){
        startActivity(new Intent(this, AddWorkoutActivity.class));
    }


    public Context getActivity() {
        return this;
    }

    private DataPoint[] fetchDataPoints(Date date) {
        WorkoutActivity activity = (WorkoutActivity) diary.getActivityFromDate(date);
        Workout workout = activity.getWorkout();
        Date startTime = workout.getStartTime();
        Date stopTime = workout.getEndTime();


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
