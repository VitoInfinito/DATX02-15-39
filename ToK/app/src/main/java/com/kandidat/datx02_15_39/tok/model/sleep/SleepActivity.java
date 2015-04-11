package com.kandidat.datx02_15_39.tok.model.sleep;

import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasherselquist on 20/02/15.
 */
public class SleepActivity implements IDiaryActivity{

    private final String id;
    private List<Sleep> sleepList;
    private Date date;

    public SleepActivity(String id, Sleep sleep) {
        this.id = id;
        sleepList = new ArrayList<>();
        sleepList.add(sleep);
        date = new Date();
    }

    public SleepActivity(String id, Sleep sleep, Date date) {
        this.id = id;
        sleepList = new ArrayList<>();
        sleepList.add(sleep);
        this.date = date;
    }

    public SleepActivity(String id, List<Sleep> sleeps, Date date) {
        this.id = id;
        sleepList = sleeps;
        this.date = date;
    }

    public List<Sleep> getSleepList() {
        return sleepList;
    }

    public Sleep getSleepThatStarts(Date date) {
        for(int i=0; i<sleepList.size(); i++) {
            if (sleepList.get(i).getStartTime().compareTo(date) == 0) {
                return sleepList.get(i);
            }
        }
        return null;
     }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void setDate(Date d) {
        date = d;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void edit(EditActivityParams eap) {

    }

    @Override
    public void add(AddToActivity addToActivity) {

    }
}
