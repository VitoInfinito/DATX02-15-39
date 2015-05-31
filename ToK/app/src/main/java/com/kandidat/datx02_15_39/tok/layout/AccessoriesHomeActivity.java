package com.kandidat.datx02_15_39.tok.layout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.jawbone.JawboneSetupActivity;
import com.kandidat.datx02_15_39.tok.model.account.Account;

/**
 * Activity class used for setting up the accessories to the application
 */
public class AccessoriesHomeActivity extends CustomActionBarActivity {
    private static final String TAG = AccessoriesHomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories_home);
        initMenu(R.layout.activity_accessories_home);

        setupUPConnectionButton();
    }

    /**
     * Method used for setting up the connection to UP button
     */
    private void setupUPConnectionButton() {
        if(Account.getInstance().isConnectedUP()) {
            ((TextView) findViewById(R.id.connectUPButton)).setBackgroundColor(Color.argb(255, 47, 107, 20));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accessories_home, menu);
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

    /**
     * Method for when clicking on the connect to UP button
     * @param view not used
     */
    public void connectUPOnClick(View view){
        startNewActivity(JawboneSetupActivity.class);
    }

    /**
     * Method for when clicking on the connect scale button
     * @param view not used
     */
    public void connectScaleOnClick(View view) {
		startNewActivity(BluetoothActivity.class);
    }

}
