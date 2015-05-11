package com.kandidat.datx02_15_39.tok.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
    private WorkoutDiary diary;


    private TextView textDay;
    private Date todaysDate;
    private BarGraphSeries<DataPoint> series;
    private GraphView graph;

    private ArrayList <WorkoutActivity> workoutActivityList;
    private int dayOffset = 0;
    private int weekOffset = 0;
    private Calendar cal;

    private boolean WHAT_THE_ACTUAL_FUCK_WERE_YOU_THINKING = true;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdfShowDay = new SimpleDateFormat("dd/MM");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdfShowTime = new SimpleDateFormat("HH:mm");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdfShowFullTime = new SimpleDateFormat("yyyy.MM.dd");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_home);
		initMenu(R.layout.activity_workout_home);

        Calendar tmpCal = Calendar.getInstance();
        Date startDate = tmpCal.getTime();
        tmpCal.set(Calendar.HOUR_OF_DAY, Calendar.HOUR_OF_DAY+2);
        Date stopDate = tmpCal.getTime();

        //Dummy listitem
        Workout workout = new Workout(0, startDate, stopDate, 15, Workout.WORKOUTTYPE_CARDIO);
        WorkoutActivity workoutActivity = new WorkoutActivity("WORKOUT", workout);
        WorkoutDiary workoutDiary = (WorkoutDiary) WorkoutDiary.getInstance();
        workoutDiary.addActivity(startDate, workoutActivity);

        todaysDate = Calendar.getInstance().getTime();
        diary = (WorkoutDiary) WorkoutDiary.getInstance();

        textDay = (TextView) findViewById(R.id.textDay);

        cal = Calendar.getInstance();

        graph = (GraphView) findViewById(R.id.workout_graph);
        series = new BarGraphSeries<>();
        //fillListWithDummyData();
        fillListWithDataFromCalendar(cal);

        updateDayScreen(cal);
        if(WHAT_THE_ACTUAL_FUCK_WERE_YOU_THINKING){
            textDay.setText("Denna vecka");
        }else{
            textDay.setText("Idag");
        }
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

    private void fillListWithDataFromCalendar(Calendar c){
        ListView lv = (ListView) findViewById(R.id.show_workout);
        Log.e("This is the cal", "" + c.getTime());
        List<IDiaryActivity> activityList = diary.showDaysActivities(c);
        List<String> workoutList = new ArrayList<>();

        Integer [] idList = new Integer[6];

        for(int i=0; i<activityList.size(); i++) {
            List<Workout> list = ((WorkoutActivity) activityList.get(i)).getWorkoutList();
            for(int j=0; j<list.size(); j++) {
                idList[j] = list.get(j).getId();
                workoutList.add(list.get(j).getWorkoutType() + " Intensitet: " + list.get(j).getIntensity() + "\n"
                        + sdfShowFullTime.format(list.get(j).getStartTime()) + " "
                        + sdfShowTime.format(list.get(j).getStartTime()) + " - "
                        + sdfShowTime.format(list.get(j).getEndTime()));
            }
        }
        CustomListAdapter adapter = new CustomListAdapter(this, workoutList, imgid);
        lv.setAdapter(adapter);
    }

    //TODO: REMOVE THIS SHIT
    /*private void fillListWithDummyData(){
        ListView lv = (ListView) findViewById(R.id.show_workout);

        List<IDiaryActivity> acts = diary.showDaysActivities(Calendar.getInstance());
        List<String> workoutList = new ArrayList<>();

        Integer [] idList = new Integer[6];
        System.out.println("Size: " + acts.size());

        for(int i=0; i<acts.size(); i++) {
            List<Workout> list = ((WorkoutActivity) acts.get(i)).getWorkoutList();
            for(int j=0; j<list.size(); j++) {
                idList[j] = list.get(j).getId();


                workoutList.add(list.get(j).getWorkoutType() + " Intensitet: " + list.get(j).getIntensity()
                        + "\n" + sdfShowFullTime.format(list.get(j).getStartTime()) + " " + sdfShowTime.format(list.get(j).getStartTime()) + " - " + sdfShowTime.format(list.get(j).getEndTime()));
            }
        }

        // This is the customlist adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array with images as a third parameter.
        /*ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                workoutList );
        lv.setAdapter(arrayAdapter);*/

//            CustomListAdapter adapter = new CustomListAdapter(this, workoutList, imgid);
//            lv.setAdapter(adapter);
//        }
//    }


    public Context getActivity() {
        return this;
    }

    private void updateActivityList (ArrayList<WorkoutActivity> workoutActivityList){
        double intensity =0;
        ArrayList <Workout> workoutArrayList = new ArrayList<>();

        for(int i = 0; i<workoutActivityList.size(); i++) {
            for (int j = 0; j < workoutActivityList.get(i).getWorkoutList().size(); j++) {
                workoutArrayList.add(workoutActivityList.get(i).getWorkoutList().get(j));
            }
        }

        for(Workout workout: workoutArrayList){
            intensity += workout.getIntensity();
        }
//        for (WorkoutActivity a : workoutActivityList){
//            for(int i = 0; i<workoutActivityList.size(); i++){
//                for(int j=0; i<j; j++){
//                    intensity += a.getWorkoutList().get(j).getIntensity();
//                }
//            }
//        }

        if(series!= null){
            series.resetData(new DataPoint[]{
                    new DataPoint(0,0),
                    new DataPoint(10, intensity),
                    new DataPoint(20, intensity),
                    new DataPoint(30, intensity),
                    new DataPoint(40, intensity),

            });
        series.setSpacing(20);

        }

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
//        staticLabelsFormatter.setHorizontalLabels(new String[] {"Antal });
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.getGridLabelRenderer().setVerticalAxisTitle("Intensitet");
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        series.setColor(Color.DKGRAY);
        graph.removeSeries(series);
        graph.addSeries(series);
    }

    private void updateDayScreen(Calendar cal){
        workoutActivityList = (ArrayList) diary.showDaysActivities(cal);
        updateActivityList(workoutActivityList);
    }

//    private void updateWeekScreen(Calendar first, Calendar last) {
//
//        workoutActivityList = (ArrayList) diary.showWeekActivities(first, last);
//
//        updateActivityList(workoutActivityList);
//    }


    //Calculates the start and end date for a given date and print out the diet activities for that interval
    private void updateWeekScreen(Calendar date) {
        Pair<Calendar, Calendar> pairDate = getDateIntervalOfWeek(date);
        workoutActivityList= (ArrayList) diary.showWeekActivities(pairDate.first, pairDate.second);
        updateActivityList(workoutActivityList);
    }

    public List<IDiaryActivity> getListOfActivities(){
        Calendar cal = new GregorianCalendar();
        cal.setTime(todaysDate);

        Calendar cal2 = new GregorianCalendar();
        cal2.add(Calendar.DATE, 1);
        return WorkoutDiary.getInstance().showWeekActivities(cal, cal2);
    }
    private Pair<Calendar, Calendar> getDateIntervalOfWeek(Calendar pairCal) {

        Calendar c = (Calendar) pairCal.clone();
        c.add(Calendar.DATE, 0);//???
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

        Calendar firstDay = (Calendar) c.clone();
        // we do not need the same day a week after, that's why use 6, not 7
        c.add(Calendar.DAY_OF_MONTH, 6);
        Calendar lastDay = (Calendar) c.clone();

        return new Pair<>(firstDay, lastDay);
    }

    public void onNextButtonClick(View view){
        //Only dayview
        if (!WHAT_THE_ACTUAL_FUCK_WERE_YOU_THINKING && dayOffset != 0) { // makes sure that you can't proceed past today's date

            dayOffset++;
            cal.add(Calendar.DATE, 1);
            String updatedDate = sdfShowFullTime.format(cal.getTime());

            if (dayOffset == -1) {
                textDay.setText("Igår");
            } else if (dayOffset == 0) {
                textDay.setText("Idag");
            } else {
                textDay.setText(updatedDate);
            }
            updateDayScreen(cal);
        //Only weekview
        } else if (WHAT_THE_ACTUAL_FUCK_WERE_YOU_THINKING && weekOffset != 0) {
            weekOffset++;
            cal.add(Calendar.DATE, 7);

            if (weekOffset == -1) {
                textDay.setText("Föregående vecka");
            } else if (weekOffset == 0) {
                textDay.setText("Denna vecka");
            } else {
                textDay.setText("Vecka " + cal.get(Calendar.WEEK_OF_YEAR));
            }
            updateWeekScreen(cal);
        }
        fillListWithDataFromCalendar(cal);
    }

    public void onPreviousButtonClick(View view){
        Log.e("Is this weekview?", " - " + WHAT_THE_ACTUAL_FUCK_WERE_YOU_THINKING);
        if (!WHAT_THE_ACTUAL_FUCK_WERE_YOU_THINKING) {
            Log.e("Inside", "I'm fucking inside DAYVIEEEW!");
            dayOffset--;
            Log.e("Dayoffset", "" + dayOffset);
            cal.add(Calendar.DATE, -1); //Sets the date to one day from the current date
            String chosenDate = sdfShowFullTime.format(cal.getTime()); // sets to yyyy-mm-dd format
            Log.e("Date:", chosenDate);

            if (dayOffset == -1) {
                textDay.setText("Igår");
            } else if (dayOffset == 0) {
                textDay.setText("Idag");
            } else {
                textDay.setText(chosenDate);
            }
            updateDayScreen(cal);
            Log.e("Cal", "" + cal.getTime());
            fillListWithDataFromCalendar(cal);

        } else {
            Log.e("Inside", "I'm fucking inside WEEKVIEEEW!");
            weekOffset--;
            cal.add(Calendar.DATE, -7);

            if (weekOffset == -1) {
                textDay.setText("Föregående vecka");
            } else if (weekOffset == 0) {
                textDay.setText("Denna vecka");
            } else {
                textDay.setText("Vecka " + cal.get(Calendar.WEEK_OF_YEAR));
            }
            updateWeekScreen(cal);
        }
    }
    private void resetDay() {
        dayOffset = 0;
        cal.setTime(Calendar.getInstance().getTime());
        textDay.setText("Idag");
    }

    private void resetWeek() {
        weekOffset = 0;
        cal.setTime(Calendar.getInstance().getTime());
        textDay.setText("Denna vecka");
    }

    public void onWeekButtonClick(View view){
        WHAT_THE_ACTUAL_FUCK_WERE_YOU_THINKING = true;
        updateWeekScreen(cal);
        resetWeek();
    }

    public void onDayButtonClick(View view){
        WHAT_THE_ACTUAL_FUCK_WERE_YOU_THINKING = false;
        updateDayScreen(cal);
        resetDay();
    }
}