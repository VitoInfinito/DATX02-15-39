package com.kandidat.datx02_15_39.tok.layout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.jawbone.upplatformsdk.api.ApiManager;
import com.jawbone.upplatformsdk.utils.UpPlatformSdkConstants;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.jawbone.JawboneSetupActivity;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.account.Account;
import com.kandidat.datx02_15_39.tok.model.sleep.Sleep;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepActivity;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepDiary;
import com.kandidat.datx02_15_39.tok.utility.JawboneUtils;
import com.kandidat.datx02_15_39.tok.utility.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SleepHomeActivity extends CustomActionBarActivity {

    private static final String TAG = SleepHomeActivity.class.getSimpleName();

    private String mAccessToken;
    private String mClientSecret;

    private SleepDiary diary;
    private GregorianCalendar currentCalendar;
    private Calendar activeCalendar;

    private LineGraphSeries<DataPoint> lightSleepSeries;
    private LineGraphSeries<DataPoint> deepSleepSeries;
    private LineGraphSeries<DataPoint> awakeSleepSeries;
    private LineGraphSeries<DataPoint> manualSleepSeries;
    //For lack of a better idea
    private LineGraphSeries<DataPoint> coverSleepSeries;

	@SuppressWarnings("SimpleDateFormat")
    private SimpleDateFormat sdfShowDay = new SimpleDateFormat("yyyy-MM-dd");
	@SuppressWarnings("SimpleDateFormat")
    private SimpleDateFormat sdfShowTime = new SimpleDateFormat("HH:mm");
	@SuppressWarnings("SimpleDateFormat")
    private SimpleDateFormat sdfShowFullTime = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleep_home);

		findViewById(R.id.previousDayButton).setBackgroundColor(Color.alpha(0));
	    findViewById(R.id.nextDayButton).setBackgroundColor(Color.alpha(0));

	    findViewById(R.id.detailedLayout).setBackgroundColor(Color.rgb(219,219,219));

		initMenu(R.layout.activity_sleep_home);
        diary = (SleepDiary) SleepDiary.getInstance();
        activeCalendar = Calendar.getInstance();

        //Checking if the user is connected to UP. If not they are redirected to the connect page
        //Otherwise data from UP concerning sleep events are collected
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

            fetchSleepFromUP();
        }else {
            Toast.makeText(getActivity(), R.string.no_connection_UP, Toast.LENGTH_LONG).show();
            Account.getInstance().setNextClassCallback(this.getClass());
            startActivity(new Intent(this, JawboneSetupActivity.class));
        }

        setupGraph();

        lightSleepSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "Series1: On Data Point clicked: " + sdfShowFullTime.format(new Date((long) dataPoint.getX())), Toast.LENGTH_SHORT).show();
            }
        });
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
            startActivity(new Intent(this, AddSleepActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method used to fetch the latest sleep events from UP
     */
    private void fetchSleepFromUP() {
        Log.e(TAG, "making Get Sleep Events List api call ...");
        ApiManager.getRestApiInterface().getSleepEventsList(
                UpPlatformSdkConstants.API_VERSION_STRING,
                getSleepEventsListRequestParams(),
                sleepListCallbackListener);
    }

    /**
     * Callback listener for fetching list of sleep activities from UP server
     */
    private Callback sleepListCallbackListener = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            Log.e(TAG,  "api call successful, json output: " + o.toString());

            List<String> list = new ArrayList<>();
            try {
                LinkedTreeMap obj = (LinkedTreeMap) o;

                Log.e(TAG, "data: " + obj.get("data").toString());
                ArrayList<LinkedTreeMap> array = (ArrayList<LinkedTreeMap>)((LinkedTreeMap)obj.get("data")).get("items");
                for (int i = 0; i < array.size(); i++) {
                    LinkedTreeMap ltm = array.get(i);
                    LinkedTreeMap details = (LinkedTreeMap) ltm.get("details");

                    String xid = ltm.get("xid").toString();
                    Log.e(TAG, "Xid: " + xid);

                    //Checking if the sleep event that was found is already in the diary.
                    //If not a call to fetch the sleep phases is called.
                    //If it is a manual sleep, the event is sent to a method that will enter it into the diary as a manual sleep.
                    if(diary.getActivity(Utils.MillisToCalendar(Double.valueOf((ltm.get("time_completed").toString())).longValue()*1000), xid) == null) {
                        if(!details.get("light").toString().equals("0.0") && !details.get("sound").toString().equals("0.0")) {
                            fetchSleepTicksFromUPWithXid(xid);
                        }else {
                            setManualSleepFromUP(xid,
                                    new Date(Double.valueOf((details.get("asleep_time").toString())).longValue()*1000),
                                    new Date(Double.valueOf((details.get("awake_time").toString())).longValue()*1000),
                                    new Date(Double.valueOf((ltm.get("time_created").toString())).longValue()*1000),
                                    new Date(Double.valueOf((ltm.get("time_completed").toString())).longValue()*1000));
                        }
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

    /**
     * Method for adding a manual sleep to the diary
     * @param xid is the id the sleep should contain
     * @param asleepDate is the date when the user fell asleep
     * @param awakeDate is the date of when the user woke up
     * @param createDate is the date of when the event was created
     * @param completeDate is the date of when the event was completed
     */
    private void setManualSleepFromUP(String xid, Date asleepDate, Date awakeDate, Date createDate, Date completeDate) {
        List<Sleep> manualSleep = (new ArrayList<>());
        manualSleep.addAll(Arrays.asList(
                new Sleep(
                        createDate,
                        asleepDate,
                        Sleep.SleepState.AWAKE),
                new Sleep(
                        asleepDate,
                        awakeDate,
                        Sleep.SleepState.MANUAL),
                new Sleep(
                        awakeDate,
                        completeDate,
                        Sleep.SleepState.AWAKE)
        ));
        diary.addActivity(new SleepActivity(xid, manualSleep, completeDate));

    }

    /**
     * Method used to call the sleep phases with a specific ID
     * @param xid is the ID used when calling for the phases connected to that ID
     */
    private void fetchSleepTicksFromUPWithXid(String xid) {
        Log.e(TAG, "making Get Sleep Ticks api call ...");
        ApiManager.getRestApiInterface().getSleepPhases(
                UpPlatformSdkConstants.API_VERSION_STRING,
                xid,
                sleepPhaseCallbackListener);
    }

    /**
     * Callback method for the sleep phases from UP. The method also enters the sleep event into the sleep diary.
     */
    private Callback sleepPhaseCallbackListener = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            Log.e(TAG,  "api call successful, json output: " + o.toString());
            LinkedTreeMap obj = (LinkedTreeMap) o;

            String url = response.getUrl();
            int xidParsingStart = url.lastIndexOf("/sleeps/") + 8;
            int xidParsingStop = url.indexOf("/ticks");
            String xid = xidParsingStart != -1 && xidParsingStop != -1 ? url.substring(xidParsingStart, xidParsingStop) : "unknownID";

            //Sorting out the relevant data from the call.
            ArrayList<LinkedTreeMap> array = (ArrayList<LinkedTreeMap>)((LinkedTreeMap)obj.get("data")).get("items");
            //Collecting and compiling the information from the different phases from UP.
            List<Sleep> newSleepPhases = new ArrayList<>();
            double lightTime = 0.0;
            double deepTime = 0.0;
            double awakeTime = 0.0;
            double timeInBed = 0.0;
            double totalSleep = 0.0;
            int nbrOfWakeups = 0;
            for(int i = 0; i < array.size()-1; i++) {
                //Connecting the current phase's start with the next phase's start to connect between what points
                //of time the current phase is active.
                LinkedTreeMap ltm = array.get(i);
                LinkedTreeMap ltmplus1 = array.get(i+1);

                Long ltmTime = Double.valueOf((ltm.get("time").toString())).longValue()*1000;
                Long ltmplus1Time = Double.valueOf((ltmplus1.get("time").toString())).longValue()*1000;
                Long timeDiff = ltmplus1Time - ltmTime;

                //Sorting what phase depth the current phase is in
                String depth = ltm.get("depth").toString();
                Sleep.SleepState sleepState;
                switch(depth) {
                    case "2.0":
                        sleepState = Sleep.SleepState.LIGHT;
                        lightTime += timeDiff;
                        totalSleep += timeDiff;
                        break;
                    case "3.0":
                        sleepState = Sleep.SleepState.DEEP;
                        deepTime += timeDiff;
                        totalSleep += timeDiff;
                        break;
                    default:
                        sleepState = Sleep.SleepState.AWAKE;
                        awakeTime += timeDiff;
                        nbrOfWakeups++;
                }

                timeInBed += timeDiff;

                //Adding the sleep to a sleep event list
                newSleepPhases.add(
                        new Sleep(
                                new Date(ltmTime),
                                new Date(ltmplus1Time),
                                sleepState)
                );
            }

            Date completedDate = array.isEmpty() ? new Date() : new Date(Double.valueOf(array.get(array.size()-1).get("time").toString()).longValue()*1000);

            //Adding the activity of the sleep event to the diary.
            diary.addActivity(new SleepActivity(xid, newSleepPhases, completedDate, lightTime, deepTime, awakeTime, totalSleep, timeInBed, nbrOfWakeups));

            //If the event entered into the diary was the date of today the graph is updated.
            if(sdfShowDay.format(completedDate).equals(sdfShowDay.format(activeCalendar.getTime()))) {
                updateGraphSeries(0);
            }
            Log.e(TAG, completedDate.toString());
        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.e(TAG,  "api call failed, error message: " + retrofitError.getMessage());
            Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    /**
     * Method used to fetch the graph connected with a certain ID
     * @param xid is the ID the graph is connected with
     */
    private void fetchSleepGraphFromUPWithXid(String xid) {
        Log.e(TAG, "making Get Sleep Graph api call ...");
        ApiManager.getRestApiInterface().getSleepGraph(
                UpPlatformSdkConstants.API_VERSION_STRING,
                xid,
                sleepGraphCallbackListener);
    }

    /**
     * Callback function for fetching the graph image from UP.
     */
    private Callback sleepGraphCallbackListener = new Callback<Object>() {
        @Override
        public void success(Object o, Response response) {
            Log.e(TAG,  "api call successful, json output: " + o.toString());

        }

        @Override
        public void failure(RetrofitError retrofitError) {
            Log.e(TAG,  "api call failed, error message: " + retrofitError.getMessage());
            Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    /**
     * Creating the query hashmap for the sleep event list call to UP.
     * @return a query hashmap.
     */
    private static HashMap<String, Integer> getSleepEventsListRequestParams() {
        HashMap<String, Integer> queryHashMap = new HashMap<String, Integer>();
        return queryHashMap;
    }



    /**
     * Updates the graph with and earlier sleep date
     * @param view Not used.
     */
    public void gotoEarlierSleepDate(View view){
        updateGraphSeries(-1);
    }

    /**
     * Updates the graph with and later sleep date
     * @param view Not used.
     */
    public void gotoLaterSleepDate(View view){
        if(!sdfShowDay.format(Calendar.getInstance().getTime()).equals(sdfShowDay.format(currentCalendar.getTime()))) {
            updateGraphSeries(1);
        }
    }

    /**
     * Method used to update the graph with the relevant date depending on an offset to the current graph's date
     * @param offset is the offset to the current date shown in the graph.
     */
    private void updateGraphSeries(int offset) {
        currentCalendar.set(Calendar.DAY_OF_MONTH, currentCalendar.get(Calendar.DAY_OF_MONTH) + offset);
        Date newDate = currentCalendar.getTime();

        List<DataPoint[]> updatedSleep = fetchDataPoints(newDate);
        lightSleepSeries.resetData(updatedSleep.get(0));
        deepSleepSeries.resetData(updatedSleep.get(1));
        awakeSleepSeries.resetData(updatedSleep.get(2));
        manualSleepSeries.resetData(updatedSleep.get(3));
        coverSleepSeries.resetData(updatedSleep.get(4));

        setGraphXBounds(updatedSleep.get(4), (GraphView) findViewById(R.id.graph));

        updateInformationDisplay();
    }

    /**
     * Method used to update the information display connected to the graph.
     * The date to be used is whatever date is currently used with showing the graph.
     */
    private void updateInformationDisplay() {
        Date newDate = currentCalendar.getTime();

        if(sdfShowDay.format(Calendar.getInstance().getTime()).equals(sdfShowDay.format(newDate))) {
            findViewById(R.id.nextDayButton).setVisibility(View.INVISIBLE);
        }else {
            findViewById(R.id.nextDayButton).setVisibility(View.VISIBLE);
        }

        String newDateSDFDay = sdfShowDay.format(newDate);
        if(newDateSDFDay.equals(sdfShowDay.format(Calendar.getInstance().getTime()))) {
            //TODO change setText to use strings.xml
            ((TextView) findViewById(R.id.textDay)).setText("Idag");
        }else {
            ((TextView) findViewById(R.id.textDay)).setText(newDateSDFDay);
        }

        List<IDiaryActivity> activities = diary.getActivitiesFromDate(newDate);
        double lightTime = 0.0;
        double deepTime = 0.0;
        double awakeTime = 0.0;
        double totalSleep = 0.0;
        double timeInBed = 0.0;
        int nbrOfWakups = 0;
        for(int i=0; i<activities.size(); i++) {
            SleepActivity sa = (SleepActivity) activities.get(i);
            lightTime += sa.getLightTime()/3600000.0;
            deepTime += sa.getDeepTime()/3600000.0;
            awakeTime += sa.getAwakeTime()/3600000.0;
            totalSleep += sa.getTotalSleep()/3600000.0;
            timeInBed += sa.getTimeInBed()/3600000.0;
            nbrOfWakups += sa.getNbrOfWakups();
        }

        ((TextView) findViewById(R.id.displayLightSleep)).setText("" + (int)lightTime + "h" + Math.round((lightTime - (int)lightTime)*60.0) + "min");
        ((TextView) findViewById(R.id.displayDeepSleep)).setText("" + (int)deepTime + "h" + Math.round((deepTime - (int)deepTime)*60.0) + "min");
        ((TextView) findViewById(R.id.displayAwakeSleep)).setText("" + (int)awakeTime + "h" + Math.round((awakeTime - (int)awakeTime)*60.0) + "min");
        ((TextView) findViewById(R.id.displayTimeInBedSleep)).setText("" + (int)timeInBed + "h" + Math.round((timeInBed - (int)timeInBed)*60.0) + "min");
        ((TextView) findViewById(R.id.displayTotalSleep)).setText("" + (int)totalSleep + "h" + Math.round((totalSleep - (int)totalSleep)*60.0) + "min");
        ((TextView) findViewById(R.id.displayNbrOfWakeupsSleep)).setText("" + nbrOfWakups + " gånger");
    }

    /**
     * Method used to get the activity context
     * @return the context of the current activity
     */
    public Context getActivity() {
        return this;
    }

    /**
     * Method used to fetch the data points from the sleep diary.
     * @param date
     * @return
     */
    private List<DataPoint[]> fetchDataPoints(Date date) {
        List<DataPoint> lightSleep = new ArrayList<>();
        List<DataPoint> deepSleep = new ArrayList<>();
        List<DataPoint> coverSleep = new ArrayList<>();
        List<DataPoint> awakeSleep = new ArrayList<>();
        List<DataPoint> manualSleep = new ArrayList<>();

        List<Sleep> sleepList = diary.getSleepListFromDate(date);
        if(!sleepList.isEmpty()) {
            coverSleep.add(new DataPoint(sleepList.get(0).getStartTime().getTime(), 0));


            for (int i = 0; i < sleepList.size(); i++) {
                Sleep sleep = sleepList.get(i);
                List<DataPoint> addList;

                if (sleep.getSleepState() == Sleep.SleepState.LIGHT) {
                    addList = lightSleep;
                } else if (sleep.getSleepState() == Sleep.SleepState.DEEP) {
                    addList = deepSleep;
                } else if (sleep.getSleepState() == Sleep.SleepState.AWAKE) {
                    addList = awakeSleep;
                }else{
                    addList = manualSleep;
                }



                Date startTime = sleep.getStartTime();
                Date stopTime = sleep.getStopTime();

                addList.add(new DataPoint(startTime.getTime(), 0));
                addList.add(new DataPoint(startTime.getTime(), sleep.getSleepLevel()));
                addList.add(new DataPoint(stopTime.getTime(), sleep.getSleepLevel()));
                addList.add(new DataPoint(stopTime.getTime(), 0));

            }

            coverSleep.add(new DataPoint(sleepList.get(sleepList.size() - 1).getStopTime().getTime(), 0));
        }

        //If any list is found empty we enter empty data to show an empty graph
        if(coverSleep.isEmpty() || lightSleep.isEmpty() || deepSleep.isEmpty() || awakeSleep.isEmpty() || manualSleep.isEmpty()) {
            Calendar dpCal = Calendar.getInstance();
            dpCal.setTime(date);

            if (lightSleep.isEmpty()) {
                lightSleep.add(new DataPoint(dpCal.getTime(), -1));
                dpCal.set(Calendar.MINUTE, dpCal.get(Calendar.MINUTE) + 1);
                lightSleep.add(new DataPoint(dpCal.getTime(), -1));
            }

            if (deepSleep.isEmpty()) {
                deepSleep.add(new DataPoint(dpCal.getTime(), -1));
                dpCal.set(Calendar.MINUTE, dpCal.get(Calendar.MINUTE) + 1);
                deepSleep.add(new DataPoint(dpCal.getTime(), -1));
            }

            if (awakeSleep.isEmpty()) {
                awakeSleep.add(new DataPoint(dpCal.getTime(), -1));
                dpCal.set(Calendar.MINUTE, dpCal.get(Calendar.MINUTE) + 1);
                awakeSleep.add(new DataPoint(dpCal.getTime(), -1));
            }

            if (manualSleep.isEmpty()) {
                manualSleep.add(new DataPoint(dpCal.getTime(), -1));
                dpCal.set(Calendar.MINUTE, dpCal.get(Calendar.MINUTE) + 1);
                manualSleep.add(new DataPoint(dpCal.getTime(), -1));
            }

            if (coverSleep.isEmpty()) {
                dpCal.set(Calendar.HOUR_OF_DAY, 0);
                coverSleep.add(new DataPoint(dpCal.getTime(), 0));
                dpCal.set(Calendar.HOUR_OF_DAY, 24);
                coverSleep.add(new DataPoint(dpCal.getTime(), 0));
            }
        }

        List<DataPoint[]> dataPointsList = new ArrayList<>();
        dataPointsList.add(lightSleep.toArray(new DataPoint[]{}));
        dataPointsList.add(deepSleep.toArray(new DataPoint[]{}));
        dataPointsList.add(awakeSleep.toArray(new DataPoint[]{}));
        dataPointsList.add(manualSleep.toArray(new DataPoint[]{}));
        dataPointsList.add(coverSleep.toArray(new DataPoint[]{}));

        return new ArrayList<>(dataPointsList);
    }

    private void setGraphXBounds(DataPoint[] list, GraphView graph) {
        if(list.length == 2) {
            // set manual X bounds
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(list[0].getX());
            graph.getViewport().setMaxX(list[1].getX());
        }
    }

    private void setupGraph() {
        //TODO change to not account for specific times i.e seconds and minutes
        Calendar cal = Calendar.getInstance();
        currentCalendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        Date activeDate = currentCalendar.getTime();

        //View viewGraph = findViewById(R.id.imageViewGraph);
        //DrawDiagram graphDiagram = new DrawDiagram(viewGraph.getContext());

        GraphView graph = (GraphView) findViewById(R.id.graph);
        ((TextView) findViewById(R.id.textDay)).setText("Idag");

        List<DataPoint[]> sleepList = fetchDataPoints(activeDate);
        DataPoint[] lightSleepDps = sleepList.get(0);
        DataPoint[] deepSleepDps = sleepList.get(1);
        DataPoint[] awakeSleepDps = sleepList.get(2);
        DataPoint[] manualSleepDps = sleepList.get(3);
        DataPoint[] coverSleepDps = sleepList.get(4);

        lightSleepSeries = new LineGraphSeries<>(lightSleepDps);
        deepSleepSeries = new LineGraphSeries<>(deepSleepDps);
        awakeSleepSeries = new LineGraphSeries<>(awakeSleepDps);
        manualSleepSeries = new LineGraphSeries<>(manualSleepDps);
        coverSleepSeries = new LineGraphSeries<>(coverSleepDps);

        //Add in order of lowest to highest except for cover
        graph.addSeries(awakeSleepSeries);
        graph.addSeries(lightSleepSeries);
        graph.addSeries(deepSleepSeries);
        graph.addSeries(manualSleepSeries);
        graph.addSeries(coverSleepSeries);

        //lightSleepSeries.setTitle(sdfShowDay.format(activeDate));
        setGraphXBounds(coverSleepDps, graph);



        lightSleepSeries.setColor(Color.rgb(0, 204, 204));
        lightSleepSeries.setDrawBackground(true);
        lightSleepSeries.setBackgroundColor(Color.rgb(0, 204, 204));
        lightSleepSeries.setThickness(0);

        deepSleepSeries.setColor(Color.rgb(0, 153, 153));
        deepSleepSeries.setDrawBackground(true);
        deepSleepSeries.setBackgroundColor(Color.rgb(0, 153, 153));
        deepSleepSeries.setThickness(0);

        awakeSleepSeries.setColor(Color.rgb(0, 65, 125));
        awakeSleepSeries.setDrawBackground(true);
        awakeSleepSeries.setBackgroundColor(Color.rgb(0, 65, 125));
        awakeSleepSeries.setThickness(0);

        manualSleepSeries.setColor(Color.rgb(188, 188, 153));
        manualSleepSeries.setDrawBackground(true);
        manualSleepSeries.setBackgroundColor(Color.rgb(188, 188, 153));
        manualSleepSeries.setThickness(0);

        coverSleepSeries.setColor(Color.rgb(0, 0, 0));
        coverSleepSeries.setDrawBackground(true);
        coverSleepSeries.setBackgroundColor(Color.rgb(0, 0, 0));
        coverSleepSeries.setThickness(0);


        //Y Axis bounds
        graph.getViewport().setScalable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(4);


        updateInformationDisplay();


        graph.setOnClickListener(new View.OnClickListener() {
            /**
             * Handles clicks on the graph.
             *
             * @param v The view to reference as current.
             */
            @Override
            public void onClick(View v) {

            }
        });

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // transform number to time
                    return sdfShowTime.format(new Date((long) value));
                } else {
                    if(value == 0) {
                        return "V";
                    }else if(value == 1) {
                        return "L";
                    }else if(value == 2) {
                        return "D";
                    }else {
                        return "";
                    }
                }
            }
        });
        graph.getGridLabelRenderer().setNumVerticalLabels(0);

        //TODO make a better solution than simply making grid white (Either transparent, remove it or make background white as well)
        graph.getGridLabelRenderer().setGridColor(Color.argb(0,255,255,255));
    }
}


