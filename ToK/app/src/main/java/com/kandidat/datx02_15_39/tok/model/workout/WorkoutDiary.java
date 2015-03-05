package com.kandidat.datx02_15_39.tok.model.workout;

import com.kandidat.datx02_15_39.tok.model.AbstractDiary;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiary;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Emma on 2015-02-20.
 */
public class WorkoutDiary extends AbstractDiary {
    private static IDiary instance;
    private List <IDiaryActivity> list;

    protected WorkoutDiary(){
        super();
    }
    public static IDiary getInstance() {

        if(instance==null){
            instance = new WorkoutDiary();

        }
        return instance;
    }

    @Override
    public void addActivity(Date d, IDiaryActivity activity) {

        addActivityToTable(d, activity);
    }

    @Override
    public IDiaryActivity getActivity(Calendar c,String id) {

        return null;
    }

    public IDiaryActivity getActivityFromDate(Date d) {

        return getActivitiesFromTable(d).get(0);
    }

    @Override
    public void removeActivity(Calendar c,String id) {

    }

	@Override
	public List<IDiaryActivity> showDaysActivities(Calendar day) {
		return null;
	}

	@Override
	public List<IDiaryActivity> showWeekActivities(Calendar start, Calendar end) {
       // List <IDiaryActivity> tmp = super.getActivitiesFromTable();




        return null;
	}

    @Override
    public void editActivity(Calendar c,String id, EditActivityParams eap) {

    }
}
