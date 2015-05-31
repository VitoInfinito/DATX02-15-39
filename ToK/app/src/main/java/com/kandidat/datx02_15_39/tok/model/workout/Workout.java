package com.kandidat.datx02_15_39.tok.model.workout;

import android.media.Image;
import android.widget.ImageView;

import com.kandidat.datx02_15_39.tok.R;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.transform.Source;

/**
 * Class containing all the relevant data concerning a workout
 */
public class Workout {
    private Date startDate;
    private Date endDate;
    private int intensity;
    private WorkoutType workoutType;
    private int calories;
    private int steps;

    /**
     * Enum containing the different workout types that can be used for a workout
     */
    public enum WorkoutType {
        STRENGTH, CARDIO, WALK, FLEX, SPORT, CUSTOM
    }

    /**
     * Constructor used when only intensity and workoutType of workout is needed
     * @param start is the start date
     * @param end is the end date
     * @param intensity is the intensity
     * @param workoutType is the workouttype
     */
    public Workout(Date start, Date end, int intensity, WorkoutType workoutType){
        startDate = start;
        endDate = end;
        this.intensity = intensity;
        this.workoutType = workoutType;
        this.calories = 0;
        this.steps = 0;

    }

    /**
     * Constructor for workout containing all types of information
     * @param start is the start date
     * @param end is the end date
     * @param intensity is the intensity
     * @param workoutType is the workout type
     * @param calories are the calories lost
     * @param steps are the steps taken
     */
    public Workout(Date start, Date end, int intensity, WorkoutType workoutType, int calories, int steps) {
        startDate = start;
        endDate = end;
        this.intensity = intensity;
        this.workoutType = workoutType;
        this.calories = calories;
        this.steps = steps;
    }

    /**
     * Get method for the start time of the workout
     * @return the start date
     */
    public Date getStartTime(){
        return startDate;
    }

    /**
     * Get method for the end time of the workout
     * @return the end date
     */
    public Date getEndTime(){
        return endDate;
    }

    /**
     * Get method for the intensity of the workout
     * @return an int representing the the intensity
     */
    public int getIntensity (){
        return intensity;
    }

    /**
     * Get method for the workout type of the workout
     * @return a WorkoutType enum
     */
    public WorkoutType getWorkoutType(){return workoutType; }

    /**
     * Get method to return the relevant icon depending on what workout type the workout has
     * @return
     */
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

    /**
     * Get method for the steps of the workout
     * @return an int representing the steps taken during the workout
     */
    public int getSteps() { return steps; }

    /**
     * Get method for the calories lost during the workout
     * @return an int representing the calories lost during a workout
     */
    public int getCalories() { return calories; }

    /**
     * Set method to change intensity of workout
     * @param intensity is the new intensity
     */
    public void setIntensity(int intensity){
        this.intensity=intensity;
    }

    /**
     * Set method for changing the start time of the workout
     * @param startTime is the new start time
     */
    public void setStartTime(Date startTime){
        this.startDate = startTime;
    }

    /**
     * Set method for chaning the end time of the workout
     * @param endDate is the new end time
     */
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }
}
