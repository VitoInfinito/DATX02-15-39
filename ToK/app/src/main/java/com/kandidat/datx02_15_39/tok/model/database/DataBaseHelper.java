package com.kandidat.datx02_15_39.tok.model.database;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper
{
	private static String TAG = "DataBaseHelper"; // Tag just for the LogCat window
	//destination path (location) of our database on device
	private static String DB_PATH = "";
	private static String DB_NAME = "food_database.sql";// Database name
	private SQLiteDatabase mDataBase;
	private final Context mContext;

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);// 1? its Database Version
		DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		this.mContext = context;
		mContext.deleteDatabase(DB_NAME);
		mContext.openOrCreateDatabase(DB_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);

	}


	@Override
	public synchronized void close()
	{
		if(mDataBase != null)
			mDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			insertFromFile(mContext, 1, db);
		} catch (IOException e) {
			e.printStackTrace();
		}
		db.execSQL("CREATE TABLE IF NOT EXISTS testarn(Username INTEGER,Password VARCHAR);");
		db.execSQL("INSERT INTO `testarn` VALUES (1,'testar');");

	}

	public int insertFromFile(Context context, int resourceId, SQLiteDatabase db) throws IOException {
		// Reseting Counter
		int result = 0;

		// Open the resource
		InputStream insertsStream = mContext.getAssets().open("dabas.sql");
		BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));

		// Iterate through lines (assuming each insert has its own line and theres no other stuff)
		while (insertReader.ready()) {
			String insertStmt = insertReader.readLine();
			db.execSQL(insertStmt);
			result++;
		}
		insertReader.close();

		// returning number of inserted rows
		return result;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}