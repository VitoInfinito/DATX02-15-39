package com.kandidat.datx02_15_39.tok.layout;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.database.DataBaseHelper;
import com.kandidat.datx02_15_39.tok.model.diet.Food;
import com.kandidat.datx02_15_39.tok.utility.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseActivity extends ActionBarActivity {

	DatabasDisplayAdapter mDatabasAdapter;
	ListView databaselist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
		DataBaseHelper mDbHelper = new DataBaseHelper(this, Utils.DATABASE_VERSION);
		databaselist = (ListView) findViewById(R.id.databas_listView);
//		updateDatabasList(cur);
		updateDatabasListWithsearch(mDbHelper);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_database, menu);
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

	public void updateDatabasListWithsearch(DataBaseHelper mDBHandler){
//		databaselist.removeAllViewsInLayout();
		mDatabasAdapter = new DatabasDisplayAdapter(this);

		ArrayList<Food> tmp = (ArrayList<Food>) mDBHandler.searchFoodItems("Br");
		for (Food f: tmp){
			mDatabasAdapter.add(f);
		}
		if (databaselist != null) {
			databaselist.setAdapter(mDatabasAdapter);
		}
//		databaselist.setOnItemClickListener(new SearchItemClickListener());
	}

	private class DatabasDisplayAdapter extends ArrayAdapter<Food> {
		public DatabasDisplayAdapter  (Context context)
		{
			super(context,0);
		}

		public View getView (int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_item, null);

			}
			// Lookup view for data population
			TextView food_item_name = (TextView) convertView.findViewById(R.id.menu_item);
			// Populate the data into the template view using the data object
			food_item_name.setHint(getItem(position).getName() + getItem(position).getCalorieAmount());
			// Return the completed view to render on screen

			return convertView;
		}
	}
}
