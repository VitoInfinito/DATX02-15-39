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
//denna klass ska innehålla flera workouts ifall man gör flera saker på gymmet
    // då behövs det flera typer av träningar
    private List<Workout> workoutList;
//    private int intensity;
    private double burnedCalCount;
    private Date date;
    private final String id;

    public WorkoutActivity(String id, Workout workout) {
        this.id=id;
        workoutList = new ArrayList<Workout>();
        workoutList.add(workout);
        date = new Date();
    }


    @Override
    public void setDate(Date d) {
        date = d;
    }
    private void update(){
        burnedCalCount = 0;
        for(Workout w : workoutList){
            burnedCalCount += w.getCalorieBurn();
        }
    }

    @Override
    public Date getDate() {
        return this.date;
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
