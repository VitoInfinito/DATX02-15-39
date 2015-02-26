package com.kandidat.datx02_15_39.tok.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kandidat.datx02_15_39.tok.R;


public class MainActivity extends CustomActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
		initMenu(R.layout.activity_main_activity);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_with_add, menu);
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

    public void OnDietButtonClick(View view) {

        //kostButton = (Button) findViewById(R.id.diet_button);

        Intent dietIntent = new Intent(this, DietHomeActivity.class);

        startActivity(dietIntent);
    }

    /**
     * Navigates to sleep activity overview.
     *
     * @param view Not used.
     */
    public void OnSleepButtonClick(View view) {
        startActivity(new Intent(this, SleepHomeActivity.class));
    }
}
