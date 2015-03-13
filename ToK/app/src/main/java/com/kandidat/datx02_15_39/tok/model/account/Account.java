package com.kandidat.datx02_15_39.tok.model.account;

import com.kandidat.datx02_15_39.tok.model.IDiary;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietDiary;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepDiary;
import com.kandidat.datx02_15_39.tok.model.weight.WeightDiary;
import com.kandidat.datx02_15_39.tok.model.workout.WorkoutDiary;

/**
 * Created by tomashasselquist on 03/03/15.
 */
public class Account {

    private String name;
    private String gender;
    private int age;

    private static Account instance;

    protected Account() {
        name = null;
        gender = null;
        age = 0;
    }

    public static Account getInstance() {
        if(instance == null){
            instance = new Account();
        }
        return instance;
    }


    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
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

    /**
     * Fetch the instance of weight diary
     * @return
     */
    public WeightDiary getWeightDiary() {
        return (WeightDiary) WeightDiary.getInstance();
    }
}
