package com.kandidat.datx02_15_39.tok.model.workout;

import com.kandidat.datx02_15_39.tok.model.AbstractDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;
import com.kandidat.datx02_15_39.tok.utility.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by emma on 2015-02-24.
 */
public class WorkoutActivity extends AbstractDiaryActivity {

    private List<Workout> workoutList;

    private Calendar calendar;
    private final String id;

    public WorkoutActivity(String id, Workout workout) {
        this.id=id;
        workoutList = new ArrayList<Workout>();
        workoutList.add(workout);
        calendar = Utils.DateToCalendar(workout.getEndTime());
    }

    public WorkoutActivity(String id, Calendar calendar, Workout workout) {
        this.id=id;
        workoutList = new ArrayList<Workout>();
        workoutList.add(workout);
        this.calendar = calendar;
    }


    /**
     * Returns the calendar belonging to the workout activity
     * @return
     */
    public Calendar getCalendar(){
        return calendar;
    }

    /**
     * Sets the calendar belonging to the activity to a new value
     * @param c is the new calendar
     */
    public void setCalendar(Calendar c){
        calendar.setTimeInMillis(c.getTimeInMillis());
    }


    @Override
    public void edit(EditActivityParams eap) {

    }

    @Override
    public void add(AddToActivity addToActivity) {
    }

    public List <Workout> getWorkoutList(){
        return workoutList;
    }

    @Override
    public String getID() {
        return id;
    }

}
