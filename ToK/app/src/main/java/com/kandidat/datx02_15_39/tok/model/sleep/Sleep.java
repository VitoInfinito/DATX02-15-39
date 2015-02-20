package com.kandidat.datx02_15_39.tok.model.sleep;

import java.util.Date;

/**
 * Created by tomashasselquist on 20/02/15.
 */
public class Sleep {

    Date startTime;
    Date stopTime;



    public Sleep(Date start, Date stop) {
        startTime = start;
        stopTime = stop;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }



}
