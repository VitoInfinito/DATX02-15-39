package com.kandidat.datx02_15_39.tok.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Lagerstedt on 2015-02-16.
 *
 * This Class is created as a singleton and use to be able
 * to have several diary at the same time. But only one of the
 * same kind.
 */
public interface IDiary {

	/**
     * A Method to add new activities to your diary
     *
     * @param date        - The date you want to add a activity to
     * @param activity -
     */
    public void addActivity(Calendar date, IDiaryActivity activity);

	/**
	 * A method to get the specific Activity type of the id
	 * @param id
	 * @return A Activity with the id if such a Activity exist
	 */
    public IDiaryActivity getActivity(Calendar c, String id);

	/**
	 * Removes a id depending on its id
	 * @param id
	 */
    public void removeActivity(Calendar c, String id);

	/**
	 * Method to show the data of a days activities
	 * @param day
	 */
    public List<IDiaryActivity> showDaysActivities(Calendar day);

	/**
	 * method to give you a list of a period containing activities
	 * @param start
	 * @param end
	 */
    public List<IDiaryActivity> showPeriodActivities(Calendar start, Calendar end);

	/**
	 * Gives a Weeks Activities in list from start day to end
	 * @param startDayAtWeek
	 */
	public List<IDiaryActivity> showWeekActivities(Calendar startDayAtWeek);


	/**
	 * Method used when a already created Activity needs to be changed.
	 * If the Activity does not exist this wont change anything.
	 *
	 * @param eap - Contains the Attributes you want to edit
	 */
	public void editActivity(Calendar c, String id, EditActivityParams eap);

	/**
	 * Method used when a already created Activity needs something more
	 *
	 * @param ata - Contains the Attributes you want to add
	 * @throws java.lang.IllegalArgumentException - if ata isn't of the right kind
	 */
	public void addActivity(Calendar c, String id, AddToActivity ata);
}
