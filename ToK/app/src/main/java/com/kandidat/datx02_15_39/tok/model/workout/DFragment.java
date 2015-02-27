package com.kandidat.datx02_15_39.tok.model.workout;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kandidat.datx02_15_39.tok.R;

public class DFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_add_workout, container,
                false);
        getDialog().setTitle("AddWorkout");
        // Do something else
        return rootView;
    }
}