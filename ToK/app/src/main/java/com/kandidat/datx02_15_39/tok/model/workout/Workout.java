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
    private double calorieBurn;

    public Workout(Date start, Date end, double intensity, double calorieBurn){
        this.startDate = start;
        this.endDate = end;
        this.intensity = intensity;
        this.calorieBurn = calorieBurn;
    }

    public Date getStartTime(){
        return this.startDate;
    }
    public Date getEndTime(){
        return this.endDate;
    }
    public double getIntensity (){
        return this.intensity;
    }
    public double getCalorieBurn(){
        return this.calorieBurn;
    }
    public void setIntensity(double intensity){
        this.intensity=intensity;
    }
}
