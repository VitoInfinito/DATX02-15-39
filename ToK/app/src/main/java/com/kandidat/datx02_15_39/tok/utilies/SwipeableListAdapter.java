package com.kandidat.datx02_15_39.tok.utilies;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * Created by Lagerstedt on 2015-04-21.
 */
public class SwipeableListAdapter<T> extends ArrayAdapter<T> {

	private static int swipeDistance = 90;
	Context context;
	private static View swipedItemView = null;

	public SwipeableListAdapter(Context context) {
		super(context, 0);
		this.context = context;
	}

	/**
	 * If you need a Specific swipe distance call this constructor
	 * @param context
	 * @param swipeDistance
	 */
	public SwipeableListAdapter(Context context, int swipeDistance) {
		this(context);
		this.swipeDistance = swipeDistance;
	}


	/**
	 * Method to add to the item that you want to be able to swipe on
	 * @param v - The swipable item
	 * @param position - The position that it has in the listView
	 */
	public void addSwipeDetection(Context c, View v, int position){
		v.setOnTouchListener(new ItemSwipeListener(position,
				context.getResources().getDisplayMetrics().density));
		v.setClickable(true);
	}

	private static class ItemSwipeListener implements AdapterView.OnTouchListener{

		private static final int MIN_DISTANCE = 50;
		private float downX, upX;
		private int position;
		private float scale;
		private DIRECTION direction;
		private static enum DIRECTION{
			NORTH,
			SOUTH,
			WEST,
			EAST,
			NONE;
		}

		public ItemSwipeListener(int position, float scale) {
			super();
			this.position = position;
			this.scale = scale;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					closeOtherSwipe(v);
					downX = event.getX();
					return false; // allow other events like Click to be processed
				}
				case MotionEvent.ACTION_MOVE: {
					upX = event.getX();

					float deltaX = downX - upX;

					// horizontal swipe detection
					if (Math.abs(deltaX / scale) > MIN_DISTANCE) {
						// left or right
						if (deltaX < 0 && (deltaX/scale) >(-swipeDistance)) {
							direction = DIRECTION.EAST;
							v.setLeft((96) + (int)(deltaX));
							return false;
						}
						if (deltaX > 0 && (deltaX/scale) < swipeDistance) {
							direction = DIRECTION.WEST;
							v.setLeft((int)(deltaX) *(-1));
							return false;
						}
					}
					return true;
				}
				case MotionEvent.ACTION_CANCEL:{
					setPositionOfContainer(v);
				}
				case MotionEvent.ACTION_UP:{
					setPositionOfContainer(v);
				}
				case MotionEvent.ACTION_POINTER_UP:{
					setPositionOfContainer(v);
				}
			}
			return false;
		}

		private void setPositionOfContainer(View v){
			if(direction == DIRECTION.WEST){
				v.setLeft((int) ((-swipeDistance) * scale));
			}else if(direction == DIRECTION.EAST) {
				v.setLeft(0);
			}
		}
	}

	private static void closeOtherSwipe(View v) {
		if(swipedItemView != null){
			swipedItemView.setLeft(0);
		}
		swipedItemView = v;
	}

}
