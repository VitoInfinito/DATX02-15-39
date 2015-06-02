package com.kandidat.datx02_15_39.tok.model.sleep;

import com.kandidat.datx02_15_39.tok.model.AbstractDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Class containing all the data model for a sleep activity
 */
public class SleepActivity extends AbstractDiaryActivity {

    private final String id;
    private List<Sleep> sleepList;

    //In milliseconds
    private double lightTime = 0.0;
    private double deepTime = 0.0;
    private double awakeTime = 0.0;
    private double totalSleep = 0.0;
    private double timeInBed = 0.0;
    private int nbrOfWakups = 0;

    /**
     * Constructor used when only referencing a sleep and an ID
     * @param id is the id
     * @param sleep is the sleep
     */
    public SleepActivity(String id, Sleep sleep) {
        this.id = id;
        sleepList = new ArrayList<>();
        sleepList.add(sleep);
        setDate(Calendar.getInstance());
    }

    /**
     * Constructor used when a list of sleeps are given
     * @param id is the id
     * @param sleeps is the list of sleeps
     * @param date is the date
     */
    public SleepActivity(String id, List<Sleep> sleeps, Date date) {
        this.id = id;
        sleepList = sleeps;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setDate(cal);
    }

    /**
     * Constructor used when given a list of sleep and all relevant information to the sleep activity
     * @param id is the id
     * @param sleeps is the list of sleeps
     * @param date is the date of the activity
     * @param light is the amount of light sleep in milliseconds
     * @param deep is the amount of deep sleep in milliseconds
     * @param awake is the amount of awake time in milliseconds
     * @param total is the total time slept in milliseconds
     * @param inBed is the time in bed in milliseconds
     * @param wakeups is the amount of wakeups during the sleep activity
     */
    public SleepActivity(String id, List<Sleep> sleeps, Date date, double light, double deep, double awake, double total, double inBed, int wakeups) {
        this.id = id;
        sleepList = sleeps;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
        setDate(cal);

        lightTime = light;
        deepTime = deep;
        awakeTime = awake;
        totalSleep = total;
        timeInBed = inBed;
        nbrOfWakups = wakeups;
    }

    /**
     * Get method for fetching the list of sleeps
     * @return a list of sleeps
     */
    public List<Sleep> getSleepList() {
        return sleepList;
    }

    /**
     * Get method for the amount of light sleep
     * @return the amount of light sleep in milliseconds
     */
    public double getLightTime() {
        return lightTime;
    }

    /**
     * Get method for the amount of deep sleep
     * @return the amount of deep sleep in milliseconds
     */
    public double getDeepTime() {
        return deepTime;
    }

    /**
     * Get method for the amount of awake time
     * @return the amount of awake time in milliseconds
     */
    public double getAwakeTime() {
        return awakeTime;
    }

    /**
     * Get method for the amount of total sleep
     * @return the amount of total sleep in milliseconds
     */
    public double getTotalSleep() {
        return totalSleep;
    }

    /**
     * Get method for the amount of time in bed
     * @return the amount of time in bed in milliseconds
     */
    public double getTimeInBed() {
        return timeInBed;
    }

    /**
     * Get method for the number of wakeups during the activity
     * @return the number of wakeups
     */
    public int getNbrOfWakups() {
        return nbrOfWakups;
    }

    @Override
    public String getID() {
        return id;
    }


    @Override
    public void edit(EditActivityParams eap) {

    }

    @Override
    public void add(AddToActivity addToActivity) {

    }
}
