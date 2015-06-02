package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietDiary;
import com.kandidat.datx02_15_39.tok.model.diet.Food;
import com.kandidat.datx02_15_39.tok.utility.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DietHomeActivity extends CustomActionBarActivity {

    private Button dayRadioButton;
    private Button weekRadioButton;
    private TextView dateButton;
    private GraphView dietGraph;
    private TextView kcalText, carbText, protText, fatText;
    private Calendar cal;
    private BarGraphSeries<DataPoint> series;

	private ViewAddDietFragment tempFragment;

    ArrayList<DietActivity> activityList;
    private ListView mealList;
    private MealListAdapter mla;
    Calendar tempCal; // used for the test food
    DietDiary myDiary;

    private int dayOffset = 0;
    private int weekOffset = 0;

    private SimpleDateFormat sdfShowFullDate = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_diet_home);
        initMenu();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80FF6F00")));

		//Initial alla buttons and views with listeneres.
        dietGraph = (GraphView) findViewById(R.id.diet_graph);
        series = new BarGraphSeries<>();

        cal = Calendar.getInstance();
        tempCal = Calendar.getInstance();

        dayRadioButton = (Button) findViewById(R.id.day_radioButton);
        weekRadioButton = (Button) findViewById(R.id.week_radiobutton);
        dateButton = (TextView) findViewById(R.id.dateButton);
        kcalText = (TextView) findViewById(R.id.kcal_text_view);
        carbText = (TextView) findViewById(R.id.carb_text_view);
        protText = (TextView) findViewById(R.id.protein_text_view);
        fatText = (TextView) findViewById(R.id.fat_text_view);

        View.OnTouchListener dayAndWeekListener = new View.OnTouchListener() { //makes sure that not both day and week button are pressed simultaneously
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (v == dayRadioButton) {
                    weekRadioButton.setActivated(false);
                    v.setActivated(true);
                    resetDay();

                } else if(v == weekRadioButton){
                    dayRadioButton.setActivated(false);
                    v.setActivated(true);
                    resetWeek();
                }
                return true;
            }
        };

        myDiary = DietDiary.getInstance();

        dayRadioButton.setActivated(true);
        dayRadioButton.setOnTouchListener(dayAndWeekListener);
        weekRadioButton.setOnTouchListener(dayAndWeekListener);

        updateDayScreen(cal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		if(tempFragment != null)
			getMenuInflater().inflate(R.menu.menu_with_confirm, menu);
		else
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
           startNewActivity(AddDietActivity.class);
        }
		if(id == R.id.right_corner_button_confirm){
			tempFragment.editActivity();
			getSupportFragmentManager().beginTransaction().remove(tempFragment).commit();
			tempFragment = null;
			updateMealList();
			invalidateOptionsMenu();
		}

        return super.onOptionsItemSelected(item);
    }

	/**
	 * Class that works as an adapter to a list view, this is needed to make listitems appear with
	 * listener on the items.
	 */
    public class MealListAdapter extends ArrayAdapter<DietActivity>
    {
        public MealListAdapter(Context context)
        {
            super(context,0);
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.meal_item, null);
            }

            String activityId = getItem(position).getID();

            // Fetching the views of the layout
            TextView meal_item_name = (TextView) convertView.findViewById(R.id.meal_item_name);
            TextView meal_item_calories = (TextView) convertView.findViewById(R.id.meal_item_calories);
            TextView meal_item_date = (TextView) convertView.findViewById(R.id.meal_item_date);

            // Populate the data into the template layout (meal_item)
            meal_item_name.setHint(getItem(position).getName());
            meal_item_calories.setHint(getItem(position).getCalorieCount() + "");
            meal_item_date.setHint(sdfShowFullDate.format(getItem(position).getDate().getTime()));


            return convertView;
        }
    }

	/**
	 * updates the meal list with the appropriate meals for given dates
	 */
    private void updateMealList(){
        mealList = (ListView) findViewById(R.id.meal_list_view);
        mealList.removeAllViewsInLayout();
        mla = new MealListAdapter(this);
        for (DietActivity da: activityList){
            mla.add(da);
        }
        if(mealList != null){
            mealList.setAdapter(mla);
        }
        mealList.setOnItemClickListener(new MealItemClickListener());

    }

	/**
	 * Decides what happens on click on meal items in the ListView
	 */
    private class MealItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            editMealItem(position);
        }
    }

	/**
	 * Method  used to make a fragment appear with the possibility to change a existing meal.
	 * @param dietActivity - the activity to change
	 */
	void startNewFragmentToEditMeal(DietActivity dietActivity){
		if(dietActivity != null) {
			tempFragment = new ViewAddDietFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable(Utils.dietActivityArgument, dietActivity);
			tempFragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().add(R.id.content_frame, tempFragment).commit();
			invalidateOptionsMenu();
		}
	}

	/**
	 * Helper method to when a item in the list is pressed that gives you an alertdialog to choose
	 * if you want to change, erase or do nothing to a DietActivity.
	 * @param position
	 */
    private void editMealItem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set title
        builder.setTitle("Välj alternativ");

        // set dialog message
        builder.setPositiveButton("Ändra", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
				startNewFragmentToEditMeal((DietActivity)mealList.getItemAtPosition(position));
            }
        });
        builder.setNegativeButton("Radera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
                myDiary.removeActivity(cal, ((DietActivity)mealList.getItemAtPosition(position)).getID());
            }
        });
        builder.setNeutralButton("Tillbaka", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.cancel();
            }
        });

        // create alert dialog
        AlertDialog alertDialog = builder.create();
        // show it
        alertDialog.show();
    }

	/**
	 * updates the graph and the list with a new sets of DietActivities.
	 * @param activityList - the new sets of DietActivity
	 */
    private void updateActivityList(ArrayList<DietActivity> activityList) {

        double calSum = 0, carbSum = 0 , protSum = 0, fatSum = 0;
        for (DietActivity act : activityList) {
            calSum += act.getCalorieCount();
            carbSum += act.getCarbCount();
            protSum += act.getProteinCount();
            fatSum += act.getFatCount();
        }

        series.resetData(new DataPoint[] {
                new DataPoint(10, calSum),
                new DataPoint(20, carbSum),
                new DataPoint(30, protSum),
                new DataPoint(40, fatSum),
        });
//
        kcalText.setText(calSum + "");
        carbText.setText(carbSum + "");
        protText.setText(protSum + "");
        fatText.setText(fatSum + "");
        series.setSpacing(8);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        series.setSpacing(40);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(dietGraph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"Kcal", "Kolhydrater", "Proteiner", "Fett"});

        dietGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        dietGraph.getGridLabelRenderer().setVerticalAxisTitle("Mängd");
        dietGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);


        dietGraph.getViewport().setXAxisBoundsManual(true);
        dietGraph.getViewport().setMinX(5);
        dietGraph.getViewport().setMaxX(45);

        dietGraph.removeAllSeries();
        dietGraph.addSeries(series);

        updateMealList();
    }

	/**
	 * Method to update the graph and list with all activities of a day
	 * @param cal - the day to take activities from
	 */
    private void updateDayScreen(Calendar cal) {

        activityList = (ArrayList) myDiary.showDaysActivities(cal);

        updateActivityList(activityList);
    }

	/**
	 * Method to update the graph anad list with all activities of a week.
	 * @param date - a Day in the week to focus.
	 */
    private void updateWeekScreen(Calendar date) {

		//Calculates the start and end date for a given date and print out the diet activities for that interval
        Pair<Calendar, Calendar> pairDate = getDateIntervalOfWeek(date);

        activityList = (ArrayList) myDiary.showPeriodActivities(pairDate.first, pairDate.second);

        updateActivityList(activityList);
    }

	/**
	 * @return - if the dayview button is pressed or not.
	 */
    private boolean isDayView() {
        return dayRadioButton.isActivated();
    }

	/**
	 * Method to handle when you press the day view. This will change the view and you will be able
	 * to look at your daily food consumption insteed of weekly
	 * @param view - not used. needed for android
	 */
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
            updateDayScreen(cal);

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
            updateWeekScreen(cal);
        }
    }

	/**
	 * Method to handle when you press the day view. This will change the view and you will be able
	 * to look at your weekly food consumption insteed of daily
	 * @param view - not used. needed for android
	 */
    public void onRightButtonClick(View view) {

        if (isDayView() && dayOffset != 0) { // makes sure that you can't proceed past today's date

            dayOffset++;
            cal.add(Calendar.DATE, 1);
            String chosenDate = sdfShowFullDate.format(cal.getTime());

            if (dayOffset == -1) {
                dateButton.setText("Igår");
            } else if (dayOffset == 0) {
                dateButton.setText("Idag");
            } else {
                dateButton.setText(chosenDate);
            }
            updateDayScreen(cal);

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
            updateWeekScreen(cal);
        }
    }

	/**
	 * Method to find the date interval of a week for a current date
	 * @param date - a date in the week you want the start day and end day of.
	 * @return - to dates the is the first an last week.
	 */
    private Pair<Calendar, Calendar> getDateIntervalOfWeek(Calendar date) {

        Calendar c = (Calendar) date.clone();
        c.add(Calendar.DATE, 0);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

        Calendar firstDay = (Calendar) c.clone();
        // we do not need the same day a week after, that's why use 6, not 7
        c.add(Calendar.DAY_OF_MONTH, 6);
        Calendar lastDay = (Calendar) c.clone();

        return new Pair<>(firstDay, lastDay);
    }

	/**
	 * Method to update the graph with the today or this weeks consumption.
	 * @param view - not used. needed for android
	 */
    public void onDateButtonClick(View view) {
        if (isDayView()) {
            resetDay();
        } else {
            resetWeek();
        }
    }

	/**
	 * Method to reset to today for the date to update graph and list with
	 */
    private void resetDay() {
        dayOffset = 0;
        cal.setTime(Calendar.getInstance().getTime());
        dateButton.setText("Idag");
    }

	/**
	 * Method to reset to todays week for the date to update graph and list with
	 */
    private void resetWeek() {
        weekOffset = 0;
        cal.setTime(Calendar.getInstance().getTime());
        dateButton.setText("Denna vecka");
    }

	@Override
	public void onBackPressed() {
		//Added this if statement to not use backpressed if tempFragment is shown
		if(tempFragment != null){
			getSupportFragmentManager().beginTransaction().remove(tempFragment).commit();
			tempFragment = null;
			updateMealList();
			invalidateOptionsMenu();
		}else{
			super.onBackPressed();
		}
	}

	/**
	 * Listener to the button that switch the view to only display days
	 * @param view
	 */
	public void onDayViewClick(View view) {
        updateDayScreen(cal);
    }

	/**
	 * Listener to the button that switch the view to only display weeks
	 * @param view
	 */
    public void onWeekViewClick(View view) {
        updateWeekScreen(cal);
    }
}