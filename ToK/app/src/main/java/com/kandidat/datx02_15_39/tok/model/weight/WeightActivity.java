package com.kandidat.datx02_15_39.tok.model.weight;

import android.provider.CalendarContract;

import com.kandidat.datx02_15_39.tok.model.AbstractDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * Class used for referencing a weight to a certain date and ID
 */
public class WeightActivity extends AbstractDiaryActivity {

    private final String id;
    private Weight weight;
    private Date date;

    /**
     * Constructor used for the weight
     * @param id is the ID
     * @param weight is the weight
     * @param date is the date
     */
    public WeightActivity(String id, Weight weight, Date date) {
        this.id = id;
        this.weight = weight;
        this.date = date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setDate(cal);
    }

    /**
     * Set method used for setting a new weight to the activity
     * @param weight is the new weight
     */
	public void setWeight(Weight weight){
		this.weight = weight;
	}

    /**
     * Get method for fetching the weight
     * @return the weight in the activity
     */
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
