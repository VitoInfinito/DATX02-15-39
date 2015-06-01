package com.kandidat.datx02_15_39.tok.model.database;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.kandidat.datx02_15_39.tok.model.diet.Food;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a helper class to be able to search in the databas aswell as create one if
 * it is the first time launching the app.
 */
public class DataBaseHelper extends SQLiteOpenHelper
{
	private static String TAG = "Database"; // Tag just for the LogCat window
	private static String DB_NAME = "food_database.sql";// Database name
	private static String DB_NAME_PART[] = {"food_database_vara.sql","food_database_nvarden.sql", "food_database_fpcu.sql"};
	private SQLiteDatabase mDataBase;
	private final Context mContext;

	public DataBaseHelper(Context context, int version) {
		super(context, DB_NAME, null, version);// 1? its Database Version
		this.mContext = context;
		mDataBase = mContext.openOrCreateDatabase(DB_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
	}

	/**
	 * Method to search in the database with a keyword. The search function will search for words
	 * that is close to the word text with sql statement %searchtxt% which search for items
	 * that has the search text in them
	 * @param searchtxt - The search text.
	 * @return - list of items it found.
	 */
	public List<Food> searchFoodItems(String searchtxt){
		Log.d(TAG, "Version" + getReadableDatabase().getVersion());
		ArrayList<Food> tmp = new ArrayList<>();
		Cursor cur = selectWhereLikeStatement(searchtxt, "VABENAM", "Vara");
		if(cur != null){
			boolean hasNext = cur.moveToFirst();
			while(hasNext){
				int id = cur.getInt(cur.getColumnIndex("VAIDENT"));
				Log.d(TAG, cur.getString(cur.getColumnIndex("VAIDENT")));
				Cursor cursor = selectWhereStatement(id + "", "VAIDENT", "FpCu");
				if (cursor != null) {
					if( cursor.moveToFirst()) {
						id = cursor.getInt(cursor.getColumnIndex("CUIDENT"));
						cursor = selectWhereStatement(id + "", "CUIDENT", "Nvarden");
						if (cursor != null) {
							if(cursor.moveToFirst())
								tmp.add(getFood(cursor, cur.getString(cur.getColumnIndex("VABENAM"))));
						}
					}
				}
				hasNext = cur.moveToNext();
			}
		}
		return tmp;
	}

	/**
	 * Helper method to create food items from the search items that is in the cursor that is
	 * gotten from the SQL SELECT FROM WHERE statement
	 * @param cursor - cursor that can iterate through the objects that need to be transformed to a
	 *               food object
	 * @param name - the name of the food object
	 * @return
	 */
	private Food getFood(Cursor cursor, String name){
		if(cursor != null){
			int calAm = 0, carbAm = 0, proAm = 0, fatAm= 0, amount = 100;
			Food.FoodPrefix prefix = Food.FoodPrefix.g;
			switch(cursor.getString(cursor.getColumnIndex("T5100_MATTKVALIFICERAREBASMANGD"))){
				case "Gram":
					prefix = Food.FoodPrefix.g;
					break;
				case "Milliliter":
					prefix = Food.FoodPrefix.ml;
					break;
			}
			amount = cursor.getInt(cursor.getColumnIndex("T4072_BASMANGDNARINGSDEKLARATION"));
			boolean hasNext = cursor.moveToFirst();
			while(hasNext) {
				switch (cursor.getString(cursor.getColumnIndex("NVNVKOD"))) {
					case "ENER-":
						calAm = cursor.getInt(cursor.getColumnIndex("NVMANGD"));
						break;
					case "PRO-":
						proAm = cursor.getInt(cursor.getColumnIndex("NVMANGD"));
						break;
					case "CHOAVL":
						carbAm = cursor.getInt(cursor.getColumnIndex("NVMANGD"));
						break;
					case "FAT":
						fatAm = cursor.getInt(cursor.getColumnIndex("NVMANGD"));
						break;
				}
				hasNext = cursor.moveToNext();
			}
			return new Food(calAm, proAm, fatAm, carbAm, name, "",prefix,amount);
		}else{
			return null;
		}
	}

	/**
	 * Method to help doing a SELEC WHERE FROM Statement in the database.
	 * @param whatToSelect - What search word you want to get from tables
	 * @param columnName - which column name you want the what to select to be seleted from
	 * @param fromTable - Which table to take information from
	 * @return - cursor with the return table
	 */
	private Cursor selectWhereStatement(String whatToSelect, String columnName, String fromTable){
		return getReadableDatabase().rawQuery("SELECT * FROM " + fromTable + " WHERE " + columnName + "='" + whatToSelect + "'", null);
	}
	/**
	 * Method to help doing a SELEC WHERE FROM Statement in the database. With a Limit and LIKE isteed of exakt match.
	 * @param whatToSelect - What search word you want to get from tables
	 * @param columnName - which column name you want the what to select to be seleted from
	 * @param fromTable - Which table to take information from
	 * @return - cursor with the return table
	 */
	private Cursor selectWhereLikeStatement(String whatToSelect, String columnName, String fromTable){
		return getReadableDatabase().rawQuery("SELECT * FROM " + fromTable + " WHERE " + columnName + " LIKE '%" + whatToSelect + "%' LIMIT 10", null);
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
		Log.d(TAG, "Skapar");
		try {
			insertFromFile(mContext, 1, db);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to help to reed from assest and copying from assest to the local database folder.
	 * @param context
	 * @param resourceId
	 * @param db - database that you want to execute on
	 * @return - the number of rows inserted.
	 * @throws IOException - file not exist
	 */
	public int insertFromFile(Context context, int resourceId, SQLiteDatabase db) throws IOException {
		// Reseting Counter
		int result = 0;

		//Reads from all sql files and takes out the sqlstatements from the lines and execute them
		db.execSQL("BEGIN TRANSACTION;");
		// Iterate through lines (assuming each insert has its own line and theres no other stuff)
		for(int i = 0; i < 3; i++) {
			Log.d(TAG, "" + DB_NAME_PART[i]);
			InputStream insertsStream = mContext.getAssets().open(DB_NAME_PART[i]);
			BufferedReader insertReader = new BufferedReader(new InputStreamReader(insertsStream));
			while (insertReader.ready()) {
				String insertStmt = insertReader.readLine();
				if (insertStmt != null && insertStmt.length() > 0) {
					String insertStmtEnd = insertStmt.substring(insertStmt.length() - 2, insertStmt.length());
					while (!insertStmtEnd.equals(");")) {
						if (insertReader.ready()) {
							String tmp = insertReader.readLine();
							insertStmt += tmp != null? tmp: "";
							insertStmtEnd = insertStmt.substring(insertStmt.length() - 2, insertStmt.length());
						} else {
							Log.d(TAG, "databas fel");
						}
					}
					db.execSQL(insertStmt);
					result++;
				}
			}
			insertReader.close();
		}
		db.execSQL("COMMIT;");
		// returning number of inserted rows
		return result;
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "Nedgraderar " + oldVersion + "->" + newVersion);
		while(!mContext.deleteDatabase(DB_NAME));
		mDataBase = mContext.openOrCreateDatabase(DB_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "Upgradering " + oldVersion + "->" + newVersion);
		while(!mContext.deleteDatabase(DB_NAME)){}
		mDataBase = mContext.openOrCreateDatabase(DB_NAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
	}

}