package com.kandidat.datx02_15_39.tok.layout;


import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.kandidat.datx02_15_39.tok.R;

import java.util.ArrayList;

public class DietHomeActivity extends CustomActionBarActivity {

    RadioButton dayRadioButton;
    RadioButton weekRadioButton;
    Button dateButton;

    ArrayList<String> days;
    ArrayList<String> weeks;

    private int dayIndex = 1;
    private int weekIndex = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_home);
		initMenu(R.layout.activity_diet_home);
        dayRadioButton = (RadioButton) findViewById(R.id.day_radioButton);
        weekRadioButton = (RadioButton) findViewById(R.id.week_radiobutton);
        dateButton = (Button) findViewById(R.id.dateButton);

        days = new ArrayList<String>(){ {
            add("Igår");
            add("Idag");
            add("Imorgon");
        }};

        weeks = new ArrayList<String>(){ {
            add("Förra veckan");
            add("Denna vecka");
            add("Nästa vecka");
        }};


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diet_home, menu);
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

    public void onLeftButtonClick(View view) {


        if (dayRadioButton.isChecked()) {
            if (dayIndex > 0) {
                dayIndex--;
            }
            dateButton.setText(days.get(dayIndex));
        }

        else if (weekRadioButton.isChecked()) {
            if (weekIndex > 0) {
                weekIndex--;
            }
            dateButton.setText(weeks.get(weekIndex));
        }
    }

    public void onRightButtonClick(View view) {

        if (dayRadioButton.isChecked()) {
            if (dayIndex < days.size() - 1) {
                dayIndex++;
            }
            dateButton.setText(days.get(dayIndex));

        }

        else if (weekRadioButton.isChecked()) {
            if (weekIndex < weeks.size() - 1 ) {
                weekIndex++;
            }
            dateButton.setText(weeks.get(weekIndex));
        }
    }


    public void onDayViewClick(View view) {
        dateButton.setText("Idag");
    }

    public void onWeekViewClick(View view) {
        dateButton.setText("Denna vecka");
    }
}
