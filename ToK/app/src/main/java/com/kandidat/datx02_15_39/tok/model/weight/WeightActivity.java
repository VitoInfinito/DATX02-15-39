package com.kandidat.datx02_15_39.tok.model.weight;

import android.provider.CalendarContract;

import com.kandidat.datx02_15_39.tok.model.AbstractDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by tomashasselquist on 03/03/15.
 */
public class WeightActivity extends AbstractDiaryActivity {

    private final String id;
    private Weight weight;
    private Date date;

    public WeightActivity(String id, Weight weight, Date date) {
        this.id = id;
        this.weight = weight;
        this.date = date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setDate(cal);
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
    public void edit(EditActivityParams eap) {

    }

    @Override
    public void add(AddToActivity addToActivity) {

    }
}
