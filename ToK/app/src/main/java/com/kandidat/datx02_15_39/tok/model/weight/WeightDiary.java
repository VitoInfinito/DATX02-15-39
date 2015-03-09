package com.kandidat.datx02_15_39.tok.model.weight;

import com.kandidat.datx02_15_39.tok.model.AbstractDiary;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiary;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tomasharselquist on 03/03/15.
 */
public class WeightDiary extends AbstractDiary {

    private static IDiary instance;

    protected WeightDiary() {
        super();
    }

    public static IDiary getInstance() {
        if(instance == null){
            instance = new WeightDiary();
        }
        return instance;
    }

    //Currently returns first element for testing
    public IDiaryActivity getActivityFromDate(Date d) {
        List<IDiaryActivity> act = getActivitiesFromTable(d);
        return act != null ? act.get(0) : null;
    }

    @Override
    public void addActivity(Date d, IDiaryActivity activity) {
	    List<IDiaryActivity> act = getActivitiesFromTable(d);
		if(act == null){
			addActivityToTable(d, activity);
		}else{
			act.set(0, activity);
	    }
    }

    @Override
    public IDiaryActivity getActivity(Calendar c, String id) {
        return null;
    }

	public List<IDiaryActivity> getActivitiesFromDate(Date d){
		List<IDiaryActivity> list = getActivitiesFromTable(d);
		return list != null ? new ArrayList<>(list) : null;
	}

    @Override
    public void removeActivity(Calendar c, String id) {

    }

    @Override
    public List<IDiaryActivity> showDaysActivities(Calendar day) {
        return null;
    }

    @Override
    public List<IDiaryActivity> showWeekActivities(Calendar start, Calendar end) {
        return null;
    }

    @Override
    public void editActivity(Calendar c, String id, EditActivityParams eap) {

    }
}
