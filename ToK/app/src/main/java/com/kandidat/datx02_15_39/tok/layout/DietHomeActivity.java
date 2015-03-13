package com.kandidat.datx02_15_39.tok.layout;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
    ArrayList<Food> foodListTwo;
    ArrayList<DietActivity> activityList;
    private ListView mealList;
    private MealListAdapter mla;
                                    //TODO make it constructor of dietActivity take name as a paramenter.
    DietDiary myDiary;
    DietActivity myActivity;
    DietActivity mySecondActivity;

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
        foodListTwo = createSeccondTestFoodList();

        dayRadioButton = (Button) findViewById(R.id.day_radioButton);
        weekRadioButton = (Button) findViewById(R.id.week_radiobutton);
        dateButton = (Button) findViewById(R.id.dateButton);
//        kcalText = (TextView) findViewById(R.id.kcal_text_view);
//        carbText = (TextView) findViewById(R.id.carb_text_view);
//        protText = (TextView) findViewById(R.id.protein_text_view);
//        fatText = (TextView) findViewById(R.id.fat_text_view);

        cal = Calendar.getInstance();

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

        Calendar tempCal = Calendar.getInstance();
        tempCal.add(Calendar.DATE, -1); // temp calendar to see if the acitivity having this calendar will show on yesterday.

        activityList = new ArrayList<>();
        myActivity = new DietActivity(foodList, tempCal);
        mySecondActivity = new DietActivity(foodListTwo, Calendar.getInstance());
        activityList.add(myActivity);
        activityList.add(mySecondActivity);

        myDiary = DietDiary.getInstance();
        myDiary.addActivity(myActivity.getDate(), myActivity);
        myDiary.addActivity(mySecondActivity.getDate(), mySecondActivity);

        dayRadioButton.setPressed(true);
        dayRadioButton.setOnTouchListener(dayAndWeekListener);
        weekRadioButton.setOnTouchListener(dayAndWeekListener);
        updateSearchList();
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

            // Lookup view for data population
            TextView meal_item_name = (TextView) convertView.findViewById(R.id.meal_item_name);
            TextView meal_item_calories = (TextView) convertView.findViewById(R.id.meal_item_calories);
            // Populate the data into the template view using the data object
            meal_item_name.setHint(getItem(position).getName());
            meal_item_calories.setHint(getItem(position).getCalorieCount() + "");
            // Return the completed view to render on screen

            return convertView;
        }
    }

    private ArrayList<Food> createTestFoodList() {
        Food testFood = new Food(500, 30, 20, 40, "Test", "Mat att testa med", Food.FoodPrefix.g, 200);
        Food testFoodTwo = new Food(300, 24, 15, 45, "Test Två", "Nu testar vi ännu", Food.FoodPrefix.g, 250);
        ArrayList<Food> foodList = new ArrayList<>();
        foodList.add(testFood);
        foodList.add(testFoodTwo);
        return foodList;
    }

    private ArrayList<Food> createSeccondTestFoodList() {
        Food testFood = new Food(1200, 150, 55, 70, "Pirre", "Kebabpizza med massa extra kött", Food.FoodPrefix.g, 200);
        Food testFoodTwo = new Food(455, 4.5, 23, 50, "Schtek", "Fistpump-schtek med x-tra allt", Food.FoodPrefix.g, 156);
        ArrayList<Food> foodList = new ArrayList<>();
        foodList.add(testFood);
        foodList.add(testFoodTwo);
        return foodList;
    }

    private void updateSearchList(){
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

    private class MealItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectedItem(position);
        }
    }

    private void selectedItem(int position) {
        Toast t = Toast.makeText(this, "Item selected", Toast.LENGTH_SHORT);
        t.show();

    }

    private void fillGraph(Calendar cal) {
        ArrayList<DietActivity> list = (ArrayList) myDiary.showDaysActivities(cal);

        double calSum = 10, carbSum = 10 , protSum = 10, fatSum = 10;
        for (DietActivity act : list) {
            calSum += act.getCalorieCount();
            carbSum += act.getCarbCount();
            protSum += act.getProteinCount();
            fatSum += act.getFatCount();
        }

        series.resetData(new DataPoint[] {
            new DataPoint(10, calSum),
            new DataPoint(20, carbSum),
            new DataPoint(30, protSum),
            new DataPoint(40, fatSum)
        });

        series.setSpacing(10);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(dietGraph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"Kcal", "Kolhydrater", "Proteiner", "Fett"});
        dietGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        dietGraph.getGridLabelRenderer().setVerticalAxisTitle("Mängd");
        dietGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        dietGraph.addSeries(series);
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
        fillGraph(cal);
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
        fillGraph(cal);
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