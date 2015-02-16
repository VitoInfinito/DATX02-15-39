package com.kandidat.datx02_15_39.tok.model;

import java.util.Date;

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
     * @param d        - The date you want to add a activity to
     * @param activity -
     */
    public void addActivity(Date d, IDiaryActivity activity);

	/**
	 * A method to get the specific Activity type of the id
	 * @param id
	 * @return A Activity with the id if such a Activity exist
	 */
    public IDiaryActivity getActivity(String id);

	/**
	 * Removes a id depending on its id
	 * @param id
	 */
    public void removeActivity(String id);

	/**
	 * Method to show the data of a days activities
	 * @param day
	 */
    public void showDaysActivities(Date day); 							// Return a Diagram maybe ?

	/**
	 * method to show the data of a weeks activities
	 * @param start
	 * @param end
	 */
    public void showWeekActivities(Date start, Date end);			// Return a Diagram maybe ?

}
