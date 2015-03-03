package com.kandidat.datx02_15_39.tok.model.weight;

import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.Date;

/**
 * Created by tomashasselquist on 03/03/15.
 */
public class WeightActivity implements IDiaryActivity {

    private final String id;
    private Weight weight;

    public WeightActivity(String id, Weight weight) {
        this.id = id;
        this.weight = weight;
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
