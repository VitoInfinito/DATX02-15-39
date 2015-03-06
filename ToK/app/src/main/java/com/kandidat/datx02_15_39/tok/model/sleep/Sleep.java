package com.kandidat.datx02_15_39.tok.model.sleep;

import java.util.Date;

/**
 * Created by tomashasselquist on 20/02/15.
 */
public class Sleep {

    Date startTime;
    Date stopTime;
    //0 is awake, 1 is light sleep, 2 is deep sleep
    int sleepLevel;


    public Sleep(Date start, Date stop) {
        startTime = start;
        stopTime = stop;
        sleepLevel = 0; //Set to awake sleep as normal
    }

    public Sleep(Date start, Date stop, int sleepLevel) {
        startTime = start;
        stopTime = stop;
        this.sleepLevel = sleepLevel;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public int getSleepLevel() { return sleepLevel; }



}
