package com.kandidat.datx02_15_39.tok.model.workout;

import com.kandidat.datx02_15_39.tok.model.AbstractDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by emma on 2015-02-24.
 */
public class WorkoutActivity extends AbstractDiaryActivity {

    private List<Workout> workoutList;

    private double burnedCalCount;
    private Date startTime;
    private Date stopTime;
    private final String id;

    public WorkoutActivity(String id, Workout workout) {
        this.id=id;
        workoutList = new ArrayList<Workout>();
        workoutList.add(workout);
        startTime = workout.getStartTime();
        stopTime = workout.getEndTime();
    }


//    public void setStartTime(Date d) {
//        startTime = d;
//    }
//    public void setStopTime(Date d){
//        stopTime = d;
//    }

    public Date getStartTime (){
        return this.startTime;
    }
    public Date getStopTime(){
        return this.stopTime;
    }
    private void update(){
//        burnedCalCount = 0;
//        for(Workout w : workoutList){
//            burnedCalCount += w.getCalorieBurn();
//        }
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

    public double getBurnedCalCount() {
        return this.burnedCalCount;
    }


}
