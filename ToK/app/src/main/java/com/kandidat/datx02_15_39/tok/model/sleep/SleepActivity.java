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
 * Created by tomasherselquist on 20/02/15.
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

    public SleepActivity(String id, Sleep sleep) {
        this.id = id;
        sleepList = new ArrayList<>();
        sleepList.add(sleep);
        setDate(Calendar.getInstance());
    }

    public SleepActivity(String id, Sleep sleep, Date date) {
        this.id = id;
        sleepList = new ArrayList<>();
        sleepList.add(sleep);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
        setDate(cal);
    }

    public SleepActivity(String id, List<Sleep> sleeps, Date date) {
        this.id = id;
        sleepList = sleeps;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setDate(cal);
    }

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



    public List<Sleep> getSleepList() {
        return sleepList;
    }

    public Sleep getSleepThatStarts(Date date) {
        for(int i=0; i<sleepList.size(); i++) {
            if (sleepList.get(i).getStartTime().compareTo(date) == 0) {
                return sleepList.get(i);
            }
        }
        return null;
     }

    public double getLightTime() {
        return lightTime;
    }

    public double getDeepTime() {
        return deepTime;
    }

    public double getAwakeTime() {
        return awakeTime;
    }

    public double getTotalSleep() {
        return totalSleep;
    }

    public double getTimeInBed() {
        return timeInBed;
    }

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
