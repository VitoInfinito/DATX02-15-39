package com.kandidat.datx02_15_39.tok.layout;


import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
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

    private int foodCal = 500, foodProt = 30, foodFat = 50, foodCarb = 50;

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
        foodList = new ArrayList<>();

        dayRadioButton = (Button) findViewById(R.id.day_radioButton);
        weekRadioButton = (Button) findViewById(R.id.week_radiobutton);
        dateButton = (Button) findViewById(R.id.dateButton);
        kcalText = (TextView) findViewById(R.id.kcal_text_view);
        carbText = (TextView) findViewById(R.id.carb_text_view);
        protText = (TextView) findViewById(R.id.protein_text_view);
        fatText = (TextView) findViewById(R.id.fat_text_view);

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
        fillGraph(200, 30, 40, 50);

        dayRadioButton.setPressed(true);
        dayRadioButton.setOnTouchListener(dayAndWeekListener);
        weekRadioButton.setOnTouchListener(dayAndWeekListener);

        createTestFoodList();

        DietActivity dietActivity = new DietActivity(foodList, cal);
        dietActivity.getFoodList();
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

    private void createTestFoodList() {
        Food testFood = new Food(500, 30, 20, 40, "Test", "Mat att testa med", Food.FoodPrefix.g, 200);
        Food testFoodTwo = new Food(300, 24, 15, 45, "Test Två", "Nu testar vi ännu", Food.FoodPrefix.g, 156);
        ArrayList<Food> foodList = new ArrayList<>();
        foodList.add(testFood);
        foodList.add(testFoodTwo);
    }

    private void fillGraph(int cal, int carb, int prot, int fat) {

        series.resetData(new DataPoint[] {
                new DataPoint(10,cal),
                new DataPoint(20, carb),
                new DataPoint(30, prot),
                new DataPoint(40, fat)
        });

        series.setSpacing(10);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(dietGraph);
        staticLabelsFormatter.setHorizontalLabels(new String[]{"low", "middle", "high"});
        dietGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        dietGraph.addSeries(series);


    }

//    private void printNutrients(int kcal, int carb, int prot, int fat) {
//        kcalText.setText(kcal + " kcal");
//        carbText.setText(carb + " g");
//        protText.setText(prot + " g");
//        fatText.setText(fat + "g ");
//    }

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