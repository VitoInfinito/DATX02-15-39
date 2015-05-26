package com.kandidat.datx02_15_39.tok.layout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kandidat.datx02_15_39.tok.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;

public class BluetoothActivity extends CustomActionBarActivity {

	private static final String TAG = "BluetoothActivity";

	private static final String DEVICE_NAME = "HC06";

	//Bluetooth Commands
	private static final int TARE_SCALE = 10;
	private static final int START_PUSH_VALUE = 11;
	private static final int END_PUSH_VALUE = 12;
	private static final int CALIBRATE_SCALE = 13;

	public static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDisplayAdapter mDevicesAdapter;
	private ArrayList<BluetoothDevice> mDevices = new ArrayList<>();
	private ListView searchResultList;
	private static final int SUCCESS_CONNECT = 100;
	private static final int MESSAGE_READ = 101;
	private static final int CONNECTED_SEND_DATA = 102;
	private static final int CONNECTION_CHANGED = 103;
	private static final int TARE_SCALE_REQUEST = 104;
	private static final int START_SCALE_REQUEST = 105;
	private static final int END_SCALE_REQUEST = 106;
	Button button ;
	TextView updateView;

	private Handler mHandler = new Handler() {
		private ConnectedThread connectedThread;

		SynchronousQueue Queue  = new SynchronousQueue(); // Kan vara bra att testa sedan kanske ?


		@Override
		public void handleMessage(Message msg) {
			if(msg == null){
				return;
			}
			switch(msg.what){
				case SUCCESS_CONNECT:
					Log.d(TAG, "Connected to "+ (msg.obj));
					connectedThread = new ConnectedThread((BluetoothSocket) msg.obj);
					connectedThread.start();
					break;
				case MESSAGE_READ:
					if(connectedThread != null) {
						Log.d(TAG, new String((byte[]) msg.obj, 0, msg.arg1));
						//button.setText(new String((byte[])msg.obj));
						//sendToast("Svar: " + new String((byte[])msg.obj, "UTF-8")));
						updateView.setText(convertMessage(new String((byte[]) msg.obj, msg.arg2, msg.arg1)) + "");
					}
					break;
				case CONNECTED_SEND_DATA:
					break;
				case CONNECTION_CHANGED:
					break;
				case TARE_SCALE_REQUEST:
					sendOverBluetooth(TARE_SCALE);
					break;
				case START_SCALE_REQUEST:
					sendOverBluetooth(START_PUSH_VALUE);
					break;
				case END_SCALE_REQUEST:
					sendOverBluetooth(END_PUSH_VALUE);
					break;
			}
		}

		void sendOverBluetooth(int code){
			if(connectedThread != null) {
				connectedThread.write(("#" + code + "~").getBytes());
			}
		}
	};

	public void onStartClick(View v){
		mHandler.sendMessage(Message.obtain(null, START_SCALE_REQUEST));
	}

	public void onEndClick(View v){
		mHandler.sendMessage(Message.obtain(null, END_SCALE_REQUEST));
	}

	public void onTareClick(View v){
		mHandler.sendMessage(Message.obtain(null, TARE_SCALE_REQUEST));
	}

	private int convertMessage(String s) {
		s = s.replaceAll("[^0-9]", "");
		return Integer.parseInt(s);
	}

	private void sendToast(String s) {
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}

	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	private void init(){
		updateView = (TextView) findViewById(R.id.output_bluetooth);
		startBluetooth();
		button = (Button)findViewById(R.id.button5);

		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
		filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

		bluetoothSearch();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		initMenu(R.layout.activity_bluetooth);
		init();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_bluetooth, menu);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mReceiver != null){
			unregisterReceiver(mReceiver);
			stopScan();
		}
	}

	void startBluetooth(){
		this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter == null){
			//What happens if it dosent have bluetooth
		}
		if(!mBluetoothAdapter.isEnabled()){
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
	}

	void getAlreadyConnectedDevices(){
		if(!mDevices.isEmpty()){
			mDevices.clear();
		}
		// Find the devices that is paired
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a ListView
				mDevices.add(device);
			}
		}
	}

	void bluetoothSearch(){
		//Get already connected devices
		getAlreadyConnectedDevices();
		//Scans for new Devices
		new Thread(startBluetoothSearch).start();
		updateSearchList();
	}

	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// When discovery finds a device
			switch (action){
				case BluetoothDevice.ACTION_FOUND:
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// Add the name and address to an array adapter to show in a ListView
					mDevices.add(device);
					break;
				case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
					//TODO Action when bluetooth starts discovery
					sendToast("Starta");
					break;
				case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
					sendToast("DiscoveryEND");
					updateSearchList();
					break;
				case BluetoothAdapter.ACTION_STATE_CHANGED:
					sendToast("Ändrad");
					break;
			}

		}
	};

	public void onUpdate(View view){
		//
		bluetoothSearch();
	}

	private void updateSearchList(){
		searchResultList = (ListView) findViewById(R.id.bluetooth_list_discover);
		searchResultList.removeAllViewsInLayout();
		mDevicesAdapter = new BluetoothDisplayAdapter(this);
		for (BluetoothDevice d: mDevices){
			mDevicesAdapter.add(d);
		}
		if(searchResultList != null){
			searchResultList.setAdapter(mDevicesAdapter);
		}
		searchResultList.setOnItemClickListener(new SearchItemClickListener());
	}

	public class BluetoothDisplayAdapter extends ArrayAdapter<BluetoothDevice>{
		public BluetoothDisplayAdapter  (Context context)
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
			food_item_name.setHint(getItem(position).getName());
			// Return the completed view to render on screen

			return convertView;
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

		void manageConnectedSocket(BluetoothSocket bs){
			mHandler.sendMessage(Message.obtain(null, SUCCESS_CONNECT, mmSocket));
		}

		/** Will cancel an in-progress connection, and close the socket */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) { }
		}
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
			byte[] buffer  = new byte[1024];  // buffer store for the stream
			int bytes; // bytes returned from read()
			boolean finishedRead = true;
			int startPos = 0;
			int endSearch = 100;
			//new Thread(startCom).start();
			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					if(mmInStream.available() > 0) {
						//buffer  = new byte[1024];
						// Read from the InputStream
						bytes = mmInStream.read(buffer);
						for (int i = 0; i < bytes; i ++){
							if(buffer[i] == '#'){
								finishedRead = false;
								startPos = i;
								break;
							}
						}
						if(!finishedRead) {
							while(!finishedRead && endSearch > 0) {
								bytes += mmInStream.read(buffer, bytes, 1023- bytes);
								for (int i = 0; i < bytes; i++) {
									if (buffer[i] == '~') {	 											// This '~' is needed to know that its the last byte that the
										finishedRead = true;
									}
								}
								endSearch--;
							}//*/
						// Send the obtained bytes to the UI activity
							mHandler.obtainMessage(MESSAGE_READ, bytes - startPos, startPos, buffer).sendToTarget();
						}
						finishedRead = true;
						startPos = 0;
					}
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

	private Runnable startBluetoothSearch = new Runnable() {
		@Override
		public void run() {
			startScan();
		}
	};

	private Runnable endBluetoothSearch = new Runnable() {
		@Override
		public void run() {
			stopScan();
		}
	};

	private void startScan() {
		if(mBluetoothAdapter != null) {
			mBluetoothAdapter.cancelDiscovery();
			mBluetoothAdapter.startDiscovery();
		}
		mHandler.postDelayed(endBluetoothSearch, 5000);
	}

	private void stopScan() {
		if(mBluetoothAdapter != null) {
			mBluetoothAdapter.cancelDiscovery();
		}
	}

	void connectBluetooth(BluetoothDevice device){
		ConnectThread connect = new ConnectThread(device);
		connect.start();
	}

	private class SearchItemClickListener implements android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			connectBluetooth(mDevices.get(position));
		}
	}


}
