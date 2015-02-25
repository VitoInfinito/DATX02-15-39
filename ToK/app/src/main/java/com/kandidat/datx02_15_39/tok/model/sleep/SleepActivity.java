package com.kandidat.datx02_15_39.tok.model.sleep;

import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.Date;

/**
 * Created by tomashasselquist on 20/02/15.
 */
public class SleepActivity implements IDiaryActivity{

    private final String id;
    private Sleep sleep;

    public SleepActivity(String id, Sleep sleep) {
        this.id = id;
        this.sleep = sleep;
    }

    public Sleep getSleep() {
        return sleep;
    }

    public Sleep getSleepThatStarts(Date date) {
        System.out.println("Got into getSleepThatStarts with date " + date + " and sleep starts at " + sleep.getStartTime());
        System.out.println("The compare gives " + sleep.getStartTime().compareTo(date));
        //TODO For later when we have list of sleep
       // for(int i=0; i<)
        if(sleep.getStartTime().compareTo(date) == 0){
            return sleep;
        }
        return null;
     }


    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setDate(Date d) {

    }

    @Override
    public Date getDate(Date d) {
        return null;
    }

    @Override
    public void edit(EditActivityParams eap) {

    }

    @Override
    public void add(AddToActivity addToActivity) {

    }
}
