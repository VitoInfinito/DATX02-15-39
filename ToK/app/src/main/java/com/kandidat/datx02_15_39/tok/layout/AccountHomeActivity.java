package com.kandidat.datx02_15_39.tok.layout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.account.Account;
import com.kandidat.datx02_15_39.tok.utility.Utils;

/**
 * Activity class for Account home
 */
public class AccountHomeActivity extends CustomActionBarActivity {

    Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_home);
        initMenu();

        account = Account.getInstance();
        ((EditText) findViewById(R.id.accountUsername)).setHint(account.getName());
        ((EditText) findViewById(R.id.accountAge)).setHint("" + account.getAge());

        Spinner spinner = (Spinner) findViewById(R.id.accountGender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_home, menu);
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
     * Method called when wanting to save the settings into the account singleton
     * @param view used as reference to the view
     */
    public void saveAccountSettings(View view) {
        String newName = ((EditText)findViewById(R.id.accountUsername)).getText().toString();
        if(!newName.equals("")) {
            account.setName(newName);

            //Setting the shared preference of name
            SharedPreferences settings = getSharedPreferences(Utils.ACCOUNT_PREFS, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("accountName", newName);
            editor.commit();

            ((EditText) findViewById(R.id.accountUsername)).setHint(newName);
            ((EditText) findViewById(R.id.accountUsername)).setText("");
        }

        String newAgeCheck = ((EditText)findViewById(R.id.accountAge)).getText().toString();
        if(!newAgeCheck.equals("")) {
            int newAge = Integer.parseInt(newAgeCheck);
            if (newAge > 0) {
                account.setAge(newAge);
                ((EditText) findViewById(R.id.accountAge)).setHint("" + account.getAge());
                ((EditText) findViewById(R.id.accountAge)).setText("");
            }
        }

    }
}
