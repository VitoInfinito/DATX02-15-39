package com.kandidat.datx02_15_39.tok.layout;



import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.diet.DietActivity;
import com.kandidat.datx02_15_39.tok.model.diet.DietDiary;
import com.kandidat.datx02_15_39.tok.model.diet.EditDietActivityParams;
import com.kandidat.datx02_15_39.tok.model.diet.Food;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AddDietActivity extends CustomActionBarActivity {

	private int activatedObject = R.id.food_button_view_diet;
	private ListView searchResultList;
	private ArrayList<Food> searchResultFood, foodItemAdded;
	private SearchResultAdapter sra;
	private DietDiary diary;
	public static String itemsList = "List";
	public static int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayAdapter mBluetoothArrayAdapter;
	public static UUID MY_UUID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_diet);
		initMenu(R.layout.activity_add_diet);
		findViewById(activatedObject).setActivated(true);
		searchResultFood = new ArrayList<Food>();
		foodItemAdded = new ArrayList<Food>();
		diary = DietDiary.getInstance();
		Calendar c = Calendar.getInstance();
		List<Food> tmp = new ArrayList<Food>();
		tmp.add(new Food(200, 300,400,500, "Gunnar", "höger lår på kyckling", Food.FoodPrefix.g, 100));
		searchResultFood.add(tmp.get(0));
		DietActivity da = new DietActivity(c);
		diary.addActivity(c.getTime(), da);
		EditDietActivityParams edap = new EditDietActivityParams(c.getTime(), tmp);
		diary.editActivity(c, da.getID(), edap);
		foodItemAdded.add(tmp.get(0));
		updateSearchList();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void startBluetooth(){
		//BLUETOOTH CONNECT
		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter == null){

		}
		if(!mBluetoothAdapter.isEnabled()){
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}

		Toast.makeText(this, "" + mBluetoothAdapter.getName(), Toast.LENGTH_SHORT).show();
		bluetoothSearch();
	}

	private void bluetoothSearch(){
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

		View add = getLayoutInflater().inflate(R.layout.bluetooth_list, null);
		AlertDialog ad = new AlertDialog.Builder(this)
				.create();
		ad.setView(add);
		ad.show();
		ListView lv = (ListView) add.findViewById(R.id.bluetooth_list_discover);
		mBluetoothArrayAdapter = new ArrayAdapter(ad.getContext(), android.R.layout.simple_list_item_1);

		if(pairedDevices.size() > 0){
			for (BluetoothDevice bd: pairedDevices){
				mBluetoothArrayAdapter.add(bd.getName() + "\n" + bd.getAddress());
				if(bd.getName().equals("Beurer KS800")){
					new ConnectThread(bd);
					Toast.makeText(this, "kontakt" + bd.describeContents(), Toast.LENGTH_SHORT).show();
				}
			}
		}
		lv.setAdapter(mBluetoothArrayAdapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//if(mBluetoothAdapter != null)
			//mBluetoothAdapter.cancelDiscovery();
	}

	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// Add the name and address to an array adapter to show in a ListView
				mBluetoothArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		}
	};

	private void connectBluetooth(){
		//TODO
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_with_moveforward, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//noinspection SimplifiableIfStatement
		if (id == R.id.right_corner_button_moveforward) {
			Intent intent = new Intent(this, ViewAddDietActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(this.itemsList, foodItemAdded);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		//This will be called to be able to see if you pressed the menu
		return super.onOptionsItemSelected(item);
	}

	private List<Food> searchForItems(String searchWord){
		ArrayList<Food> tmp = new ArrayList<Food>();
		//TODO the search
		return tmp;
	}

	/**
	 * When the barcode button is pressed the Barcode app will open and we will ask for the EAN,
	 * this will not activate the button and therefore Barcode cant give a search result.
	 * The Scale button will try to connect to the the scale and recive some information
	 * if its connected or not.
	 * All but Barcode is activatable
	 * @param view
	 */
	public void onDietSelectorClick(View view) {
		if(view instanceof ImageButton) {
			ImageButton ib = (ImageButton) view;
			if (ib.getId() == R.id.barcode_button_view_diet) {
				//TODO Change View to a Barcode app
			}else{
				if(ib.getId() == R.id.scale_button_view_diet) {
					this.startBluetooth();
				}
				int amount = ((LinearLayout) findViewById(R.id.button_container)).getChildCount();
				View child;
				for (int i = 0; i < amount; i++) {
					child = ((LinearLayout) findViewById(R.id.button_container)).getChildAt(i);
					if (child instanceof ImageButton) {
						child.setActivated(false);
						child.setFocusableInTouchMode(false);
					}
				}
				ib.setActivated(true);
			}
		}
	}

	/*
	Updates the list that is show so that new items appear
	 */
	private void updateSearchList(){
		searchResultList = (ListView) findViewById(R.id.food_search_item_container);
		searchResultList.removeAllViewsInLayout();
		sra = new SearchResultAdapter(this);
		for (Food f: searchResultFood){
			sra.add(f);
		}
		if(searchResultList != null){
			searchResultList.setAdapter(sra);
		}
		searchResultList.setOnItemClickListener(new SearchItemClickListener());
	}

	private void updateSearchList(List<Food> foodList){
		searchResultFood = new ArrayList<Food>(foodList);
		updateSearchList();
	}

	/**
	 * This Class is added and extends ArrayAdapter and it lets me draw what i want to the list item
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
	 * This class is handel all the clicks on the listview
	 */
	private class SearchItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView parent, View view, int position, long id) {
			selectedItem(position);
		}
	}

	private void selectedItem(int position) {
		if(findViewById(R.id.recipe_button_view_diet).isActivated()){
			//TODO when we implement so we have a database and can store food
			Toast.makeText(this, "Diet_button"
					+ searchResultFood.get(position).getName()
					, Toast.LENGTH_SHORT).show();
		}else if(findViewById(R.id.scale_button_view_diet).isActivated()){
			//TODO Can only be made when we have connected with the scale
			Toast.makeText(this, "Scale_button" + searchResultFood.get(position).getName(), Toast.LENGTH_SHORT).show();
		}else if(findViewById(R.id.food_button_view_diet).isActivated()){
			foodItemAdded.add(sra.getItem(position));
			Toast.makeText(this, "Diet_button"
					+ searchResultFood.get(position).getName()
					+ "Item added"
					, Toast.LENGTH_SHORT).show();
		}

	}


	//********************************************BLUETOOTH*****************************************

	private class AcceptThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;

		public AcceptThread() {
			// Use a temporary object that is later assigned to mmServerSocket,
			// because mmServerSocket is final
			BluetoothServerSocket tmp = null;
			try {
				// MY_UUID is the app's UUID string, also used by the client code
				tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Test", MY_UUID);
			} catch (IOException e) { }
			mmServerSocket = tmp;
		}

		public void run() {
			BluetoothSocket socket = null;
			// Keep listening until exception occurs or a socket is returned
			while (true) {
				try {
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					break;
				}
				// If a connection was accepted
				if (socket != null) {
					// Do work to manage the connection (in a separate thread)
					manageConnectedSocket(socket);
					try {
						mmServerSocket.close();
					} catch (IOException e) {

					}
					break;
				}
			}
		}

		/** Will cancel the listening socket, and cause the thread to finish */
		public void cancel() {
			try {
				mmServerSocket.close();
			} catch (IOException e) { }
		}
	}

	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ConnectThread(BluetoothDevice device) {
			// Use a temporary object that is later assigned to mmSocket,
			// because mmSocket is final
			BluetoothSocket tmp = null;
			mmDevice = device;

			// Get a BluetoothSocket to connect with the given BluetoothDevice
			try {
				// MY_UUID is the app's UUID string, also used by the server code
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) { }
			mmSocket = tmp;
		}

		public void run() {
			// Cancel discovery because it will slow down the connection
			mBluetoothAdapter.cancelDiscovery();

			try {
				// Connect the device through the socket. This will block
				// until it succeeds or throws an exception
				mmSocket.connect();
			} catch (IOException connectException) {
				// Unable to connect; close the socket and get out
				try {
					mmSocket.close();
				} catch (IOException closeException) { }
				return;
			}

			// Do work to manage the connection (in a separate thread)
			manageConnectedSocket(mmSocket);
		}

		/** Will cancel an in-progress connection, and close the socket */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) { }
		}
	}

	void manageConnectedSocket(BluetoothSocket bs){
		new ConnectedThread(bs);
	}

	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) { }

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[1024];  // buffer store for the stream
			int bytes; // bytes returned from read()

			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					// Read from the InputStream
					bytes = mmInStream.read(buffer);
					// Send the obtained bytes to the UI activity
					//mHandler.obtainMessage("", bytes, -1, buffer).sendToTarget();
					message();
				} catch (IOException e) {
					break;
				}
			}
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(byte[] bytes) {
			try {
				mmOutStream.write(bytes);
			} catch (IOException e) { }
		}

		/* Call this from the main activity to shutdown the connection */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) { }
		}
	}


	void message(){
		Toast.makeText(this, "Hej", Toast.LENGTH_SHORT).show();
	}
}
