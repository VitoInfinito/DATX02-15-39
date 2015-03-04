package com.kandidat.datx02_15_39.tok.layout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.kandidat.datx02_15_39.tok.R;

import java.util.Calendar;

public class ViewAddDietActivity extends CustomActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_add_diet);
		initMenu(R.layout.activity_view_add_diet);
	}

	public void onDietDateClick(View view){
		Calendar c = Calendar.getInstance();

		DatePickerDialog picker = new DatePickerDialog(this, 0, new DatePickerListener(), 1999,10,1);

		picker.show();
	}

	private class DatePickerListener implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

		}
	}

	public void onMealSelectorClick(View view){
		//Creating the instance of PopupMenu
		PopupMenu popup = new PopupMenu(this, (Button)view);
		//Inflating the Popup using xml file
		popup.getMenuInflater()
				.inflate(R.menu.popup_menu_meal, popup.getMenu());

		//registering popup with OnMenuItemClickListener
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				mealSelector(item);
				return true;
			}
		});

		popup.show(); //showing popup menu
	}

	private void mealSelector(MenuItem item){
		Toast.makeText(this, "You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_with_confirm, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.right_corner_button_confirm) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
