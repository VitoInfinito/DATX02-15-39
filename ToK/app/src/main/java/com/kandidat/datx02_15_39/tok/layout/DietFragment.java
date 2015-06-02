package com.kandidat.datx02_15_39.tok.layout;

import android.support.v4.app.Fragment;

import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;

/**
 * Classed you as a helper abstract class to hold parameters
 */
public abstract class DietFragment extends Fragment {

	protected DietActivity newActivity;

	protected DietActivity getDietActivity(){
		return newActivity;
	}
}
