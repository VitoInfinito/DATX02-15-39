package com.kandidat.datx02_15_39.tok.model.workout;

import android.support.annotation.NonNull;
import android.util.Log;

import com.kandidat.datx02_15_39.tok.layout.AddWorkoutActivity;
import com.kandidat.datx02_15_39.tok.model.AbstractDiary;
import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiary;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.diet.AddToDietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.utility.Utils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    public void addActivity(Date d, IDiaryActivity activity) {
        addActivityToTable(d, activity);
    }

    //TODO apparently getDate returns a calendar. Look up.
    public void addActivity(IDiaryActivity act) {
        addActivityToTable(act.getDate().getTime(), act);
    }

	@Override
	public void addActivity(Calendar date, IDiaryActivity activity) {

	}

	@Override
    public IDiaryActivity getActivity(Calendar c,String id) {
        List<IDiaryActivity> tmp = super.getActivitiesFromTable(c.getTime());
        for (IDiaryActivity ida: tmp){
            if(ida.getID().equals(id))
                return ida;
        }
        return null;
    }

    public IDiaryActivity getActivityFromDate(Date d) {

        return getActivitiesFromTable(d).get(0);
    }

    @Override
    public void removeActivity(Calendar c,String id) {
        removeActivity(c.getTime(), getActivity(c, id));
    }

	@Override
	public List<IDiaryActivity> showDaysActivities(Calendar day) {
        return getActivitiesFromTable(day.getTime());
	}

	@Override
	public List<IDiaryActivity> showPeriodActivities(Calendar start, Calendar end) {

//        if(!end.before(start)){
//            throw new IllegalArgumentException();
//        }
        List<IDiaryActivity> returnValue = new ArrayList<IDiaryActivity>();
        while(start.before(end)){
            List<IDiaryActivity> tmp = getActivitiesFromTable(start.getTime());
            for(IDiaryActivity ida: tmp){
                if(ida instanceof WorkoutActivity){
                    returnValue.add((WorkoutActivity)ida);
                }
            }
            start.add(Calendar.DATE, 1);
        }
        return returnValue;
	}

	@Override
	public List<IDiaryActivity> showWeekActivities(Calendar startDayAtWeek) {
		return null;
	}

	public List <IDiaryActivity> getList(){
        return this.list;
    }
    @Override
    public void editActivity(Calendar c,String id, EditActivityParams eap) {

    }
    public List<IDiaryActivity> getActivitiesFromDate(Date d) {

        return new ArrayList<>(getActivitiesFromTable(d));
    }
    public List<Workout> getWorkoutListFromDate(Date d) {
        List<IDiaryActivity> activities = getActivitiesFromDate(d);
        List<Workout> workoutList = new ArrayList<>();
        if(activities != null) {
            for(int i=0; i<activities.size(); i++) {
                List<Workout> isl = ((WorkoutActivity) activities.get(i)).getWorkoutList();
                workoutList.addAll(isl);
            }
        }
        Log.d("WORKOUT", workoutList.toString());
        return workoutList;
    }
	@Override
	public void addActivity(Calendar c, String id, AddToActivity ata) {

	}
}
