package com.kandidat.datx02_15_39.tok.model.workout;

import com.kandidat.datx02_15_39.tok.model.AbstractDiary;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiary;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.Date;
import java.util.List;

/**
 * Created by emma on 2015-02-20.
 */
public class WorkoutDiary extends AbstractDiary {
    private IDiary instance;
    @Override
    public IDiary getInstance() {
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
    public List<IDiaryActivity> showDaysActivities(Date day) {
        return null;
    }

    @Override
    public List<IDiaryActivity> showWeekActivities(Date start, Date end) {
        return null;
    }

    @Override
    public void editActivity(String id, EditActivityParams eap) {

    }
}
