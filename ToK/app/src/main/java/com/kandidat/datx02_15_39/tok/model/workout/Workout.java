package com.kandidat.datx02_15_39.tok.model.workout;

import android.media.Image;
import android.widget.ImageView;

import com.kandidat.datx02_15_39.tok.R;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.transform.Source;

/**
 * Created by emma on 2015-02-24.
 */
public class Workout {
    private Date startDate;
    private Date endDate;
    private int intensity;
    private WorkoutType workoutType;
    private int calories;
    private int steps;

    public enum WorkoutType {
        STRENGTH, CARDIO, WALK, FLEX, SPORT, CUSTOM
    }

    public Workout(Date start, Date end, int intensity, WorkoutType workoutType){
        startDate = start;
        endDate = end;
        this.intensity = intensity;
        this.workoutType = workoutType;
        this.calories = 0;
        this.steps = 0;

    }

    public Workout(Date start, Date end, int intensity, WorkoutType workoutType, int calories, int steps) {
        startDate = start;
        endDate = end;
        this.intensity = intensity;
        this.workoutType = workoutType;
        this.calories = calories;
        this.steps = steps;
    }

    public Date getStartTime(){
        return startDate;
    }
    public Date getEndTime(){
        return endDate;
    }
    public int getIntensity (){
        return intensity;
    }
    public WorkoutType getWorkoutType(){return workoutType; }
    public int getIconId(){
        switch(getWorkoutType()){
            case STRENGTH:
                return R.drawable.strength;
            case CARDIO:
            case WALK:
                return R.drawable.sprint;
            case SPORT:
                return R.drawable.soccer;
            default:
                return R.drawable.yoga_icon;
        }
    }
    public int getSteps() { return steps; }
    public int getCalories() { return calories; }
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
