package com.kandidat.datx02_15_39.tok.layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.workout.Workout;
import com.kandidat.datx02_15_39.tok.model.workout.WorkoutActivity;
import com.kandidat.datx02_15_39.tok.model.workout.WorkoutDiary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class WorkoutHomeActivity extends CustomActionBarActivity {
    private WorkoutDiary diary;
    private Date todaysDate;
//    private GregorianCalendar calendar;
    private LineGraphSeries<DataPoint> series;
    private ListView workoutList;
    private SearchResultAdapter sra;

    private SimpleDateFormat sdfShowDay = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdfShowTime = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdfShowHour = new SimpleDateFormat("HH");
    private SimpleDateFormat sdfShowFullTime = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workout_home);
		initMenu(R.layout.activity_workout_home);
        todaysDate = new Date();
       // calendar = GregorianCalendar.getInstance();
        diary = (WorkoutDiary) WorkoutDiary.getInstance();

        updateWorkoutList();

        Calendar cal = Calendar.getInstance();
        Date activeDate = cal.getTime();

        Date start = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 1, cal.get(Calendar.MINUTE)).getTime();
        Date end = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime();

        Workout workout = new Workout (start, end, 5.0);
        String id = "01";
        WorkoutActivity activity = new WorkoutActivity(id, workout);
        activity.setDate(workout.getStartTime());
//        diary.addActivity(activeDate, activity);

        Date d1 = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date d2 = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date d3 = cal.getTime();

        GraphView graph = (GraphView) findViewById(R.id.workout_graph);

//         series = new LineGraphSeries<>(fetchDataPoints(activeDate));
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3)
        });
        graph.addSeries(series);

//        graph.getViewport().setYAxisBoundsManual(true);
//        graph.getViewport().setMinY(0);
//        graph.getViewport().setMaxY(10);
//
//        graph.getViewport().setXAxisBoundsManual(true);
//        graph.getViewport().setMinX(0);
//        graph.getViewport().setMaxX(7);
//
        graph.getGridLabelRenderer().setNumVerticalLabels(5);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Kalorier brända");
        graph.getGridLabelRenderer().setNumHorizontalLabels(7);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Vecka " + cal.WEEK_OF_YEAR);

        series.setColor(Color.GREEN);
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
            startActivity(new Intent(this, AddWorkoutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddWorkoutButtonClick(View view){
        startActivity(new Intent(this, AddWorkoutActivity.class));
    }

    private void updateWorkoutList(){
    workoutList = (ListView) findViewById(R.id.show_workout);
    workoutList.removeAllViewsInLayout();

    sra = new SearchResultAdapter(this);
        for(IDiaryActivity w: getListOfActivities()){
            sra.add(w);
        }
        if(workoutList != null){
            workoutList.setAdapter(sra);
        }

    }
    public Context getActivity() {
        return this;
    }

    private List<DataPoint[]> fetchDataPoints(Date date) {

        List <DataPoint> dpList = new ArrayList<DataPoint>();
        List <WorkoutActivity> wList = diary.getWorkoutActivityList();

        List <DataPoint> tmp = new ArrayList<DataPoint>();
        tmp.add(new DataPoint((wList.get(0).getWorkoutList().get(0).getStartTime()),0));
        tmp.add(new DataPoint(wList.get(wList.size() - 1).getWorkoutList().get(wList.size() - 1).getEndTime().getTime(), 0));
        int count = 0;
        List <DataPoint> addList = new ArrayList<DataPoint>();

        for( WorkoutActivity w :wList ){
            WorkoutActivity wa = wList.get(count);
            Date startTime = w.getStartTime();
            Date stopTime = w.getStopTime();

            addList.add(new DataPoint(startTime.getTime(),0));
            addList.add(new DataPoint(stopTime.getTime(),0));
            count++;

        }

        List<DataPoint[]> dataPointsList = new ArrayList<DataPoint[]>();
        dataPointsList.add(addList.toArray(new DataPoint[]{}));

        return new ArrayList<DataPoint[]>(dataPointsList);
    }

    public List<IDiaryActivity> getListOfActivities(){
        Calendar cal = new GregorianCalendar();
        cal.setTime(todaysDate);
        return WorkoutDiary.getInstance().showWeekActivities(cal,cal);
    }

    public class SearchResultAdapter extends ArrayAdapter<IDiaryActivity> {
        public SearchResultAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.show_workout, null);


            }
            TextView workout_type_name = (TextView) convertView.findViewById(R.id.workout_type);

            return convertView;
        }
    }
}
