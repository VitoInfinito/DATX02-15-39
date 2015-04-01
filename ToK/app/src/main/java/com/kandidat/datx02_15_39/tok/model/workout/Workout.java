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
    private String workoutType;

    public static final String WORKOUTTYPE_STRENGTH = "STYRKA -";
    public static final String WORKOUTTYPE_CARDIO = "KONDITION -";
    public static final String WORKOUTTYPE_FLEXIBILITY = "FLEXIBILITET -";
    public static final String WORKOUTTYPE_SPORT = "SPORT -";

    public Workout(Date start, Date end, int intensity, String workoutType){
        this.startDate = start;
        this.endDate = end;
        this.intensity = intensity;
        this.workoutType = workoutType;

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
    public String getWorkoutType(){return this.workoutType; }
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
