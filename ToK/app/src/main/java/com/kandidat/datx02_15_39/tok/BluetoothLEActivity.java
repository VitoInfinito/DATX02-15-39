package com.kandidat.datx02_15_39.tok;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kandidat.datx02_15_39.tok.model.diet.Food;

import java.util.ArrayList;
import java.util.UUID;


public class BluetoothActivity extends ActionBarActivity implements BluetoothAdapter.LeScanCallback {

	private static final String TAG = "BluetoothGattActivity";

	private static final String DEVICE_NAME = "HC06";

	public static int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter mBluetoothAdapter;
	private ArrayAdapter<BluetoothDevice> mBluetoothArrayAdapter;
	private BluetoothDisplayAdapter mDevicesAdapter;
	private ArrayList<BluetoothDevice> mDevices;
	public static UUID MY_UUID;
	private BluetoothGatt mConnectedGatt;
	private ListView searchResultList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);

		// Initializes Bluetooth adapter.
		final BluetoothManager bluetoothManager =
				(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		mDevices = new ArrayList<>();

		startBluetoothLE();
		updateSearchList();
	}
	public void onUpdate(View view){
		updateSearchList();
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
		//searchResultList.setOnItemClickListener(new SearchItemClickListener());
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


	private void startBluetoothLE() {


		// Ensures Bluetooth is available on the device and it is enabled. If not,
		// displays a dialog requesting user permission to enable Bluetooth.
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			return;
		}

		// Use this check to determine whether BLE is supported on the device. Then
		// you can selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		startScan();

		for (int i = 0; i < mDevices.size(); i++) {
			BluetoothDevice device = mDevices.get(i);
		}

		//mConnectedGatt = device.connectGatt(this, false, mGattCallback);

	}

	private Runnable mStopRunnable = new Runnable() {
		@Override
		public void run() {
			stopScan();
		}
	};

	private Runnable mStartRunnable = new Runnable() {
		@Override
		public void run() {
			startScan();
		}
	};

	public void ConnectToDevice(BluetoothDevice device) {
		mConnectedGatt = device.connectGatt(this, true, mGattCallback);
	}

	public void startScan() {
		mDevices.clear();
		mBluetoothAdapter.startLeScan(this);

		mHandler.postDelayed(mStopRunnable, 1000);
	}

	public void stopScan() {
		mBluetoothAdapter.stopLeScan(this);
	}

	@Override
	public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
		Log.i(TAG, "New LE Device: " + device.getName() + " @ " + rssi);
		mDevices.add(device);
		if (DEVICE_NAME.equals(device.getName())) {
			mDevices.add(device);
		}
	}

	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		private int mState = 0;

		private void reset() {
			mState = 0;
		}

		private void advance() {
			mState++;
		}

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			Log.d(TAG, "Connection State Change: "
							+ status + " -> "
					// + connectionState(newState)
			);
			if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
				gatt.discoverServices();

			} else if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_DISCONNECTED) {

			} else if (status != BluetoothGatt.GATT_SUCCESS) {
				gatt.disconnect();
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			Log.d(TAG, "Services Discovered"
							+ status
			);
			reset();
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicRead(gatt, characteristic, status);
			Log.d(TAG, "Characteristic Read: " + "" );
			mHandler.sendMessage(Message.obtain(null, MSG_WEIGHT_UPDATE, characteristic));
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			super.onCharacteristicWrite(gatt, characteristic, status);
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			super.onCharacteristicChanged(gatt, characteristic);
		}
	};

	/*
 * We have a Handler to process event results on the main thread
 */

	private static final int MSG_WEIGHT_UPDATE = 101;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			BluetoothGattCharacteristic characteristic;
			if(msg == null){
				return;
			}
			if(MSG_WEIGHT_UPDATE == msg.what){
				characteristic = (BluetoothGattCharacteristic) msg.obj;
				if (characteristic.getValue() == null) {
					Log.w(TAG, "Error obtaining weight value");
					return;
				}
				updateWeight(characteristic);
			}
		}
	};

	private void updateWeight(BluetoothGattCharacteristic characteristic) {
		Log.d(TAG, "Characteristics: " + characteristic);
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

}