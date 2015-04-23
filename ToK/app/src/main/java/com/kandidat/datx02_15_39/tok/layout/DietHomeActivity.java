package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietDiary;
import com.kandidat.datx02_15_39.tok.model.diet.Food;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DietHomeActivity extends CustomActionBarActivity {

    private Button dayRadioButton;
    private Button weekRadioButton;
    private Button dateButton;
    private GraphView dietGraph;
    private TextView kcalText, carbText, protText, fatText;
    private Calendar cal;
    private BarGraphSeries<DataPoint> series;
    ArrayList<Food> foodList;

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
        initMenu(R.layout.activity_diet_home);

        dietGraph = (GraphView) findViewById(R.id.diet_graph);
        series = new BarGraphSeries<>();
        foodList = createTestFoodList();

        cal = Calendar.getInstance();
        tempCal = Calendar.getInstance();

        dayRadioButton = (Button) findViewById(R.id.day_radioButton);
        weekRadioButton = (Button) findViewById(R.id.week_radiobutton);
        dateButton = (Button) findViewById(R.id.dateButton);
        kcalText = (TextView) findViewById(R.id.kcal_text_view);
        carbText = (TextView) findViewById(R.id.carb_text_view);
        protText = (TextView) findViewById(R.id.protein_text_view);
        fatText = (TextView) findViewById(R.id.fat_text_view);

        View.OnTouchListener dayAndWeekListener = new View.OnTouchListener() { //makes sure that not both day and week button are pressed simultaneously
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (v == dayRadioButton) {
                    weekRadioButton.setPressed(false);
                    v.setPressed(true);
                    resetDay();

                } else {
                    dayRadioButton.setPressed(false);
                    v.setPressed(true);
                    resetWeek();
                }
                return true;
            }
        };

        myDiary = DietDiary.getInstance();

        dayRadioButton.setPressed(true);
        dayRadioButton.setOnTouchListener(dayAndWeekListener);
        weekRadioButton.setOnTouchListener(dayAndWeekListener);

        updateDayScreen(cal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            View add = getLayoutInflater().inflate(R.layout.activity_add_all, null);
            AlertDialog ad = new AlertDialog.Builder(this, R.style.CustomDialog)
                    .create();
            ad.setView(add);
            ad.setCanceledOnTouchOutside(true);
            ad.show();
        }

        return super.onOptionsItemSelected(item);
    }

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
            meal_item_date.setHint(sdfShowFullDate.format(getItem(position).getDate()));


            return convertView;
        }
    }

    private ArrayList<Food> createTestFoodList() {
        Food testFood = new Food(460, 120, 20, 40, "Kokt potatis", "Mjölig potatis som är kokt", Food.FoodPrefix.g, 200);
        Food testFoodTwo = new Food(300, 24, 15, 45, "Kokt Wienerkorv", "Wienerkorv som är kokt", Food.FoodPrefix.g, 250);
        Food testFoodThree = new Food(150, 24, 15, 45, "Bostongurka", "Finns inget mer att säga", Food.FoodPrefix.g, 250);
        ArrayList<Food> foodList = new ArrayList<>();
        foodList.add(testFood);
        foodList.add(testFoodTwo);
        foodList.add(testFoodThree);
        return foodList;
    }

    //updates the meal list with the appropriate meals for given dates
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

    //Decides what happens on click on meal items in the ListView
    private class MealItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            editMealItem(position);
        }
    }

    private void editMealItem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // set title
        builder.setTitle("Välj alternativ");

        // set dialog message
        builder.setPositiveButton("Ändra", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Radera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO fråga Marcus om understående
                myDiary.removeActivity(cal, mealList.getChildAt(position).);
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

    void sendMessage(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

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
        dietGraph.getViewport().setMaxX(45); //om större än 45 så fuckar de statiska lablarna ur, venne varför.

        //TODO lägg till nedanstående line till Workout
        dietGraph.removeAllSeries();
        dietGraph.addSeries(series);

        updateMealList();
    }

    
    private void updateDayScreen(Calendar cal) {

        activityList = (ArrayList) myDiary.showDaysActivities(cal);

        updateActivityList(activityList);
    }

    //Calculates the start and end date for a given date and print out the diet activities for that interval
    private void updateWeekScreen(Calendar date) {

        Pair<Calendar, Calendar> pairDate = getDateIntervalOfWeek(date);

        activityList = (ArrayList) myDiary.showWeekActivities(pairDate.first, pairDate.second);

        updateActivityList(activityList);
    }

    private boolean isDayView() {
        return dayRadioButton.isPressed();
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

    //Fins the date interval of a week for a current date
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

    public void onDayViewClick(View view) {
        updateDayScreen(cal);
    }

    public void onWeekViewClick(View view) {
        updateWeekScreen(cal);
    }
}