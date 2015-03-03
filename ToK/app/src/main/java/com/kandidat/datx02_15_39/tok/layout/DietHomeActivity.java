package com.kandidat.datx02_15_39.tok.layout;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.kandidat.datx02_15_39.tok.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DietHomeActivity extends CustomActionBarActivity {

    RadioButton dayRadioButton;
    RadioButton weekRadioButton;
    Button dateButton;

    private Calendar cal;

    private int dayOffset = 0;
    private int weekOffset = 0;

    private SimpleDateFormat sdfShowFullDate = new SimpleDateFormat("yyyy-MM-dd");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_diet_home);
		initMenu(R.layout.activity_diet_home);

        dayRadioButton = (RadioButton) findViewById(R.id.day_radioButton);
        weekRadioButton = (RadioButton) findViewById(R.id.week_radiobutton);
        dateButton = (Button) findViewById(R.id.dateButton);

        cal = Calendar.getInstance();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diet_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isDayView() {
        return dayRadioButton.isChecked();
    }

    public void onLeftButtonClick(View view) {

        if (isDayView()) {

            dayOffset--;
            cal.add(Calendar.DATE, -1); //Sets the date to one day from the current date
            String chosenDate = sdfShowFullDate.format(cal.getTime()); // sets to yyyy-mm-dd format

            if (dayOffset == -1) {
                dateButton.setText("Igår");
            } else if (dayOffset == 0) {
                dateButton.setText("Idag");
            } else {
                dateButton.setText(chosenDate);
            }

        } else {
            weekOffset--;
            cal.add(Calendar.DATE, -7);

            if (weekOffset == -1) {
                dateButton.setText("Förgående vecka");
            } else if (weekOffset == 0) {
                dateButton.setText("Denna vecka");
            } else {
                dateButton.setText("Vecka " + cal.get(Calendar.WEEK_OF_YEAR));
            }
        }
    }

    public void onRightButtonClick(View view) {

        if (isDayView() && dayOffset != 0) { // makes sure that you can't proceed past today's date

            dayOffset++;
            cal.add(Calendar.DATE, 1);
            String updatedDate = sdfShowFullDate.format(cal.getTime());

            if (dayOffset == -1) {
                dateButton.setText("Igår");
            } else if (dayOffset == 0) {
                dateButton.setText("Idag");
            } else {
                dateButton.setText(updatedDate);
            }

        } else if (!isDayView() && weekOffset != 0) {
            weekOffset++;
            cal.add(Calendar.DATE, 7);

            if (weekOffset == -1) {
                dateButton.setText("Förgående vecka");
            } else if (weekOffset == 0) {
                dateButton.setText("Denna vecka");
            } else {
                dateButton.setText("Vecka " + cal.get(Calendar.WEEK_OF_YEAR));
            }
        }
    }

    public void onDayViewClick(View view) {
        resetDay();
    }

    public void onWeekViewClick(View view) {
        resetWeek();
    }


    public void onDateButtonClick(View view) {
        if (isDayView()) {
            resetDay();
        } else {
            resetWeek();
        }
    }

    private void resetDay() {
        dayOffset = 0;
        cal.setTime(Calendar.getInstance().getTime());
        dateButton.setText("Idag");
    }

    private void resetWeek() {
        weekOffset = 0;
        cal.setTime(Calendar.getInstance().getTime());
        dateButton.setText("Denna vecka");
    }

}
