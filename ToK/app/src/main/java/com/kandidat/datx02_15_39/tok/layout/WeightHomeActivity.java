package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.account.Account;
import com.kandidat.datx02_15_39.tok.model.weight.Weight;
import com.kandidat.datx02_15_39.tok.model.weight.WeightActivity;
import com.kandidat.datx02_15_39.tok.model.weight.WeightDiary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class WeightHomeActivity extends CustomActionBarActivity {
    private WeightDiary diary;
    private GregorianCalendar currentCalendar;
    private Date activeDate;
    private LineGraphSeries<DataPoint> series;

    private SimpleDateFormat sdfShowYearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfShowMonthDay = new SimpleDateFormat("dd/MM");
    private SimpleDateFormat sdfShowMonth = new SimpleDateFormat("MM");
    private SimpleDateFormat sdfShowDay = new SimpleDateFormat("dd");
    private SimpleDateFormat sdfShowFullTime = new SimpleDateFormat("yyyy.MM.dd HH:mm");

	private int selectedWeightValue = 95;
	private Date selectedDate;

	private Button weightText;
	private Weight weight;
	private WeightActivity weightActivity;
	private Calendar cal = Calendar.getInstance();

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_home);
        initMenu(R.layout.activity_weight_home);

        diary = (WeightDiary) WeightDiary.getInstance();

        //TODO change to not account for specific times i.e seconds and minutes
        currentCalendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        activeDate = currentCalendar.getTime();
        //Used for testing
        produceFakeData();
        //View viewGraph = findViewById(R.id.imageViewGraph);
        //DrawDiagram graphDiagram = new DrawDiagram(viewGraph.getContext());

	    weightText = (Button) findViewById(R.id.weightText);

	    weight = new Weight(selectedWeightValue);
	    weightActivity = new WeightActivity("id1", weight, activeDate);
	    WeightDiary weightDiary = (WeightDiary) WeightDiary.getInstance();
	    weightDiary.addActivity(activeDate, diary.getActivityFromDate(activeDate));

		selectedWeightValue = (int) weight.getWeight();
	    weightText.setText(selectedWeightValue + " kg");

		Button dateButton = (Button) findViewById(R.id.dateButton);
		dateButton.setText("Datum :" + sdfShowYearMonthDay.format(activeDate));

        GraphView graph = (GraphView) findViewById(R.id.graph);

        series = new LineGraphSeries<>(fetchDataPoints());

        graph.addSeries(series);
        series.setTitle(sdfShowYearMonthDay.format(activeDate));
        setGraphXBounds(cal, graph);

        series.setColor(Color.RED);

       /* graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    int hours = (int) value;
                    int minutes = (int) ((value - hours) * 100);
                    if (minutes > 60) {
                        minutes -= 60;
                        hours++;
                    }

                    if(hours > 24) {
                        hours -= 24;
                    }

                    return super.formatLabel(hours, true) + ":" + (minutes < 10 ? '0' + minutes : minutes);
                } else {
                    return super.formatLabel(value, false);
                }
            }
        });*/

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // transform number to time
                    return sdfShowMonthDay.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

/*        series.setCustomShape(new PointsGraphSeries.CustomShape() {
            @Override
            public void draw(Canvas canvas, Paint paint, float x, float y) {
                //Sleep drawSleep = ((SleepActivity) diary.getActivityFromDate(activeDate)).getSleepThatStarts(earlierDate);
                //System.out.println("DrawSleep at " + x + " starts at time " + drawSleep);

                paint.setStrokeWidth(10);
                canvas.drawLine(x-20, y-20, x+20, y+20, paint);
                canvas.drawLine(x+20, y-20, x-20, y+20, paint);
            }
        });*/


      /* // styling
       series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
           @Override
           public int get(DataPoint data) {
               //return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
               if(data.getY() == 3) {
                   return Color.rgb(255, 0, 0);
               }else if(data.getY() == 2) {
                   return Color.rgb(0, 255, 0);
               }

               return Color.rgb(0, 0, 255);
           }
       });*/


 /*      StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
       staticLabelsFormatter.setVerticalLabels(new String[] {"V", "L", "D"});
       graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

*/

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Series1: On Data Point clicked: " + sdfShowFullTime.format(new Date((long) dataPoint.getX())) + "/" + dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_weight_home, menu);
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

    public Context getActivity() {
        return this;
    }

    /**
     * Fetches data points belonging to current date and up to 20 days earlier
     * @return an array of DataPoints with the weights belonging to the dates
     */
    private DataPoint[] fetchDataPoints() {
        WeightDiary wd = Account.getInstance().getWeightDiary();
        List<WeightActivity> wal = new ArrayList<>();

        for(int i=-20; i<=0; i++) {
            Date date = new GregorianCalendar(cal.get(Calendar.YEAR),
		            cal.get(Calendar.MONTH),
		            cal.get(Calendar.DAY_OF_MONTH) + i,
		            cal.get(Calendar.HOUR_OF_DAY),
		            cal.get(Calendar.MINUTE)).getTime();
            WeightActivity wa = (WeightActivity) wd.getActivityFromDate(date);
            if(wa != null) {
                wal.add(wa);
            }
        }
	    return constructDataPoints(wal);
    }

	/**
	 * Creates and return datapoints for the graph.
	 *
	 * @param wal A list which contains weight activities.
	 * @return A list of datapoints portraying the activities.
	 */
	private DataPoint[] constructDataPoints (List<WeightActivity> wal){
		if(wal.size() > 0) {
			DataPoint[] dp = new DataPoint[wal.size()];

			for(int j=0; j<wal.size(); j++) {
				Date pointDate = wal.get(j).getDate();

				double pseudoDate = Double.parseDouble(sdfShowDay.format(pointDate)) / 100;
				pseudoDate += Double.parseDouble(sdfShowMonth.format(pointDate));

				dp[j] = new DataPoint(pointDate.getTime(), wal.get(j).getWeight().getWeight());
			}

			return dp;
		}

		//If activity was not found we return an empty list.
		return new DataPoint[]{
				new DataPoint(0,0)
		};
	}

    /**
     * Sets the x bounds in the graph between 20 days before the given calendar to the current calendar day
     *
     * @param cal is the calendar used to determine the x bound
     * @param graph is the graph we set the x bound to
     */
    private void setGraphXBounds(Calendar cal, GraphView graph) {
        Date startTime = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) - 20, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime();
        Date stopTime = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime();


       /* double pseudoStart = Double.parseDouble(sdfShowDay.format(startTime))/100;
        pseudoStart += Double.parseDouble(sdfShowMonth.format(startTime));

        double pseudoStop = Double.parseDouble(sdfShowDay.format(stopTime))/100;
        pseudoStop += Double.parseDouble(sdfShowMonth.format(stopTime));

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(pseudoStart);
        graph.getViewport().setMaxX(pseudoStop);*/
/*
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX();
        graph.getViewport().setMaxX(stopTime);*/

    }

    /*Temporary for testing*/
    private void produceFakeData() {
        Calendar cal = Calendar.getInstance();
        if(diary.getActivityFromDate(cal.getTime()) == null) {

            Date todaysDate = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime();



            for(int i=-10; i<0; i++) {
                diary.addActivity(new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + i, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime(),
                        new WeightActivity("id" + i, new Weight(100+i),
                                new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + i, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)).getTime()));
            }



        }
    }

	/**
	 * Creates an alertdialog and gives the user the option to change the date that will be added
	 * to the weight diary.
	 *
	 * @param view Not used.
	 */
	public void changeDateOnClick(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

		builder.setTitle("Datum");
		builder.setIcon(R.drawable.weigth_scale);

		final DatePicker datePicker = new DatePicker(this);

		datePicker.setMaxDate(cal.getTimeInMillis());

		builder.setView(datePicker);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Calendar cal = Calendar.getInstance();

				cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

				selectedDate = cal.getTime();

				Button dateButton = (Button) findViewById(R.id.dateButton);
				dateButton.setText("Datum: " + sdfShowYearMonthDay.format(selectedDate));
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});

		AlertDialog dialog = builder.create();

		dialog.show();
	}

	/**
	 * Creates an alertdialog and gives the user the option to change the weight in that will be
	 * added to their weight diary.
	 *
	 * @param view Not used.
	 */
	public void changeWeightOnClick(View view){

		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

		builder.setTitle("Vikt");
		builder.setIcon(R.drawable.weigth_scale);

		final NumberPicker weightPicker = new NumberPicker(this);

		weightPicker.setMinValue(25);
		weightPicker.setMaxValue(200);
		weightPicker.setValue(80);

		builder.setView(weightPicker);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				selectedWeightValue = weightPicker.getValue();
				weightText.setText(selectedWeightValue + " kg");
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});

		AlertDialog dialog = builder.create();

		dialog.show();
	}

	/**
	 * Saves the selected values from the alert dialogs and adds them to the weight diary.
	 *
	 * @param view Not used.
	 */
	public void saveChangesOnClick(View view){
		Weight newWeight = new Weight(selectedWeightValue);
		weightActivity = new WeightActivity("id15", newWeight, selectedDate);
		WeightDiary weightDiary = (WeightDiary) WeightDiary.getInstance();
		weightDiary.addActivity(selectedDate, weightActivity);
		//selectedDate, diary.getActivityFromDate(selectedDate)
		//series.resetData(fetchDataPoints());
		//startActivity(new Intent(this, MainActivity.class));
	}
}