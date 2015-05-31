package com.kandidat.datx02_15_39.tok.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.jawbone.IGraphSetup;
import com.kandidat.datx02_15_39.tok.jawbone.JawboneSetupActivity;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.account.Account;
import com.kandidat.datx02_15_39.tok.model.sleep.Sleep;
import com.kandidat.datx02_15_39.tok.model.workout.CustomListAdapter;
import com.kandidat.datx02_15_39.tok.model.workout.Workout;
import com.kandidat.datx02_15_39.tok.model.workout.WorkoutActivity;
import com.kandidat.datx02_15_39.tok.model.workout.WorkoutDiary;
import com.kandidat.datx02_15_39.tok.utility.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class WorkoutHomeActivity extends CustomActionBarActivity implements IGraphSetup{

    private WorkoutDiary diary;

    private static final String TAG = WorkoutHomeActivity.class.getSimpleName();

    private String mAccessToken;
    private String mClientSecret;

    private TextView textDay;
    private Date todaysDate;
    private BarGraphSeries<DataPoint> series;
    private GraphView graph;

    private ArrayList <WorkoutActivity> workoutActivityList;
    private int dayOffset = 0;
    private int weekOffset = 0;
    private Calendar cal;

    private boolean isWeekView = true;

    private Integer [] imgid ={
            R.drawable.yoga_icon,
            R.drawable.sprint,
            R.drawable.soccer,
            R.drawable.strength
    };

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdfShowDay = new SimpleDateFormat("dd/MM");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdfShowTime = new SimpleDateFormat("HH:mm");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdfShowFullTime = new SimpleDateFormat("yyyy.MM.dd");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_workout_home);
		initMenu(R.layout.activity_workout_home);

        Calendar tmpCal = Calendar.getInstance();
        Date startDate = tmpCal.getTime();
        tmpCal.set(Calendar.HOUR_OF_DAY, Calendar.HOUR_OF_DAY+2);
        Date stopDate = tmpCal.getTime();

        Button previousDateButton = (Button) findViewById(R.id.previousDayButton);
        previousDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPreviousButtonClick(v);

            }
        });

        Button nextDateButton = (Button) findViewById(R.id.nextDayButton);
        nextDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextButtonClick(v);

            }
        });

        Button dayToggleButton = (Button) findViewById(R.id.day_radioButton);
        dayToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDayButtonClick(v);

            }
        });

        Button weekToggleButton = (Button) findViewById(R.id.week_radiobutton);
        weekToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWeekButtonClick(v);

            }
        });


        //Dummy listitem
       /* Workout workout = new Workout(startDate, stopDate, 15, Workout.WorkoutType.CARDIO);
        WorkoutActivity workoutActivity = new WorkoutActivity("WORKOUT", workout);
        WorkoutDiary workoutDiary = (WorkoutDiary) WorkoutDiary.getInstance();
        workoutDiary.addActivity(startDate, workoutActivity);*/

        todaysDate = Calendar.getInstance().getTime();
        diary = (WorkoutDiary) WorkoutDiary.getInstance();

        textDay = (TextView) findViewById(R.id.textDay);

        cal = Calendar.getInstance();

        setupGraph();
        //graph = (GraphView) findViewById(R.id.workout_graph);
        //series = new BarGraphSeries<>();
        //fillListWithDummyData();
        fillListWithDataFromCalendar(cal);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F5B3C0")));

        updateDayScreen(cal);

        if(isWeekView){
            textDay.setText("Denna vecka");
        }else {
            textDay.setText("Idag");
        }

        if(Account.getInstance().isConnectedUP()) {
            Intent intent = getIntent();
            if (intent != null) {
                mClientSecret = intent.getStringExtra(UpPlatformSdkConstants.CLIENT_SECRET);
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            mAccessToken = preferences.getString(UpPlatformSdkConstants.UP_PLATFORM_ACCESS_TOKEN, null);

            if (mAccessToken != null) {
                ApiManager.getRequestInterceptor().setAccessToken(mAccessToken);
            }



            fetchWorkoutFromUP();


        }else {
            Toast.makeText(getActivity(), R.string.no_connection_UP, Toast.LENGTH_LONG).show();
            Account.getInstance().setNextClassCallback(this.getClass());
            startActivity(new Intent(this, JawboneSetupActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            startActivity(new Intent(this, AddWorkoutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void fetchWorkoutFromUP() {
        Log.e(TAG, "making Get Workout Events List api call ...");
        ApiManager.getRestApiInterface().getWorkoutEventList(
                UpPlatformSdkConstants.API_VERSION_STRING,
                getWorkoutEventsListRequestParams(),
                workoutListCallbackListener);
    }

    /**
     * Callback listener for fetching list of workout activities from UP server
     */
    private Callback workoutListCallbackListener = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            Log.e(TAG,  "api call successful, json output: " + o.toString());

            try {
                LinkedTreeMap obj = (LinkedTreeMap) o;

                ArrayList<LinkedTreeMap> array = (ArrayList<LinkedTreeMap>)((LinkedTreeMap)obj.get("data")).get("items");
                for (int i = 0; i < array.size(); i++) {
                    LinkedTreeMap ltm = array.get(i);
                    LinkedTreeMap details = (LinkedTreeMap) ltm.get("details");

                    String xid = ltm.get("xid").toString();
                    Log.e(TAG, "Xid: " + xid);

                    if(diary.getActivity(Utils.MillisToCalendar(Double.valueOf((ltm.get("time_completed").toString())).longValue() * 1000), xid) == null) {

                        //Creating new activity with a workout using the information from UP and adding it to the diary
                        diary.addActivity(new WorkoutActivity(xid, Utils.MillisToCalendar(Double.valueOf((ltm.get("time_completed").toString())).longValue()*1000),
                                new Workout(new Date(Double.valueOf((ltm.get("time_created").toString())).longValue()*1000),
                                        new Date(Double.valueOf((ltm.get("time_completed").toString())).longValue()*1000),
                                        convertUPStringToInt(details.get("intensity").toString()), convertSubToWork(convertUPStringToInt(ltm.get("sub_type").toString())),
                                        convertUPStringToInt(details.get("calories").toString()), convertUPStringToInt(details.get("steps").toString()))));
                    }else {
                        Log.e(TAG, xid + " ALREADY EXISTED");
                    }
                }
            }catch(Exception e){
                Log.e(TAG, "We got an error on our hands: ");
                e.printStackTrace();
            }
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.e(TAG, "api call failed, error message: " + retrofitError.getMessage());
            Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    private static HashMap<String, Integer> getWorkoutEventsListRequestParams(){
        HashMap<String, Integer> queryHashMap = new HashMap<>();
        return queryHashMap;
    }

    private void fillListWithDataFromCalendar(Calendar c){
        ListView lv = (ListView) findViewById(R.id.show_workout);
        Log.e("This is the cal", "" + c.getTime());
        List<IDiaryActivity> activityList = diary.showDaysActivities(c);
        List<String> workoutList = new ArrayList<>();

        Integer [] idList = new Integer[6];

        for(int i=0; i<activityList.size(); i++) {
            List<Workout> list = ((WorkoutActivity) activityList.get(i)).getWorkoutList();
            for(int j=0; j<list.size(); j++) {
                idList[j] = list.get(j).getIconId();
                workoutList.add(list.get(j).getWorkoutType() + " Intensitet: " + list.get(j).getIntensity() + "\n"
                        + sdfShowFullTime.format(list.get(j).getStartTime()) + " "
                        + sdfShowTime.format(list.get(j).getStartTime()) + " - "
                        + sdfShowTime.format(list.get(j).getEndTime()));
            }
        }
        CustomListAdapter adapter = new CustomListAdapter(this, workoutList, imgid);
        lv.setAdapter(adapter);
    }

    public Context getActivity() {
        return this;
    }

    private void updateActivityList (ArrayList<WorkoutActivity> workoutActivityList){
        double intensity =0;
        ArrayList <Workout> workoutArrayList = new ArrayList<>();
        GraphView wGraph = (GraphView) findViewById(R.id.workout_graph);

        for(int i = 0; i<workoutActivityList.size(); i++) {
            for (int j = 0; j < workoutActivityList.get(i).getWorkoutList().size(); j++) {
                workoutArrayList.add(workoutActivityList.get(i).getWorkoutList().get(j));
            }
        }

        for(Workout workout: workoutArrayList){
            intensity += workout.getIntensity();
        }

        /*if(series!= null){
            series.resetData(new DataPoint[]{
                   // new DataPoint(0,0),
                    //new DataPoint(10, intensity),
                   // new DataPoint(20, intensity),
                  //  new DataPoint(30, intensity),
                   // new DataPoint(40, intensity),

            });
        series.setSpacing(20);
va
        }*/

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
//        staticLabelsFormatter.setHorizontalLabels(new String[] {"Antal });

        wGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        wGraph.getGridLabelRenderer().setVerticalAxisTitle("Intensitet");
        wGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);

        series.setColor(Color.DKGRAY);
        //graph.removeSeries(series);
        //graph.addSeries(series);
    }

    private void updateDayScreen(Calendar cal){
        workoutActivityList = (ArrayList) diary.showDaysActivities(cal);
        updateActivityList(workoutActivityList);
    }

    //Calculates the start and end date for a given date and print out the diet activities for that interval
    private void updateWeekScreen(Calendar date) {
        Pair<Calendar, Calendar> pairDate = getDateIntervalOfWeek(date);
        workoutActivityList= (ArrayList) diary.showPeriodActivities(pairDate.first, pairDate.second);
        updateActivityList(workoutActivityList);
    }

    public List<IDiaryActivity> getListOfActivities(){
        Calendar cal = new GregorianCalendar();
        cal.setTime(todaysDate);

        Calendar cal2 = new GregorianCalendar();
        cal2.add(Calendar.DATE, 1);
        return WorkoutDiary.getInstance().showPeriodActivities(cal, cal2);
    }

    private Pair<Calendar, Calendar> getDateIntervalOfWeek(Calendar pairCal) {

        Calendar c = (Calendar) pairCal.clone();
        c.add(Calendar.DATE, 0);//???
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

        Calendar firstDay = (Calendar) c.clone();
        // we do not need the same day a week after, that's why use 6, not 7
        c.add(Calendar.DAY_OF_MONTH, 6);
        Calendar lastDay = (Calendar) c.clone();

        return new Pair<>(firstDay, lastDay);
    }

    public void onNextButtonClick(View view){
        //Only dayview
        if (!isWeekView && dayOffset != 0) { // makes sure that you can't proceed past today's date

            dayOffset++;
            cal.add(Calendar.DATE, 1);
            String updatedDate = sdfShowFullTime.format(cal.getTime());

            if (dayOffset == -1) {
                textDay.setText("Igår");
            } else if (dayOffset == 0) {
                textDay.setText("Idag");
            } else {
                textDay.setText(updatedDate);
            }
            updateDayScreen(cal);
        //Only weekview
        } else if (isWeekView && weekOffset != 0) {
            weekOffset++;
            cal.add(Calendar.DATE, 7);

            if (weekOffset == -1) {
                textDay.setText("Föregående vecka");
            } else if (weekOffset == 0) {
                textDay.setText("Denna vecka");
            } else {
                textDay.setText("Vecka " + cal.get(Calendar.WEEK_OF_YEAR));
            }
            updateWeekScreen(cal);
        }
        fillListWithDataFromCalendar(cal);
    }

    public void onPreviousButtonClick(View view){
        Log.e("Is this weekview?", " - " + isWeekView);
        if (!isWeekView) {
            Log.e("Inside", "I'm fucking inside DAYVIEEEW!");
            dayOffset--;
            Log.e("Dayoffset", "" + dayOffset);
            cal.add(Calendar.DATE, -1); //Sets the date to one day from the current date
            String chosenDate = sdfShowFullTime.format(cal.getTime()); // sets to yyyy-mm-dd format
            Log.e("Date:", chosenDate);

            if (dayOffset == -1) {
                textDay.setText("Igår");
            } else if (dayOffset == 0) {
                textDay.setText("Idag");
            } else {
                textDay.setText(chosenDate);
            }
            updateDayScreen(cal);
            Log.e("Cal", "" + cal.getTime());
            fillListWithDataFromCalendar(cal);

        } else {
            Log.e("Inside", "I'm fucking inside WEEKVIEEEW!");
            weekOffset--;
            cal.add(Calendar.DATE, -7);

            if (weekOffset == -1) {
                textDay.setText("Föregående vecka");
            } else if (weekOffset == 0) {
                textDay.setText("Denna vecka");
            } else {
                textDay.setText("Vecka " + cal.get(Calendar.WEEK_OF_YEAR));
            }
            updateWeekScreen(cal);
        }
    }
    private void resetDay() {
        dayOffset = 0;
        cal.setTime(Calendar.getInstance().getTime());
        textDay.setText("Idag");
    }

    private void resetWeek() {
        weekOffset = 0;
        cal.setTime(Calendar.getInstance().getTime());
        textDay.setText("Denna vecka");
    }

    public void onWeekButtonClick(View view){
        isWeekView = true;
        updateWeekScreen(cal);
        resetWeek();
    }

    public void onDayButtonClick(View view){
        isWeekView = false;
        updateDayScreen(cal);
        resetDay();
    }

    /**
     * Helper method to sort out which workout type should be used
     */
    private Workout.WorkoutType convertSubToWork(int subtype) {
        switch(subtype) {
            case 1:
            case 24:
                return Workout.WorkoutType.WALK;
            case 2:
            case 4:
            case 5:
            case 11:
            case 13:
            case 14:
            case 15:
            case 26:
            case 27:
                return Workout.WorkoutType.CARDIO;
            case 3:
            case 8:
            case 12:
                return Workout.WorkoutType.STRENGTH;
            case 9:
            case 10:
            case 17:
            case 23:
            case 25:
            case 28:
            case 29:
                return Workout.WorkoutType.CUSTOM;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
                return Workout.WorkoutType.SPORT;
            default:
                return Workout.WorkoutType.FLEX;
        }
    }

    /**
     * Helper method for returning a correct int when fetching it from UP
     * @param integerWannabe
     * @return
     */
    private int convertUPStringToInt(String integerWannabe) {
        return Integer.parseInt(integerWannabe.substring(0, integerWannabe.indexOf(".")));

    }

    public List<DataPoint[]> fetchDataPoints(Date date) {
        List<DataPoint> workoutGraphPoints = new ArrayList<>();
        List<Workout> workoutList;
        if(isWeekView) {
            workoutList = diary.getWorkoutListFromWeek(date);
        }else {
            workoutList = diary.getWorkoutListFromDate(date);
        }

        int steps = 0;
        int calories = 0;
        int intensity = 0;
        int nbrOfIntensities = 0;
        if(!workoutList.isEmpty()) {
            for (int i = 0; i < workoutList.size(); i++) {
                Workout workout = workoutList.get(i);

                steps += workout.getSteps();
                calories += workout.getCalories();
                intensity += workout.getIntensity();
                nbrOfIntensities++;
            }
        }
        if(nbrOfIntensities != 0)
            intensity /= nbrOfIntensities;

        List<DataPoint[]> dataPointsList = new ArrayList<>();
        dataPointsList.add(new DataPoint[]{
                new DataPoint(0,0),
                new DataPoint(10, steps),
                new DataPoint(20, calories),
                new DataPoint(30, intensity),
                new DataPoint(40, 0),
        });

        return new ArrayList<>(dataPointsList);

    }

    public void setupGraph() {
        cal = Calendar.getInstance();
        Date activeDate = cal.getTime();

        GraphView wGraph = (GraphView) findViewById(R.id.workout_graph);

        DataPoint[] barGraphPoints = fetchDataPoints(activeDate).get(0);
        series = new BarGraphSeries<>(barGraphPoints);

        wGraph.addSeries(series);
        series.setColor(Color.rgb(0, 204, 204));

        //Y Axis bounds
        wGraph.getViewport().setScalable(true);
        wGraph.getViewport().setYAxisBoundsManual(true);


    //    updateInformationDisplay();

        series.setSpacing(20);
        wGraph.getGridLabelRenderer().setNumVerticalLabels(0);

        //wGraph.getGridLabelRenderer().setGridColor(Color.argb(0,255,255,255));
    }
}