package com.kandidat.datx02_15_39.tok.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.database.DataBaseHelper;
import com.kandidat.datx02_15_39.tok.model.diet.AddToDietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.Food;
import com.kandidat.datx02_15_39.tok.model.diet.Recipe;
import com.kandidat.datx02_15_39.tok.model.diet.RecipeCollection;
import com.kandidat.datx02_15_39.tok.model.diet.ScaleBluetoothAdapter;
import com.kandidat.datx02_15_39.tok.utilies.Database;
import com.kandidat.datx02_15_39.tok.utility.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Fragment tht will make it possible to search for recipes and/or food object to add to you
 * diet activity.
 * This will also make it possible to add new food objects and ned recipes to the database.
 */
public class AddDietFragment extends DietFragment{

	private int activatedObject = R.id.food_button_view_diet;
	private ListView searchResultList;
	private ArrayList<Food> searchResultFood  = new ArrayList<>();
	private SearchResultAdapter sra;
	private RecipeResultAdapter rra;
	private FragmentActivity listener;
	private ScaleBluetoothAdapter sba;
	private Thread weightUpdateThread;
	private TextView displaywieght;
	private boolean saveStateForScale = false;
	private DataBaseHelper mDbHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_add_diet, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (FragmentActivity) activity;
		getActivity().setTitle(getResources().getString(R.string.AddDietFragment));
		mDbHelper = new DataBaseHelper(activity.getApplicationContext(), Utils.DATABASE_VERSION);
	}

	/**
	* A method to initial all the listeners on buttons and search view aswell as 	the argument set
	* on the Fragment that is a Serializable object, and in this case containing a meal.
	 */
	private void init() {
		//Init listeners to button on fragment
		View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onDietSelectorClick(v);
			}
		};
		(getView().findViewById(R.id.food_button_view_diet)).setOnClickListener(listener);
		(getView().findViewById(R.id.scale_button_view_diet)).setOnClickListener(listener);
		(getView().findViewById(R.id.recipe_button_view_diet)).setOnClickListener(listener);
		((SearchView)getView().findViewById(R.id.search_field)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				searchForItems(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				return false;
			}
		});
		getView().findViewById(activatedObject).setActivated(true);
		Bundle bundle = getArguments();
		if(bundle != null){
			Object object = bundle.getSerializable(Utils.dietActivityArgument);
			if (object instanceof DietActivity) {
			newActivity = (DietActivity) object;
			}else {
				newActivity = new DietActivity(Calendar.getInstance());
			}
		}else {
			newActivity = new DietActivity(Calendar.getInstance());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		//addded to make the bluetooth connection turn off if its started
		if(sba != null) {
			if (weightUpdateThread != null) {
				weightUpdateThread.interrupt();
				while (weightUpdateThread.isInterrupted()) {}
				weightUpdateThread = null;
			}
			sba.destroy();
			sba = null;
			saveStateForScale = true;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		//Starts the bluetooth connection if we reconnect an had the scale connected earlier
		if (saveStateForScale){
			sba = new ScaleBluetoothAdapter(getView().getContext(), (CustomActionBarActivity) getActivity());
			sba.bluetoothSearch();
		}
		updateList();
	}

	/**
	* This method is a second hand to search in the Database aswell as updating the result
	* of the search
	*/
	private void searchForItems(String searchWord){
		searchResultFood = (ArrayList<Food>) mDbHelper.searchFoodItems(searchWord);
		updateList();
		((TextView)getView().findViewById(R.id.info_about_search_list)).setText("Search result for " + searchWord);
	}

	/**
	 * When pressing one of the three alternatives of adding food this method is called.
	 * The plus button is for manually adding food to you meal.
	 * The Scale button is to used to add food with automaticly adding weight, and when pressing the
	 * button it will try to connect to the the scale and recive some information
	 * if its connected or not.
	 * The Recipe button will change the list to displaying recipes that ccan be added to you meal
	 * @param view
	 */
	public void onDietSelectorClick(View view) {
		if(view instanceof ImageButton) {
			ImageButton ib = (ImageButton) view;
			int amount = ((LinearLayout) getView().findViewById(R.id.button_container)).getChildCount();
			View child;
			for (int i = 0; i < amount; i++) {
				child = ((LinearLayout) getView().findViewById(R.id.button_container)).getChildAt(i);
				if (child instanceof ImageButton) {
					child.setActivated(false);
					child.setFocusableInTouchMode(false);
				}
			}
			ib.setActivated(true);
			activatedObject = ib.getId();
			if(ib.getId() == R.id.food_button_view_diet){
				updateList();
			}else if(ib.getId() == R.id.scale_button_view_diet) {
				updateList();
				confirmScaleConnection();
			}else if (ib.getId() == R.id.recipe_button_view_diet){
				updateList();
			}
		}
	}

	/*
	Updates the list that is shown with the new food item
	 */
	private void updateSearchList(){
		searchResultList = (ListView) getView().findViewById(R.id.food_search_item_container);
		searchResultList.removeAllViewsInLayout();
		sra = new SearchResultAdapter(getView().getContext());
		for (Food f: searchResultFood){
			sra.add(f);
		}
		if(searchResultList != null){
			searchResultList.setAdapter(sra);
		}
		searchResultList.setOnItemClickListener(new SearchItemClickListener());
	}

	/**
	 * Method to update the result list view depending on you search, and depending och what
	 * button is activated the one that's used to adding food or the one for recipes
	 */
	public void updateList() {
		if(activatedObject == R.id.recipe_button_view_diet){
			updateRecipeList();
		}else{
			updateSearchList();
		}
		if(searchResultList.getFooterViewsCount() == 0) {
			View view = LayoutInflater.from(getView().getContext()).inflate(R.layout.add_button_listview, null);
			searchResultList.addFooterView(view);
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (activatedObject == R.id.recipe_button_view_diet) {
						//TODO Start creating a new Recipe
						EditRecipeFragment editRecipeFragment = new EditRecipeFragment();
						listener.onAttachFragment(editRecipeFragment);
						listener.getSupportFragmentManager().beginTransaction()
								.replace(R.id.content_frame, editRecipeFragment)
								.commit();

					} else if (activatedObject == R.id.food_button_view_diet) {
						//TODO Start creating a new Food Item
					}
				}
			});
		}
	}

	/*
	Updates the list that is shown with the new recipes
	 */
	private void updateRecipeList() {
		searchResultList = (ListView) getView().findViewById(R.id.food_search_item_container);
		searchResultList.removeAllViewsInLayout();
		rra = new RecipeResultAdapter(getView().getContext());
		for (Recipe r: RecipeCollection.getInstance().getList()){
			rra.add(r);
		}
		if(searchResultList != null){
			searchResultList.setAdapter(rra);
		}
		searchResultList.setOnItemClickListener(new SearchItemClickListener());
	}

	/**
	 * This Class is added and extends ArrayAdapter and it lets the program
	 * draw what is wanted in the list view.
	 * This is used when to display food items.
	 */
	private class SearchResultAdapter extends ArrayAdapter<Food>
	{

		public SearchResultAdapter  (Context context)
		{
			super(context,0);
		}

		public View getView (int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_food_item, null);
			}
			// Lookup view for data population
			TextView food_item_name = (TextView) convertView.findViewById(R.id.food_item_name);
			TextView food_item_calorie = (TextView) convertView.findViewById(R.id.food_calorie_amount);
			// Populate the data into the template view using the data object
			food_item_name.setHint(getItem(position).getName());
			food_item_calorie.setHint(getItem(position).getCalorieAmount() + "");
			// Return the completed view to render on screen

			return convertView;
		}
	}
	/**
	 * This Class is added and extends ArrayAdapter and it lets the program
	 * draw what is wanted in the list view.
	 * This is used when to display recipes.
	 */
	private class RecipeResultAdapter extends ArrayAdapter<Recipe>
	{

		public RecipeResultAdapter  (Context context)
		{
			super(context,0);
		}

		public View getView (int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_food_item, null);

			}
			// Lookup view for data population
			TextView food_item_name = (TextView) convertView.findViewById(R.id.food_item_name);
			TextView food_item_calorie = (TextView) convertView.findViewById(R.id.food_calorie_amount);
			// Populate the data into the template view using the data object
			food_item_name.setHint(getItem(position).getName());
			food_item_calorie.setHint(getItem(position).getCalorieCountPortion() + "kcal per portion");
			// Return the completed view to render on screen

			return convertView;
		}
	}


	/**
	 * This class is handel click event on the listview
	 */
	private class SearchItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			selectedItem(position);
		}
	}

	/**
	 * Method to handle the items on the listview that is pressed, and this is depending on if the
	 * item is a recipe or food item.
	 * Food item will only add it to a list,
	 * but if it is a recipe it will change the fragment.
	 * @param position
	 */
	private void selectedItem(int position) {
		if(getView().findViewById(R.id.recipe_button_view_diet).isActivated()){
			//TODO when we implement so we have a database and can store food
			messageToast("Recipe" + rra.getItem(position).getName());
			if(rra != null){
				Fragment currentFragement = new RecipeViewFragment();
				listener.onAttachFragment(currentFragement);
				Recipe recipe = rra.getItem(position);
				Bundle bundle = new Bundle();
				bundle.putSerializable(Utils.recipeArgument, recipe);
				currentFragement.setArguments(bundle);
				listener.getSupportFragmentManager().beginTransaction()
						.replace(R.id.content_frame, currentFragement)
						.commit();
			}
		}else if(getView().findViewById(R.id.scale_button_view_diet).isActivated()){
			//TODO Can only be made when we have connected with the scale
			showWeight(sra.getItem(position).clone());
			messageToast("Scale_button" + searchResultFood.get(position).getName());
		}else if(getView().findViewById(R.id.food_button_view_diet).isActivated()){
			newActivity.add(new AddToDietActivity(sra.getItem(position).clone()));
			messageToast("Diet_button" + searchResultFood.get(position).getName() + "Item added");
		}

	}

	@Override
	public void onStop() {
		super.onStop();
		//When the application stops we want to end the bluetooth connection if its active.
		if(sba != null) {
			if (weightUpdateThread != null) {
				weightUpdateThread.interrupt();
				while (weightUpdateThread.isInterrupted()) {}
				weightUpdateThread = null;
			}
			sba.destroy();
		}
	}

	/**
	 * Called to display a dialog where the user can choose between connect the specified
	 * bluetooth unit or a new one.
	 */
	private void confirmScaleConnection() {
		if(sba == null || !sba.isConnected()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
			builder.setTitle("Koppla vågen");
			builder.setPositiveButton("Koppla", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					sba = new ScaleBluetoothAdapter(getActivity().getApplicationContext(), (CustomActionBarActivity) getActivity());
					sba.update();
				}
			});
			builder.setNegativeButton("Sök efter våg", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					((CustomActionBarActivity) listener).startNewActivity(BluetoothActivity.class);
				}
			});
			AlertDialog dialog = builder.create();
			dialog.setCancelable(true);
			dialog.show();
		}
	}

	/**
	 * Method called when a item in the list is pressed and will show a dialog box with the weight
	 * of the scale that could be tared or saved with the selected item*
	 * @param item - the selected item*
	 */
	private void showWeight(Food item) {
		if(sba != null && sba.isConnected() ) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
			View list = getActivity().getLayoutInflater().inflate(R.layout.display_weight_from_scale, null);
			sba.startCommunicate();
			displaywieght = (TextView) list.findViewById(R.id.display_weight);
			weightUpdateThread = new Thread(new WeightRunnable(displaywieght));
			weightUpdateThread.start();
			builder.setTitle("Lägg till vikt");
			builder.setPositiveButton("Spara", new SaveWeightListener(item));
			builder.setNegativeButton("Tare", null);
			builder.setView(list);
			AlertDialog dialog = builder.create();
			dialog.setCancelable(true);
			dialog.show();
			dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new DialogListener(dialog));
		}else{
			confirmScaleConnection();
		}
	}

	/**
	 * Listener on the Tare button to not close the dialog it belongs to.
	 */
	private class DialogListener implements View.OnClickListener {

		DialogInterface dialogInterface;

		public DialogListener(DialogInterface dialog) {
			dialogInterface = dialog;
		}

		@Override
		public void onClick(View v) {
			if (sba != null){
				sba.tareScale();
			}
		}
	}

	/**
	 * A runnable class to update the weight on the dialog aswell as get the weight from the
	 * Bluetooth class
	 */
	private class WeightRunnable implements Runnable {
		Handler handler = new Handler();
		TextView weightDisplay;

		public WeightRunnable(TextView txv){
			super();
			weightDisplay = txv;
		}

		@Override
		public void run() {
			handler.postDelayed(this, 500);
			scanBluetoothResponse(weightDisplay);
		}
	}

	/**
	 * Methods used to display the weight a textview that is taken from the bluetooth adapter
	 * @param txv
	 */
	private void scanBluetoothResponse(TextView txv) {
		String tmp = "--";
		if(sba != null && !(sba.getWeight() + "").equals("-1")) {
			tmp = sba.getWeight() + " g";
		}
		(txv).setText(tmp);
	}

	/**
	 * Method to send message on the screen
	 * @param s - message
	 */
	void messageToast(String s){
		Toast.makeText(getView().getContext(), s, Toast.LENGTH_SHORT).show();
	}

	/**
	 * DialogListener to add Food object to your permanetly DietActivity
	 */
	private class SaveWeightListener implements DialogInterface.OnClickListener {

		private Food food;

		public SaveWeightListener (Food item){
			food = item.clone();
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(sba != null) {
				if(weightUpdateThread != null)
					weightUpdateThread.interrupt();
				dialog.cancel();
				if(sba.isConnected() && sba.getWeight() > 0) {
					sba.endCommunicate();
					food.setAmount(sba.getWeight());
					newActivity.add(new AddToDietActivity(food));
					sendMessage(food.getAmount() + "gram av " + food.getName() + " tillagd");
				}else{
					sendMessage("Ingen vikt upptäckt");
				}
			}
		}
	}

	/**
	 * Method to be able to send message to the user.
	 * @param s - The message
	 */
	public void sendMessage(String s){
		Toast.makeText(getView().getContext(), s, Toast.LENGTH_SHORT).show();;
	}
}
