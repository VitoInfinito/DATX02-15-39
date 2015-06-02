package com.kandidat.datx02_15_39.tok.model.weight;

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
 * Class used as diary containing all data about weights
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


    /**
     * Fetch the weight belonging to a selected date
     * @param d is the referenced date
     * @return an activity containing a weight for the specified date
     */
    public WeightActivity getWeightFromDate(Date d) {
        List<IDiaryActivity> list = getActivitiesFromDate(d);
        return list.isEmpty() ? null : (WeightActivity) list.get(0);
    }

    /**
     * Method used to fetch the activites used on a single date
     * @param d is the referenced date
     * @return a list of activities
     */
    public List<IDiaryActivity> getActivitiesFromDate(Date d) {
        return new ArrayList<>(getActivitiesFromTable(d));
    }

    @Override
    public void addActivity(Calendar cal, IDiaryActivity activity) {
	    List<IDiaryActivity> act = getActivitiesFromTable(cal.getTime());
		if(act.isEmpty()){
			addActivityToTable(cal.getTime(), activity);
		}else{
			act.set(0, activity);
	    }
    }

    @Override
    public IDiaryActivity getActivity(Calendar c, String id) {
        return null;
    }

    @Override
    public void removeActivity(Calendar c, String id) {

    }

    @Override
    public List<IDiaryActivity> showDaysActivities(Calendar day) {
        return null;
    }

    @Override
    public List<IDiaryActivity> showPeriodActivities(Calendar start, Calendar end) {
        return null;
    }

	@Override
	public List<IDiaryActivity> showWeekActivities(Calendar startDayAtWeek) {
		return null;
	}

	@Override
    public void editActivity(Calendar c, String id, EditActivityParams eap) {

    }

	@Override
	public void addActivity(Calendar c, String id, AddToActivity ata) {

	}
}
