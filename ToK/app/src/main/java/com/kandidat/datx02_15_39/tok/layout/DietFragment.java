package com.kandidat.datx02_15_39.tok.layout;

import android.support.v4.app.Fragment;

import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;

/**
 * Created by Lagerstedt on 2015-04-26.
 */
public abstract class DietFragment extends Fragment {

	protected DietActivity newActivity;

	protected DietActivity getDietActivity(){
		return newActivity;
	}
}
