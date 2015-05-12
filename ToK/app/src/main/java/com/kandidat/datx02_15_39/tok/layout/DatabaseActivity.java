package com.kandidat.datx02_15_39.tok.layout;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.kandidat.datx02_15_39.tok.R;
import com.kandidat.datx02_15_39.tok.model.database.DataBaseHelper;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseActivity extends ActionBarActivity {

	private static String DB_PATH ;
	private static String DB_NAME = "dabas.mdb";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);
		DataBaseHelper mDbHelper = new DataBaseHelper(this);
		Cursor cur = mDbHelper.getReadableDatabase().rawQuery("SELECT * FROM vara", null);
		if(cur != null) {
			cur.moveToFirst();
			for (int i = 0; i < cur.getColumnCount(); i++){
				Log.e("Columns1", cur.getString(i) + "\n");
			}

		}
		Log.e("Columns", "Didnt work");

//		TestAdapter mDbHelper = new TestAdapter(this);
//		try {
////			mDbHelper.createDatabase();
////			mDbHelper.open();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}

//		Cursor testdata = mDbHelper.getTestData();

//		mDbHelper.close();
	}

//	try {
////			copyDataBase();
//		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
//
//		String filePath = "file:///android_asset/" +  DB_NAME;
////			AssetFileDescriptor tmmp = getAssets().openFd(DB_NAME);
////			FileReader fr = new FileReader(tmmp.getFileDescriptor());
//		Log.d("HeEJEJEJE", new File(filePath).exists() +"");
////			Log.d("HeEJEJEJE", fr.toString() + "");
//		conn = DriverManager.getConnection(
//				"jdbc:ucanaccess://" + filePath);
//		Statement s = conn.createStatement();
//		ResultSet rs = s.executeQuery("SELECT * FROM HuvudGrp");
//		String tmp = conn.getCatalog();
//		while (rs.next()) {
//			tmp += rs.getString(1) + "\n";
//		}
//		((TextView)this.findViewById(R.id.my_dream_database)).setText(tmp);
//	} catch (SQLException e) {
//		e.printStackTrace();
//	} catch (ClassNotFoundException e) {
//		e.printStackTrace();
//	}
////		catch (IOException e) {
////			e.printStackTrace();
////		}

	/**
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		Log.e("Problem", " Hit1");
		InputStream myInput = this.getAssets().open(DB_NAME);
		Log.e("Problem", " Hit1");
		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		File file = new File(outFileName);
		file.createNewFile();
		file.toURI();
		Log.e("Problem", " Hit1");
		//Open the empty db as the output stream
		OutputStream myOutput = this.openFileOutput(outFileName, this.MODE_PRIVATE);
		Log.e("Problem", " Hit1");
		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}
		Log.e("Problem", " Hit1");

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

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

//	 class TestAdapter	{
//		protected static final String TAG = "DataAdapter";
//
//		private final Context mContext;
//		private SQLiteDatabase mDb;
//		private DataBaseHelper mDbHelper;
//
//		public TestAdapter(Context context)
//		{
//			this.mContext = context;
//			mDbHelper = new DataBaseHelper(mContext);
//		}
//
//		public TestAdapter createDatabase() throws SQLException
//		{
//			try
//			{
////				mDbHelper.createDataBase();
//			}
//			catch (IOException mIOException)
//			{
//				Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
//				throw new Error("UnableToCreateDatabase");
//			}
//			return this;
//		}
//
//		public TestAdapter open() throws SQLException
//		{
//			mDbHelper.openDataBase();
//			mDbHelper.close();
//			mDb = mDbHelper.getReadableDatabase();
//			return this;
//		}
//
//		public void close()
//		{
//			mDbHelper.close();
//		}
//
//		public Cursor getTestData()
//		{
//			String sql ="SELECT * FROM testarn";
//			Log.e("Problem", " Hit1");
//			Cursor mCur = mDb.rawQuery(sql, null);
//			Log.e("Problem", " Hit1");
//			if (mCur!=null)
//			{
//				mCur.moveToNext();
//			}
//			return mCur;
//		}
//	}
}
