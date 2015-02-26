package com.kandidat.datx02_15_39.tok.layout;

<<<<<<< Updated upstream
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
=======
>>>>>>> Stashed changes
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kandidat.datx02_15_39.tok.R;

public class AddDietActivity extends ActionBarActivity {



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_diet);
<<<<<<< Updated upstream
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
=======

>>>>>>> Stashed changes
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_add_diet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

    public void onToggle(View view) {
        Toast t = Toast.makeText(this, "GG", Toast.LENGTH_SHORT);
        t.show();
    }
}
