package com.kandidat.datx02_15_39.tok.model.workout;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by emma on 2015-02-24.
 */
public class Workout {
    private Date startDate;
    private Date endDate;
    private int intensity;

    static enum workoutType{
        WORKOUT_YOGA("YOGA"),
        WORKOUT_BIKE("BIKE"),
        WORKOUT_RUN("RUN"),
        WORKOUT_SOCCER("SOCCER"),
        WORKOUT_TENNIS("TENNIS"),
        WORKOUT_SWIM("SWIM");

        private String value;

        workoutType(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }
    }


    public Workout(Date start, Date end, int intensity, workoutType type){
        this.startDate = start;
        this.endDate = end;
        this.intensity = intensity;

    }

    public Date getStartTime(){
        return this.startDate;
    }
    public Date getEndTime(){
        return this.endDate;
    }
    public int getIntensity (){
        return this.intensity;
    }
    public void setIntensity(int intensity){
        this.intensity=intensity;
    }
    public void setStartTime(Date startTime){
        this.startDate = startTime;
    }
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }
}
