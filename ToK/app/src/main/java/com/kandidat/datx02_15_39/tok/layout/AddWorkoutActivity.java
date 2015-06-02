package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;


import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.workout.Workout;
import com.kandidat.datx02_15_39.tok.model.workout.WorkoutActivity;
import com.kandidat.datx02_15_39.tok.model.workout.WorkoutDiary;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.widget.Toast;

/**
 * Activity class for adding a workout activity
 */
public class AddWorkoutActivity extends CustomActionBarActivity {

    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
    private Calendar cal = Calendar.getInstance();

    private SimpleDateFormat sdfShowDate = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfShowTime = new SimpleDateFormat("HH:mm");

    private Date startDate;

    private int startMonth;
    private int startDay;

    private int startHours;
    private int startMinutes;

    private Date stopDate;
    private int stopHours; 
    private int stopMinutes;


    private int intensity;

    Workout.WorkoutType workoutType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
		initMenu();
        //runnerButton = (ImageButton) findViewById(R.id.sprint_button);

        Calendar currentCalendar = Calendar.getInstance();



        startHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        startMinutes = Calendar.getInstance().get(Calendar.MINUTE);

        /*
        startMonth = Calendar.getInstance().get(Calendar.MONTH);
        startDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);*/

        currentCalendar.set(Calendar.HOUR_OF_DAY, startHours);
        currentCalendar.set(Calendar.MINUTE, startMinutes);
        /*currentCalendar.set(Calendar.MONTH, startMonth);
        currentCalendar.set(Calendar.DAY_OF_MONTH, startDay);*/
        startDate = currentCalendar.getTime();

        stopHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1;
        stopMinutes = Calendar.getInstance().get(Calendar.MINUTE);

        currentCalendar.set(Calendar.HOUR_OF_DAY, stopHours);
        currentCalendar.set(Calendar.MINUTE, stopMinutes);
        stopDate = currentCalendar.getTime();

        intensity = 0;


    }

    /**
     * Handles click on the flex workout-type button
     * @param view Sent as a parameter to register intensity
     */
    public void onClickFlex(final View view){
        this.workoutType = Workout.WorkoutType.FLEX;
        registerWorkoutIntensityOnClick(view);
    }

    /**
     * Handles click on the strength workout-type button
     * @param view Sent as a parameter to register intensity
     */
    public void onClickStrength(final View view){
        this.workoutType = Workout.WorkoutType.STRENGTH;
        registerWorkoutIntensityOnClick(view);
    }

    /**
     * Handles click on the cardio workout-type button
     * @param view Sent as a parameter to register intensity
     */
    public void onClickCardio(final View view){
        this.workoutType = Workout.WorkoutType.CARDIO;
        registerWorkoutIntensityOnClick(view);
    }

    /**
     * Handles click on the sports workout-type button
     * @param view Sent as a parameter to register intensity
     */
    public void onClickSports(final View view){
        this.workoutType = Workout.WorkoutType.SPORT;
        registerWorkoutIntensityOnClick(view);
    }

    /**
     * Creates and opens a dialog frame in which the user can select the intensity level of
     * the new workout activity.
     * @param view Provides the context on which the dialog will be opened.
     */
    public void registerWorkoutIntensityOnClick(final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("Välj intensitet");
        builder.setIcon(R.drawable.heart288);
        final NumberPicker intensityPicker = new NumberPicker(this);

        intensityPicker.setMinValue(1);
        intensityPicker.setMaxValue(10);
        intensityPicker.setValue(5);
        intensityPicker.setWrapSelectorWheel(false);

        builder.setView(intensityPicker);
        builder.setPositiveButton("Nästa", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                intensity = intensityPicker.getValue();
                //TODO remove println when not needed
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA" + intensity);
                registerWorkoutDayOnClick(view);
            }
        });
        builder.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Creates and opens a dialog frame in which the user can select which day
     * the new workout activity took place.
     * @param view Provides the context on which the dialog will be opened.
     */
    public void registerWorkoutDayOnClick(final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());


        builder.setTitle("Välj Dag");
        builder.setIcon(R.drawable.heart288);

        Calendar cal = GregorianCalendar.getInstance();
        startDate = cal.getTime();

        final DatePicker datePicker = new DatePicker(this);
        datePicker.setMaxDate(startDate.getTime());
        datePicker.setCalendarViewShown(false);
        datePicker.setSpinnersShown(true);

        builder.setView(datePicker);
        builder.setPositiveButton("Nästa", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                registerWorkoutStartTimeOnClick(view);
            }
        });
        builder.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                registerWorkoutIntensityOnClick(view);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Creates and opens a dialog frame in which the user can select which time
     * the new workout activity started.
     * @param view Provides the context on which the dialog will be opened.
     */
    public void registerWorkoutStartTimeOnClick(final View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Välj starttid:");
        builder.setIcon(R.drawable.heart288);

        final TimePicker timePicker = new TimePicker(this);

        timePicker.setIs24HourView(true);

        builder.setView(timePicker);
        builder.setPositiveButton("Nästa", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                cal = Calendar.getInstance();
                Date date = cal.getTime();
                startHours = timePicker.getCurrentHour();
                startMinutes = timePicker.getCurrentMinute();
                registerWorkoutEndTimeOnClick(view);
            }
        });
        builder.setNegativeButton("Tillbaka", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                registerWorkoutDayOnClick(view);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Creates and opens a dialog frame in which the user can select which time
     * the new workout activity ended.
     * @param view Provides the context on which the dialog will be opened.
     */
   public void registerWorkoutEndTimeOnClick(final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Välj sluttid:");
        builder.setIcon(R.drawable.heart288);

       final TimePicker timePicker = new TimePicker(this);

       timePicker.setIs24HourView(true);

       builder.setView(timePicker);
       builder.setPositiveButton("Spara träning", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
               stopHours = timePicker.getCurrentHour();
               stopMinutes = timePicker.getCurrentMinute();
               addNewWorkout();
           }
       });
       builder.setNegativeButton("Tillbaka", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int id) {
               registerWorkoutStartTimeOnClick(view);
           }
       });

       AlertDialog dialog = builder.create();
       dialog.show();
    }

    private void updateDate(){
        Calendar currentCalendar = Calendar.getInstance();

        currentCalendar.set(Calendar.HOUR_OF_DAY, startHours);
        currentCalendar.set(Calendar.MINUTE, startMinutes);
        startDate = currentCalendar.getTime();

        Log.e("Date to be added: ", "" + startDate);


        currentCalendar.set(Calendar.HOUR_OF_DAY, stopHours);
        currentCalendar.set(Calendar.MINUTE, stopMinutes);
        stopDate = currentCalendar.getTime();
    }


    /**
     * Add new workout in the WorkoutHomeActivity list.
     */
    public void addNewWorkout(){
        updateDate();
        Workout workout = new Workout(startDate, stopDate, intensity, workoutType);
        WorkoutActivity workoutActivity = new WorkoutActivity("WORKOUT", workout);
        WorkoutDiary workoutDiary = (WorkoutDiary) WorkoutDiary.getInstance();
        workoutDiary.addActivity(startDate, workoutActivity);

        startNewActivity(WorkoutHomeActivity.class);
        Toast.makeText(getApplicationContext(), "Träning sparad", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_workout, menu);
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
     * Redirects to WorkoutHomeActivity on click.
     * @param view Not used.
     */
    public void addButtonOnClick(View view){
        startNewActivity( WorkoutHomeActivity.class);
    }


}
