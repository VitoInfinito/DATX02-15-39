package com.kandidat.datx02_15_39.tok.layout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.account.Account;
import com.kandidat.datx02_15_39.tok.utility.Utils;

public class CreateUserActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
    }

    public void onClickcreateUserSignup(View view){

        String newName = ((EditText)findViewById(R.id.createUserUsername)).getText().toString();
        if(!newName.equals("")) {
            Account.getInstance().setName(newName);

            SharedPreferences settings = getSharedPreferences(Utils.ACCOUNT_PREFS, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("accountName", newName);
            editor.commit();

            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
