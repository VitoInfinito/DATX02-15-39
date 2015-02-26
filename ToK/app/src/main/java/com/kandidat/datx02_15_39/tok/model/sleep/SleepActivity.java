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


    @Override
    public String getID() {
        return id;
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
}
