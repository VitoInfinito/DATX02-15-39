package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kandidat.datx02_15_39.tok.R;

/**
 * Created by Lagerstedt on 2015-02-24.
 */
public class CustomActionBarActivity extends ActionBarActivity{

	private ActionBarDrawerToggle mDrawerToggle;
	private String mTitle="";
	private String mDrawerTitle="hello";
	private String[] mPlanetTopTiles, mPlanetDiaryTiles, mPlanetSettingTiles;
	protected DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private MenuItemAdapter miaTop, miaDiary, miaSetting;

	protected void initMenu(int layout){
		// Set up your ActionBar
		final ActionBar actionBar = getSupportActionBar();
		final int actionBarColor = getResources().getColor(R.color.action_bar);

		mPlanetTopTiles = getResources().getStringArray(R.array.nav_drawer_top_items);
		mPlanetDiaryTiles = getResources().getStringArray(R.array.nav_drawer_diary_items);
		mPlanetSettingTiles = getResources().getStringArray(R.array.nav_drawer_settingsinformation_item);
		mDrawerList = (ListView) findViewById(R.id.left_drawer_list_top);
		miaTop = new MenuItemAdapter(this);
		miaDiary = new MenuItemAdapter(this);
		miaSetting = new MenuItemAdapter(this);
		for(int i = 0; i < mPlanetTopTiles.length; i++){
			miaTop.add(new MenuItems(mPlanetTopTiles[i]));
		}//*/
		/*mia.add(new MenuItems("Home", "Hem"));

		mia.add(new MenuItems());
		mia.add(new MenuItems("Workout","Träning"));
		mia.add(new MenuItems("Diet","Kost"));
		mia.add(new MenuItems("Sleep","Sömn"));
		mia.add(new MenuItems("Weight","Vikt"));
		mia.add(new MenuItems());
		mia.add(new MenuItems("Settings","Inställningar"));
		mia.add(new MenuItems("Device","Koppla tillbehör"));
		mia.add(new MenuItems("About","Om ToK"));//*/
		if(mDrawerList !=null) {
			// Set the adapter for the list view
			mDrawerList.setAdapter(miaTop);
		}
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerList = (ListView) findViewById(R.id.left_drawer_list_diary);
		for(int i = 0; i < mPlanetDiaryTiles.length; i++){
			miaDiary.add(new MenuItems(mPlanetDiaryTiles[i]));
		}//*/
		if(mDrawerList !=null) {
			// Set the adapter for the list view
			mDrawerList.setAdapter(miaDiary);
		}
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerList = (ListView) findViewById(R.id.left_drawer_list_setting);
		for(int i = 0; i < mPlanetSettingTiles.length; i++){
			miaSetting.add(new MenuItems(mPlanetSettingTiles[i]));
		}//*/
		if(mDrawerList !=null) {
			// Set the adapter for the list view
			mDrawerList.setAdapter(miaSetting);
		}
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.string.drawer_open,  /* "open drawer" description */
				R.string.drawer_close  /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				invalidateOptionsMenu();
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}


	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			selectItem(parent, position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(AdapterView parent, int position) {
		mDrawerLayout.closeDrawer(GravityCompat.START);
		if(parent.getId() == R.id.left_drawer_list_top){
			switch(position){
				case 0:
					startActivity(new Intent(this, MainActivity.class));
					break;
				default:
					Toast.makeText(this, "Please Report this button is not implemented, TOP", Toast.LENGTH_SHORT).show();
			}
		}else if(parent.getId() == R.id.left_drawer_list_diary){
			switch(position){
				case 0:
					startActivity(new Intent(this, WorkoutHomeActivity.class));
					break;
				case 1:
					startActivity(new Intent(this, DietHomeActivity.class));
					break;
				case 2:
					startActivity(new Intent(this, SleepHomeActivity.class));
					break;
				case 3:
					//TODO
					break;
				default:
					Toast.makeText(this, "Please Report this button is not implemented, Diary", Toast.LENGTH_SHORT).show();
			}
		}else if(parent.getId() == R.id.left_drawer_list_setting){
			switch(position){
				case 0:
					//TODO
					break;
				case 1:
					//TODO
					break;
				case 2:
					//TODO
					break;
				default:
					Toast.makeText(this, "Please Report this button is not implemented, DiaryP", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(this, "Should not happen", Toast.LENGTH_LONG).show();
		}
		/*

		// Highlight the selected item, update the title, and close the drawer
		switch (mia.getItem(position).menuName) {
			case "Home":
				mDrawerLayout.closeDrawer(mDrawerList);
				startActivity(new Intent(this, MainActivity.class));
				break;
			case "Workout":
				mDrawerLayout.closeDrawer(mDrawerList);
				startActivity(new Intent(this, WorkoutHomeActivity.class));
				break;
			case "Diet":
				mDrawerLayout.closeDrawer(mDrawerList);
				startActivity(new Intent(this, DietHomeActivity.class));
				break;
			case "Sleep":
				mDrawerLayout.closeDrawer(mDrawerList);
				startActivity(new Intent(this, SleepHomeActivity.class));
				break;
			case "Weight":
				mDrawerLayout.closeDrawer(mDrawerList);
				//TODO
				break;
			case "Settings":
				mDrawerLayout.closeDrawer(mDrawerList);
				//TODO
				break;
			case "Device":
				mDrawerLayout.closeDrawer(mDrawerList);
				//TODO
				break;
			case "About":
				mDrawerLayout.closeDrawer(mDrawerList);
				//TODO
				break;
			default:
				Toast.makeText(getApplicationContext(), "Denna knappen funkare ej", Toast.LENGTH_LONG).show();
				break;
		}//*/
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if(this.getParent() == null) {
			new AlertDialog.Builder(this)
					.setTitle("Really Exit?")
					.setMessage("Are you sure you want to exit?")
					.setNegativeButton(android.R.string.no, null)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface arg0, int arg1) {
							System.exit(0);
						}
					}).create().show();
		}else{
			super.onBackPressed();
		}
	}


	public class MenuItems{
		public String menuName;				//The name (Has to be english)
		//public int inLayout;					//the description that will be printed
		//public String name;
		//public MenuItems(){this("space", "");}
		public MenuItems(String menuName/*,int inLayout//*/
						 /*String name//*/)
		{
			this.menuName = menuName;
			//this.inLayout = inLayout;
			//this.name = name;
		}
	}

	public class MenuItemAdapter extends ArrayAdapter<MenuItems>
	{
		public MenuItemAdapter (Context context)
		{
			super(context,0);
		}

		public View getView (int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				/*if(getItem(position).menuName.equals("space")){
					convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_divider, null);
				}else {
					convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_top_item, null);
				}*/
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_top_item, null);

			}

			// Lookup view for data population
			TextView item1 = (TextView) convertView.findViewById(R.id.menu_item);
			// Populate the data into the template view using the data object
			item1.setHint(getItem(position).menuName);
			// Return the completed view to render on screen

			return convertView;
		}
	}
}
