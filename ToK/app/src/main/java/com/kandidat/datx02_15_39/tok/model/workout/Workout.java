package com.kandidat.datx02_15_39.tok.model.workout;

import java.sql.Timestamp;

/**
 * Created by emma on 2015-02-24.
 */
public class Workout {

    private Timestamp startTime;
    private Timestamp endTime;

    public Workout(Timestamp start, Timestamp end){
        this.startTime = start;
        this.endTime = end;
    }

    public Timestamp getStartTime(){
        return this.startTime;
    }
    public Timestamp getEndTime(){
        return this.endTime;
    }

}
