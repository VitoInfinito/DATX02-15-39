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


public class AddWorkoutActivity extends CustomActionBarActivity {

    ImageButton yogaButton;
    ImageButton runnerButton;
    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
		initMenu(R.layout.activity_add_workout);
        yogaButton = (ImageButton) findViewById(R.id.yoga_button);
        runnerButton = (ImageButton) findViewById(R.id.sprint_button);

//        yogaButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                 DialogFragment dialogFragment = new DialogFragment();
//                 dialogFragment.show(fm, "Hej hopp!");
//            }
//        });

    }
    public void registerWorkoutOnClick(final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("Välj intensitet");
        builder.setIcon(R.drawable.yoga);
        NumberPicker intensityPicker = new NumberPicker(this);

        intensityPicker.setMinValue(1);
        intensityPicker.setMaxValue(10);
        intensityPicker.setValue(5);
        intensityPicker.setWrapSelectorWheel(false);

        builder.setView(intensityPicker);
        builder.setPositiveButton("Nästa", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                //Spara undan värde för intensitet
                registerWorkoutStartTimeOnClick(view);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void registerWorkoutStartTimeOnClick(final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("Välj starttid:");
        builder.setIcon(R.drawable.yoga);

        TimePicker timePicker = new TimePicker(this);

        timePicker.setIs24HourView(true);

        builder.setView(timePicker);
        builder.setPositiveButton("Nästa", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Spara undan värde för starttid
                //Anropa sluttiden
            }
        });
        builder.setNegativeButton("Tillbaka", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                registerWorkoutOnClick(view);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
   /* public void registerWorkoutEndTimeOnClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("Välj sluttid:");
        builder.setIcon(R.drawable.yoga);
    }*/

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
