package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kandidat.datx02_15_39.tok.R;

/**
 * This class is an activity that all our activities will extends to be able to implement the
 * slidable menu, Switch activities right and send toast. This class is created to not make
 * redundant code.
 */
public class CustomActionBarActivity extends ActionBarActivity{

	private ActionBarDrawerToggle mDrawerToggle;
	private String[] mPlanetTopTiles, mPlanetDiaryTiles, mPlanetSettingTiles;
	private TypedArray mDrawerIcons;
	private ListView mDrawerList;
	private MenuItemAdapter miaTop, miaDiary, miaSetting;
	private DrawerLayout mDrawerLayout;
	protected int screenWidth, screenHeight;


	/**
	 * Method to initial a activity with menu on the left hand side.
	 * This gives all buttons a listener and divides the menu item in different sections.
	 */
	protected void initMenu(){
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		this.screenHeight = dm.heightPixels;
		this.screenWidth = dm.widthPixels;
		// Set up your ActionBar
		final ActionBar actionBar = getSupportActionBar();
		//Gets all the the array containing the menu, the array contains the name of the button
		//Here we also creates the adapters that the info will be added to.
		mPlanetTopTiles = getResources().getStringArray(R.array.nav_drawer_top_items);
		mPlanetDiaryTiles = getResources().getStringArray(R.array.nav_drawer_diary_items);
		mPlanetSettingTiles = getResources().getStringArray(R.array.nav_drawer_settingsinformation_item);
		mDrawerList = (ListView) findViewById(R.id.left_drawer_list_top);
		miaTop = new MenuItemAdapter(this);
		miaDiary = new MenuItemAdapter(this);
		miaSetting = new MenuItemAdapter(this);

		//The first group of buttons in menu
		mDrawerIcons = getResources().obtainTypedArray(R.array.nav_drawer_top_icon);
		int icon;
		for(int i = 0; i < mPlanetTopTiles.length; i++){
			icon = mDrawerIcons.getResourceId(i, -1);
			if(icon != -1){
				miaTop.add(new MenuItems(mPlanetTopTiles[i], icon));
			}else{
				miaTop.add(new MenuItems(mPlanetTopTiles[i]));
			}
		}
		if(mDrawerList !=null) {
			// Set the adapter for the list view
			mDrawerList.setAdapter(miaTop);
		}
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		//The second group of buttons in menu
		mDrawerList = (ListView) findViewById(R.id.left_drawer_list_diary);
		mDrawerIcons = getResources().obtainTypedArray(R.array.nav_drawer_diary_icon);
		for(int i = 0; i < mPlanetDiaryTiles.length; i++){
			icon = mDrawerIcons.getResourceId(i, -1);
			if(icon != -1){
				miaDiary.add(new MenuItems(mPlanetDiaryTiles[i], icon));
			}else{
				miaDiary.add(new MenuItems(mPlanetDiaryTiles[i]));
			}
		}

		if(mDrawerList !=null) {
			// Set the adapter for the list view
			mDrawerList.setAdapter(miaDiary);
		}
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		//The third group of buttons in menu
		mDrawerList = (ListView) findViewById(R.id.left_drawer_list_setting);
		mDrawerIcons = getResources().obtainTypedArray(R.array.nav_drawer_settingsinformation_icon);
		for(int i = 0; i < mPlanetSettingTiles.length; i++){
			icon = mDrawerIcons.getResourceId(i, -1);
			if(icon != -1){
				miaSetting.add(new MenuItems(mPlanetSettingTiles[i], icon));
			}else{
				miaSetting.add(new MenuItems(mPlanetSettingTiles[i]));
			}
		}

		if(mDrawerList !=null) {
			// Set the adapter for the list view
			mDrawerList.setAdapter(miaSetting);
		}
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		//Enables the actionbar
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		//Added the drawerlayout that is a building stone for the swipeable menu that is located
		// to the left and can be open with a button or a swipe to the right
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

	/**
	 * The menu item click listener. This will handle the input on the menu.
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			selectItem(parent, position);
		}
	}

	/** Swaps fragments in the main content view
	 * This is a helper class for the menu item click listener that changes the Activity depending
	 * on which menu item you press.
	 * @param parent - The view that was pressed
	 * @param position - The position it has in the listview
	 */
	private void selectItem(AdapterView parent, int position) {
		mDrawerLayout.closeDrawer(GravityCompat.START);
		if(parent.getId() == R.id.left_drawer_list_top){
			switch(position){
				case 0:
					startNewActivity(MainActivity.class);
					break;
				default:
					sendToast("Var god rapportera fel, denna knapp är inte implementerad, DiaryTop");
			}
		}else if(parent.getId() == R.id.left_drawer_list_diary){
			switch(position){
				case 0:
					startNewActivity(WorkoutHomeActivity.class);
					break;
				case 1:
					startNewActivity(DietHomeActivity.class);
					break;
				case 2:
					startNewActivity(SleepHomeActivity.class);
					break;
				case 3:
					startNewActivity(WeightHomeActivity.class);
					break;
				default:
					sendToast("Var god rapportera fel, denna knapp är inte implementerad, DiaryMid");
			}
		}else if(parent.getId() == R.id.left_drawer_list_setting){
			switch(position){
				case 0:
					startNewActivity(AccountHomeActivity.class);
					break;
				case 1:
                    startNewActivity(AccessoriesHomeActivity.class);
					break;
				case 2:
					sendToast("");
					break;
				default:
					sendToast("Var god rapportera fel, denna knapp är inte implementerad, DiaryBot");
			}
		}else{
			sendToast("Borde inte hända, var god rapportera fel");
		}
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
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		/*
		This added to make the user aware of when the user leaves the application.
		 */
		if(this.getClass() == MainActivity.class) {
			new AlertDialog.Builder(this)
									.setTitle("Really Exit?")
									.setMessage("Are you sure you want to exit?")
									.setNegativeButton(android.R.string.no, null)
									.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface arg0, int arg1) {
											quitPrograme();
						}
					}).create().show();
		}else{
			super.onBackPressed();
		}
	}

	/**
	 * Method to exit the program
	 */
	private void quitPrograme(){
		this.finish();
	}

	/**
	 * This is a helper class to represent the menu items.
	 * This is needed because of the adapter that is used.
	 */
	public class MenuItems{
		private String title;
		private int icon;

		public MenuItems(){
			this("");
		}

		public MenuItems(String title){
			this(title, -1);
		}

		public MenuItems(String title, int icon){
			this.title = title;
			this.icon = icon;
		}

		public void setIcon(int icon) {
			this.icon = icon;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getIcon() {
			return icon;
		}

		public String getTitle() {
			return title;
		}
	}

	/**
	 * A class that extends adapters to be able to fill a List view with information
	 */
	public class MenuItemAdapter extends ArrayAdapter<MenuItems>
	{
		public MenuItemAdapter (Context context)
		{
			super(context,0);
		}

		public View getView (int position, View convertView, ViewGroup parent)
		{
			if (convertView == null){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_item, null);
			}
			// Lookup view for data population
			if (getItem(position).getIcon() != -1){
				ImageView icon = (ImageView) convertView.findViewById(R.id.menu_icon);
				icon.setImageResource(getItem(position).getIcon());
			}
			TextView item1 = (TextView) convertView.findViewById(R.id.menu_item);
			// Populate the data into the template view using the data object
			item1.setText(getItem(position).getTitle());
			// Return the completed view to render on screen
			return convertView;
		}
	}


	/**
	 * Helper method to handle a button selection of diet, sleep, training and weight.
	 * This is usefull when you want to make buttons with all the different types of diarys.
	 * @param view
	 */
	public void onAlertAddButtonClick(View view){
		switch(view.getId()) {
			case R.id.alert_diet_button:
				startNewActivity( AddDietActivity.class);
				break;
			case R.id.alert_sleep_button:
				startNewActivity( AddSleepActivity.class);
				break;
			case R.id.alert_training_button:
				startNewActivity( AddWorkoutActivity.class);
				break;
			case R.id.alert_weight_button:
				startNewActivity(WeightHomeActivity.class);
				break;
		}
	}

	/**
	 * Method added to not make redundant code in all activities.
	 * This will close the acticity your on and switch to the wanted activity.class,
	 * but it the new is the same as 'this.class' nothing will happen and if
	 * 'this.class' == Main.class it will only switch acitivity.
	 * @param activity - The new activity you want to switch to
	 */
	public void startNewActivity(Class<?> activity) {
		if (this.getClass() != activity) {
			startActivity(new Intent(this, activity));
			if(activity == MainActivity.class) {
				finish();
			}
		}
	}

	/**
	 * Method to not have redundant code with toasts.
	 * @param message - The message you want to display.
	 */
	public void sendToast(String message){
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}
