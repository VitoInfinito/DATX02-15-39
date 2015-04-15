package com.kandidat.datx02_15_39.tok.layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.workout.CustomListAdapter;
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
    WorkoutDiary diary;
    WorkoutActivity workoutActivity;
    private Workout workout;
    private Date todaysDate;
    private BarGraphSeries<DataPoint> series;
    private int id = R.id.yoga_button;
    private GraphView graph;
    ArrayList <Workout> workoutList;

    ArrayList <WorkoutActivity> workoutActivityList;


    Calendar cal;

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

        cal = Calendar.getInstance();

        Date activeDate = cal.getTime();

        Date start = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY) - 1, cal.get(Calendar.MINUTE)).getTime();
        Date end = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime();

        //adding a workoutactivity in the diary so that the list is not empty

//        workout = new Workout (id, start, end, 2, " ");
//        String id = "01";
//        workoutActivity = new WorkoutActivity(id, workout);
//        workoutActivity.setStopTime(workout.getStartTime());
//        workoutActivity.setStopTime(workout.getEndTime());
//        diary.addActivity(activeDate, workoutActivity);

        graph = (GraphView) findViewById(R.id.workout_graph);
        series = new BarGraphSeries<>();
        fillListWithDummyData();

        updateDayScreen(cal);

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
    Integer [] imgid ={
        R.drawable.yoga_icon,
        R.drawable.sprint,
        R.drawable.soccer,
        R.drawable.strength
    };

    private void fillListWithDummyData(){
        ListView lv = (ListView) findViewById(R.id.show_workout);

        List<IDiaryActivity> acts = diary.showDaysActivities(Calendar.getInstance());
        List<String> workoutList = new ArrayList<String>();

        Integer [] idList = new Integer[6];

        for(int i=0; i<acts.size(); i++) {
            List<Workout> list = ((WorkoutActivity) acts.get(i)).getWorkoutList();
//            workoutList = new String[list.size()];
            for(int j=0; j<list.size(); j++) {
                idList[j] = list.get(j).getId();

                workoutList.add(list.get(j).getWorkoutType() + " Intensitet: " + list.get(j).getIntensity()
                        + "\n" + "Start: " + sdfShowFullTime.format(list.get(j).getStartTime()) +
                        "\n" + "Slut: " + sdfShowFullTime.format(list.get(j).getEndTime()));
            }
        }

        // This is the customlist adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array with images as a third parameter.

            CustomListAdapter adapter = new CustomListAdapter(this, workoutList, imgid);
            lv.setAdapter(adapter);
//        }
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

    private void updateActivityList (ArrayList<WorkoutActivity> workoutActivityList){
        double intensity =0;
//                [] = new double[workoutActivityList.size()];
        for (WorkoutActivity a : workoutActivityList){
            for(int i = 0; i<workoutActivityList.size(); i++){
//                for(int j=0; i<j; j++){
                    intensity += a.getWorkoutList().get(0).getIntensity();
//                }
            }
        }
        if(series!= null){
            series.resetData(new DataPoint[]{
                    new DataPoint(0,0),
                    new DataPoint(10, intensity),
                    new DataPoint(20, intensity+1),
                    new DataPoint(30, intensity+2),
                    new DataPoint(40, intensity+3),
                    new DataPoint(50, 0),
            });
            series.setSpacing(20);

        }

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"M", "T", "O", "T", "F", "L", "S"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.getGridLabelRenderer().setVerticalAxisTitle("Intensitet");
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        series.setColor(Color.DKGRAY);
        graph.addSeries(series);

    }

    private void updateDayScreen(Calendar cal){
        workoutActivityList = (ArrayList) diary.showDaysActivities(cal);
        updateActivityList(workoutActivityList);
    }

    public List<IDiaryActivity> getListOfActivities(){
        Calendar cal = new GregorianCalendar();
        cal.setTime(todaysDate);

        Calendar cal2 = new GregorianCalendar();
        cal2.add(Calendar.DATE, 1);
        return WorkoutDiary.getInstance().showWeekActivities(cal, cal2);
    }

}