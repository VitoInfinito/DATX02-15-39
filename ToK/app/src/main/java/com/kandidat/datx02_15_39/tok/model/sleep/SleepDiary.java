package com.kandidat.datx02_15_39.tok.model.sleep;

import com.kandidat.datx02_15_39.tok.model.AbstractDiary;
import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiary;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.ArrayList;
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

    public void addActivity(IDiaryActivity act) {
        addActivityToTable(act.getDate(), act);
    }

    @Override
    public IDiaryActivity getActivity(Calendar c,String id) {
        return null;
    }

    //Currently returns first element for testing
    public IDiaryActivity getActivityFromDate(Date d) {
        List<IDiaryActivity> act = getActivitiesFromTable(d);
        return !act.isEmpty() ? act.get(0) : null;
    }

    //TODO remove later on
    public List<IDiaryActivity> getActivitiesFromDate(Date d) {
        //List<IDiaryActivity> lida = getActivitiesFromTable(d);
        //return lida != null ? new ArrayList<>(lida) : null;
        return new ArrayList<>(getActivitiesFromTable(d));
    }

    public List<Sleep> getSleepListFromDate(Date d) {
        List<IDiaryActivity> activities = getActivitiesFromDate(d);
        List<Sleep> sleepList = new ArrayList<>();
        if(activities != null) {
            for(int i=0; i<activities.size(); i++) {
                List<Sleep> isl = ((SleepActivity) activities.get(i)).getSleepList();
                sleepList.addAll(isl);
            }
        }
        return sleepList;
    }

    @Override
    public void removeActivity(Calendar c,String id) {

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
    public void editActivity(Calendar c,String id, EditActivityParams eap) {

    }

	@Override
	public void addActivity(Calendar c, String id, AddToActivity ata) {

	}
}
