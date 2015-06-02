package com.kandidat.datx02_15_39.tok.model.sleep;

import java.util.Date;

/**
 * Class used for representing a sleep
 */
public class Sleep {

    /**
     * The enums used for representing a state of sleep
     */
    public enum SleepState {
        AWAKE, LIGHT, DEEP, MANUAL
    }

    Date startTime;
    Date stopTime;
    SleepState sleepState;

    /**
     * Constructor when sleep is only referenced as awake
     * @param start is the start date
     * @param stop is the stop date
     */
    public Sleep(Date start, Date stop) {
        startTime = start;
        stopTime = stop;
        sleepState = SleepState.AWAKE; //Set to awake sleep as normal
    }

    /**
     * Constructor when the sleep references a sleep state
     * @param start is the start date
     * @param stop is the stop date
     * @param sleepLevel is the sleep level
     */
    public Sleep(Date start, Date stop, SleepState sleepLevel) {
        startTime = start;
        stopTime = stop;
        this.sleepState = sleepLevel;
    }

    /**
     * Get method for the start date
     * @return the start date
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * Get method for the stop date
     * @return the stop date
     */
    public Date getStopTime() {
        return stopTime;
    }

    /**
     * Get method for returning the sleep state
     * @return the sleep state
     */
    public SleepState getSleepState() { return sleepState; }

    /**
     * Giving the correct value to use when displaying the sleep in a graph
     * @return
     */
    public int getSleepLevel() {
        switch(sleepState) {
            case LIGHT:
                return 2;
            case DEEP:
            case MANUAL:
                return 4;
            case AWAKE:
                return 1;
            default:
                return 0;
        }
    }
}
