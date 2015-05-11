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

    private Date date;
    private final String id;

    public WorkoutActivity(String id, Workout workout) {
        this.id=id;
        workoutList = new ArrayList<Workout>();
        workoutList.add(workout);
        date = workout.getEndTime();
    }

    public WorkoutActivity(String id, Date date, Workout workout) {
        this.id=id;
        workoutList = new ArrayList<Workout>();
        workoutList.add(workout);
        this.date = date;
    }


    //TODO Another very wut moment. Look up later.
    public Calendar getDate(){
        return Utils.DateToCalendar(date);
    }
    public void setDate(Date d){
        date = new Date(d.getTime());
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
