package com.kandidat.datx02_15_39.tok.model.workout;

import com.kandidat.datx02_15_39.tok.model.AbstractDiary;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiary;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Emma on 2015-02-20.
 */
public class WorkoutDiary extends AbstractDiary {
    private WorkoutDiary instance;

    public WorkoutDiary getInstance() {

        if(instance==null){
            instance = new WorkoutDiary();

        }
        return instance;
    }

    @Override
    public void addActivity(Date d, IDiaryActivity activity) {
    }

    @Override
    public IDiaryActivity getActivity(String id) {
        return null;
    }

    @Override
    public void removeActivity(String id) {

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
    public void editActivity(String id, EditActivityParams eap) {

    }
}
