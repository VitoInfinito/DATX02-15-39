package com.kandidat.datx02_15_39.tok.layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
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
    private WorkoutActivity workoutActivity;
    private Workout workout;
    private Date todaysDate;
    private LineGraphSeries<DataPoint> series;
    private ListView workoutListView;


    private SimpleDateFormat sdfShowDay = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat sdfShowTime = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat sdfShowHour = new SimpleDateFormat("HH");
    private SimpleDateFormat sdfShowFullTime = new SimpleDateFormat("yyyy.MM.dd HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workout_home);
		initMenu(R.layout.activity_workout_home);
        todaysDate = Calendar.getInstance().getTime();
        diary = (WorkoutDiary) WorkoutDiary.getInstance();

        fillListWithDummyData();

        Calendar cal = Calendar.getInstance();
        Date activeDate = cal.getTime();

        Date start = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 1, cal.get(Calendar.MINUTE)).getTime();
        Date end = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime();

        workout = new Workout (start, end, 5, " ");
        String id = "01";
        workoutActivity = new WorkoutActivity(id, workout);
        workoutActivity.setDate(workout.getStartTime());
        diary.addActivity(activeDate, workoutActivity);

        GraphView graph = (GraphView) findViewById(R.id.workout_graph);
//        List<DataPoint[]> workoutList = fetchDataPoints(activeDate);
//        DataPoint [] workoutDataPoints = workoutList.get(0);
//        series = new LineGraphSeries<DataPoint>(workoutDataPoints);
        series = fetchDataPoints(activeDate);
        graph.addSeries(series);

        Log.d("SERIES", series.toString());

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"M", "T", "O", "T", "F", "L", "S"});
        staticLabelsFormatter.setVerticalLabels(new String[] {"låg", "medium", "hög"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        series.setColor(Color.RED);

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

    private void fillListWithDummyData(){
        ListView lv = (ListView) findViewById(R.id.show_workout);

        List<IDiaryActivity> acts = diary.showDaysActivities(Calendar.getInstance());
        List<String> workoutList = new ArrayList<String>();


        for(int i=0; i<acts.size(); i++) {
            List<Workout> list = ((WorkoutActivity) acts.get(i)).getWorkoutList();
            for(int j=0; j<list.size(); j++) {
                workoutList.add(list.get(j).getWorkoutType() + " Intensitet: " + list.get(j).getIntensity()
                        + "\n" + "Start: " + sdfShowFullTime.format(list.get(j).getStartTime()) +
                        "\n" + "Slut: " + sdfShowFullTime.format(list.get(j).getEndTime()));
            }
        }

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                workoutList );

        lv.setAdapter(arrayAdapter);
    }

    /*public void onAddWorkoutButtonClick(View view){
        startActivity(new Intent(this, AddWorkoutActivity.class));
    }*/


   /* private void updateWorkoutList(){
        workoutListView = (ListView) findViewById(R.id.show_workout);
        workoutListView.removeAllViewsInLayout();

        sra = new SearchResultAdapter(this);
            for(IDiaryActivity w: getListOfActivities()){
                sra.add(w);
            }
            if(workoutListView != null){
                workoutListView.setAdapter(sra);
            }

    }*/

    public Context getActivity() {
        return this;
    }

    private LineGraphSeries<DataPoint> fetchDataPoints(Date date) {
        Calendar cal=Calendar.getInstance();
        Calendar pastCal = Calendar.getInstance();
        cal.setTime(date);
        pastCal.setTime(date);
        pastCal.add(Calendar.HOUR, -1);

        Log.d("DATE", date.toString());
        List <DataPoint> wList = new ArrayList<DataPoint>();
        List<Workout> dayList = diary.getWorkoutListFromDate(date);

//        if(wList.size()>0){
//            for(int i = 0; i<wList.size(); i++){
//                Workout workout1 = dayList.get(i);
//                List <DataPoint> addList = new ArrayList<DataPoint>();
//
//                Date startTime = workout1.getStartTime();
//                Date stopTime = workout1.getEndTime();
//
//                addList.add(new DataPoint(startTime.getTime(), 0));
//                addList.add(new DataPoint(startTime.getTime(), workout1.getIntensity()));
//                addList.add(new DataPoint(stopTime.getTime(), workout1.getIntensity()));
//                addList.add(new DataPoint(stopTime.getTime(), 0));
//            }
//
//        }
        Date x = dayList.get(0).getStartTime();
        int y = dayList.get(0).getIntensity();
        LineGraphSeries<DataPoint> returnList = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(1, 2),
                new DataPoint(2, 3)
        });

//        returnList.add(wList.toArray(new DataPoint[]{}));
//        return new ArrayList<DataPoint []>(returnList);
        return returnList;
    }

    public List<IDiaryActivity> getListOfActivities(){
        Calendar cal = new GregorianCalendar();
        cal.setTime(todaysDate);

        Calendar cal2 = new GregorianCalendar();
        cal2.add(Calendar.DATE, 1);
        return WorkoutDiary.getInstance().showWeekActivities(cal, cal2);
    }

}