package com.kandidat.datx02_15_39.tok.layout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.jawbone.JawboneSetupActivity;
import com.kandidat.datx02_15_39.tok.model.account.Account;
import com.kandidat.datx02_15_39.tok.utility.JawboneUtils;

public class AccessoriesHomeActivity extends CustomActionBarActivity {
    private static final String TAG = AccessoriesHomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories_home);
        initMenu(R.layout.activity_accessories_home);

        setupUPConnectionButton();
    }

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

    public void connectUPOnClick(View view){
        startNewActivity(JawboneSetupActivity.class);
    }

    public void connectScaleOnClick() {
		startNewActivity(BluetoothActivity.class);
    }

}
