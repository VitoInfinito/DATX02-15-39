package com.kandidat.datx02_15_39.tok.model.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kandidat.datx02_15_39.tok.layout.SleepHomeActivity;
import com.kandidat.datx02_15_39.tok.model.IDiary;
import com.kandidat.datx02_15_39.tok.model.IDiaryActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietDiary;
import com.kandidat.datx02_15_39.tok.model.sleep.SleepDiary;
import com.kandidat.datx02_15_39.tok.model.weight.WeightDiary;
import com.kandidat.datx02_15_39.tok.model.workout.WorkoutDiary;
import com.kandidat.datx02_15_39.tok.utility.Utils;

/**
 * Singleton class referencing the account of the user
 */
public class Account {

    private String name;
    private String gender;
    private int age;

    private boolean connectedUP;
    private Class nextClassCallback;

    private static Account instance;

    protected Account() {
        name = null;
        gender = null;
        age = 0;
        connectedUP = false;
        nextClassCallback = null;
    }

    /**
     * Used for fetching the instane of the singleton
     * @return
     */
    public static Account getInstance() {
        if(instance == null){
            instance = new Account();
        }
        return instance;
    }

    /**
     * Get method for the name of the user
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * Get method for the gender of the user
     * @return the gender of the user
     */
    public String getGender() {
        return gender;
    }

    /**
     * Get method for the age of the user
     * @return the age of the user
     */
    public int getAge() {
        return age;
    }

    /**
     * Method to return whether or not the user is connected to UP
     * @return boolean containing information about the connection to UP
     */
    public boolean isConnectedUP() { return connectedUP; }

    /**
     * Method used when a callback to an activity class is stored. Nulls the callback class when used
     * @return the activity class to be sent to
     */
    public Class getNextClassCallback() {
        Class sendClassCallback = nextClassCallback;
        nextClassCallback = null;
        return sendClassCallback;
    }

    /**
     * Set method for setting a new name of the user
     * @param name is the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set method for setting a new gender for the user
     * @param gender is the new gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Set method for setting a new age of the user
     * @param age is the new age
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Set method for the connected to UP boolean
     * @param connected is the new connection state
     */
    public void setConnectedUP(boolean connected) { connectedUP = connected; }

    /**
     * Set method for setting the next callback class
     * @param nextClassCallback
     */
    public void setNextClassCallback(Class nextClassCallback) { this.nextClassCallback = nextClassCallback; }

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
