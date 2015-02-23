package com.kandidat.datx02_15_39.tok.layout;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kandidat.datx02_15_39.tok.R;

public class DietHomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_home);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
    }
}
