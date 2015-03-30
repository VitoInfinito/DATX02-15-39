package com.kandidat.datx02_15_39.tok.model.diet;

import com.kandidat.datx02_15_39.tok.model.AbstractDiary;
import com.kandidat.datx02_15_39.tok.model.AddToActivity;
import com.kandidat.datx02_15_39.tok.model.EditActivityParams;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

	protected DietDiary(){}

	@Override
	public void addActivity(Date d, IDiaryActivity activity) {
		activity.setDate(d);
		addActivityToTable(d, activity);
	}

	@Override
	public IDiaryActivity getActivity(Calendar c, String id) {
		List<IDiaryActivity> tmp = super.getActivitiesFromTable(c.getTime());
		for (IDiaryActivity ida: tmp){
			if(ida.getID().equals(id))
				return ida;
		}
		throw new IllegalArgumentException();
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
		end.add(Calendar.DATE, 1);
		while(start.before(end)){
			List<IDiaryActivity> tmp = getActivitiesFromTable(start.getTime());
			for(IDiaryActivity ida: tmp){
				if(ida instanceof DietActivity){
					returnValue.add(ida);
				}
			}
			start.add(Calendar.DATE, 1);
		}
		return returnValue;
	}

	@Override
	public void editActivity(Calendar c, String id, EditActivityParams eap) {
		if(eap instanceof EditDietActivityParams)
			getActivity(c, id).edit(eap);
		else
			throw new IllegalArgumentException("Error");
	}

	@Override
	public void addActivity(Calendar c, String id, AddToActivity ata) {
		if(ata instanceof AddToDietActivity)
			getActivity(c,id).add(ata);
		else
			throw new IllegalArgumentException("Error");
	}

}
