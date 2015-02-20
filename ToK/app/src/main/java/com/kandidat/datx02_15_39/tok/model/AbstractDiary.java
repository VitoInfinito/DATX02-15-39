package com.kandidat.datx02_15_39.tok.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Lagerstedt on 2015-02-16.
 */
public abstract class AbstractDiary implements IDiary {
	/*
		 * This will have the date as StringDateFormat yyyyMMdd and be a key to
		 * one days activities
		 */
    private Hashtable<String,List<IDiaryActivity>> activities;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    protected void addActivityToTable(Date d, IDiaryActivity ida){
        String key = sdf.format(d);
        List<IDiaryActivity> idl;

        if(activities.containsKey(key)) {
            idl = activities.get(key);
        }else {
            idl = new ArrayList<IDiaryActivity>();
        }

        idl.add(ida);


        //activities.put(sdf.format(d), ida);
    }

    protected void getActivityFromTable


}
