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
    private Date date;

    public WeightActivity(String id, Weight weight, Date date) {
        this.id = id;
        this.weight = weight;
        this.date = date;
    }

	public void setWeight(Weight weight){
		this.weight = weight;
	}

    public Weight getWeight() {
        return weight;
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
