package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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


public class AddWorkoutActivity extends CustomActionBarActivity {

    ImageButton yogaButton;
    ImageButton runnerButton;
    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
    private Calendar cal = Calendar.getInstance();

    private SimpleDateFormat sdfShowDate = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfShowTime = new SimpleDateFormat("HH:mm");

    private Date startDate;
    private int startHours;
    private int startMinutes;

    private Date stopDate;
    private int stopHours;
    private int stopMinutes;

    private int intensity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
		initMenu(R.layout.activity_add_workout);
        yogaButton = (ImageButton) findViewById(R.id.yoga_button);
        runnerButton = (ImageButton) findViewById(R.id.sprint_button);

        Calendar currentCalendar = Calendar.getInstance();



        startHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        startMinutes = Calendar.getInstance().get(Calendar.MINUTE);

        currentCalendar.set(Calendar.HOUR_OF_DAY, startHours);
        currentCalendar.set(Calendar.MINUTE, startMinutes);
        startDate = currentCalendar.getTime();

        stopHours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1;
        stopMinutes = Calendar.getInstance().get(Calendar.MINUTE);

        currentCalendar.set(Calendar.HOUR_OF_DAY, stopHours);
        currentCalendar.set(Calendar.MINUTE, stopMinutes);
        stopDate = currentCalendar.getTime();

        intensity = 0;


    }
    public void registerWorkoutIntensityOnClick(final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("Välj intensitet");
        builder.setIcon(R.drawable.yoga);
        final NumberPicker intensityPicker = new NumberPicker(this);

        intensityPicker.setMinValue(1);
        intensityPicker.setMaxValue(10);
        intensityPicker.setValue(5);
        intensityPicker.setWrapSelectorWheel(false);

        builder.setView(intensityPicker);
        builder.setPositiveButton("Nästa", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                intensity = intensityPicker.getValue();
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA" + intensity);
                registerWorkoutStartTimeOnClick(view);
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


    public void registerWorkoutStartTimeOnClick(final View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Välj starttid:");
        builder.setIcon(R.drawable.yoga);

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
                registerWorkoutIntensityOnClick(view);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
   public void registerWorkoutEndTimeOnClick(final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Välj sluttid:");
        builder.setIcon(R.drawable.yoga);

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


        currentCalendar.set(Calendar.HOUR_OF_DAY, stopHours);
        currentCalendar.set(Calendar.MINUTE, stopMinutes);
        stopDate = currentCalendar.getTime();
    }


    /**
     * Add new workout in the WorkoutHomeActivity list.
     */

    public void addNewWorkout(){
        updateDate();
        System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBB" + intensity);
        Workout workout = new Workout(startDate, stopDate, intensity);
        WorkoutActivity workoutActivity = new WorkoutActivity("WORKOUT", workout);
        WorkoutDiary workoutDiary = (WorkoutDiary) WorkoutDiary.getInstance();
        workoutDiary.addActivity(startDate, workoutActivity);

        startActivity(new Intent(this, WorkoutHomeActivity.class));
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
    public void addButtonOnClick(View view){
        startActivity(new Intent(this, WorkoutHomeActivity.class));
    }


}
