package com.kandidat.datx02_15_39.tok.model.sleep;

import com.kandidat.datx02_15_39.tok.model.AbstractDiary;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiary;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by tomashasselquist on 20/02/15.
 */
public class SleepDiary extends AbstractDiary {

    private static IDiary instance;
    private List<IDiaryActivity> activityList;

    protected SleepDiary() {
        super();
    }

    public static IDiary getInstance() {
        if(instance == null){
            instance = new SleepDiary();
        }
        return instance;
    }

    @Override
    public void addActivity(Date d, IDiaryActivity activity) {
	    addActivityToTable(d, activity);
    }

    @Override
    public IDiaryActivity getActivity(String id) {
        return null;
    }

    //Currently returns first element for testing
    public IDiaryActivity getActivityFromDate(Date d) {
	    return getActivitiesFromTable(d).get(0);
    }

    @Override
    public void removeActivity(String id) {

    }

	@Override
	public List<IDiaryActivity> showDaysActivities(Calendar day) {
		return getActivitiesFromTable(day.getTime());
	}

	@Override
	public List<IDiaryActivity> showWeekActivities(Calendar start, Calendar end) {
		return null;
	}

	@Override
    public void editActivity(String id, EditActivityParams eap) {

    }
}
