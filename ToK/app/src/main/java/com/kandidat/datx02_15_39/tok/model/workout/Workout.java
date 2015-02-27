package com.kandidat.datx02_15_39.tok.model.workout;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by emma on 2015-02-24.
 */
public class Workout {
    private Date startDate;
    private Date endDate;
    private double intensity;

    public Workout(Date start, Date end, double intensity){
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

}
