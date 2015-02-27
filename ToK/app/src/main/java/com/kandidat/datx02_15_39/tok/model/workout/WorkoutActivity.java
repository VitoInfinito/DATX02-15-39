package com.kandidat.datx02_15_39.tok.model.workout;

import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.Date;

/**
 * Created by emma on 2015-02-24.
 */
public class WorkoutActivity implements IDiaryActivity {
    private final String id;
    private Workout workout;

    public WorkoutActivity(String id, Workout workout){
        this.id = id;
        this.workout = workout;
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public void setDate(Date d) {

    }

    @Override
    public Date getDate() {
        return null;
    }

    @Override
    public void edit(EditActivityParams eap) {

    }

    @Override
    public void add(AddToActivity addToActivity) {

    }
    public Workout getWorkout(){
        return this.workout;
    }

}
