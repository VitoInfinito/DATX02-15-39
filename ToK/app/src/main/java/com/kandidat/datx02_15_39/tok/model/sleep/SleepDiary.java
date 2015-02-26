package com.kandidat.datx02_15_39.tok.model.sleep;

import com.kandidat.datx02_15_39.tok.model.AbstractDiary;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiary;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

<<<<<<< Updated upstream
import java.util.Calendar;
=======
>>>>>>> Stashed changes
import java.util.Date;
import java.util.List;

/**
 * Created by tomashasselquist on 20/02/15.
 */
public class SleepDiary extends AbstractDiary {

    private static IDiary instance;
    private List<IDiaryActivity> activityList;

    @Override
    public IDiary getInstance() {
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

    @Override
    public void removeActivity(String id) {

    }

<<<<<<< Updated upstream
	@Override
	public List<IDiaryActivity> showDaysActivities(Calendar day) {
		return null;
	}

	@Override
	public List<IDiaryActivity> showWeekActivities(Calendar start, Calendar end) {
		return null;
	}

	@Override
=======
    @Override
    public List<IDiaryActivity> showDaysActivities(Date day) {
        return null;
    }

    @Override
    public List<IDiaryActivity> showWeekActivities(Date start, Date end) {
        return null;
    }

    @Override
>>>>>>> Stashed changes
    public void editActivity(String id, EditActivityParams eap) {

    }
}
