package com.kandidat.datx02_15_39.tok.model.diet;

import com.kandidat.datx02_15_39.tok.model.AbstractDiary;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiary;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Lagerstedt on 2015-02-16.
 */
public class DietDiary extends AbstractDiary {

	private static DietDiary instance;

	public static DietDiary getInstance() {
		if(instance == null){
			instance = new DietDiary();
		}
		return instance;
	}

	@Override
	public void addActivity(Date d, IDiaryActivity activity) {
		activity.setDate(d);
		addActivityToTable(d, activity);
	}

	@Override
	public IDiaryActivity getActivity(Calendar c, String id) {
		return null;
	}

	@Override
	public void removeActivity(Calendar c, String id) {
		removeActivity(c.getTime(), getActivity(c, id));
	}

	@Override
	public List<IDiaryActivity> showDaysActivities(Calendar day) {
		return getActivitiesFromTable(day.getTime());
	}

	@Override
	public List<IDiaryActivity> showWeekActivities(Calendar start, Calendar end) {
		if(!start.before(end)){
			throw new IllegalArgumentException();
		}
		List<IDiaryActivity> returnValue = new ArrayList<IDiaryActivity>();
		while(start.before(end)){
			List<IDiaryActivity> tmp = getActivitiesFromTable(start.getTime());
			for(IDiaryActivity ida: tmp){
				if(ida instanceof DietActivity){
					returnValue.add((DietActivity)ida);
				}
			}
			start.add(Calendar.DATE, 1);
		}
		return returnValue;
	}

	@Override
	public void editActivity(Calendar c, String id, EditActivityParams eap) {
		getActivity(c, id).edit(eap);
	}

}
