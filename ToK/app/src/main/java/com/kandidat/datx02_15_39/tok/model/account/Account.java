package com.kandidat.datx02_15_39.tok.model.account;

import com.kandidat.datx02_15_39.tok.model.IDiary;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietDiary;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepDiary;
import com.kandidat.datx02_15_39.tok.model.workout.WorkoutDiary;

/**
 * Created by tomashasselquist on 03/03/15.
 */
public class Account {

    private static Account instance;

    protected Account() {
        super();
    }

    public static Account getInstance() {
        if(instance == null){
            instance = new Account();
        }
        return instance;
    }

    /**
     * Fetch the instance of diet diary
     * @return
     */
    public DietDiary getDietDiary() {
        return (DietDiary) DietDiary.getInstance();
    }

    /**
     * Fetch the instance of sleep diary
     * @return
     */
    public SleepDiary getSleepDiary() {
        return (SleepDiary) SleepDiary.getInstance();
    }

    /**
     * Fetch the instance of workout diary
     * @return
     */
    public WorkoutDiary getWorkoutDiary() {
        return (WorkoutDiary) WorkoutDiary.getInstance();
    }
}
