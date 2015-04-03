package com.kandidat.datx02_15_39.tok.model.sleep;

import java.util.Date;

/**
 * Created by tomashasselquist on 20/02/15.
 */
public class Sleep {

    public enum SleepState {
        AWAKE, LIGHT, DEEP
    }

    Date startTime;
    Date stopTime;
    //0 is awake, 1 is light sleep, 2 is deep sleep
    SleepState sleepState;


    public Sleep(Date start, Date stop) {
        startTime = start;
        stopTime = stop;
        sleepState = SleepState.AWAKE; //Set to awake sleep as normal
    }

    public Sleep(Date start, Date stop, SleepState sleepLevel) {
        startTime = start;
        stopTime = stop;
        this.sleepState = sleepLevel;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public SleepState getSleepState() { return sleepState; }

    /**
     * Giving the correct value to use when displaying the sleep in a graph
     * @return
     */
    public int getSleepLevel() {
        switch(sleepState) {
            case LIGHT:
                return 1;
            case DEEP:
                return 2;
            default:
                return 0;
        }
    }



}
