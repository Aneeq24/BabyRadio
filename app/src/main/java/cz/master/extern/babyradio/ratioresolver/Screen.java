package cz.master.extern.babyradio.ratioresolver;

import android.app.Activity;
import android.util.DisplayMetrics;

public class Screen {

	/**
	 * <p>
	 * Return the screen width of device
	 * </p>
	 * 
	 * @param activity
	 *            get the reference of the activity from where you call the
	 *            function
	 * 
	 * @return width of the screen as an integer
	 */
	public static int getScreenWidth(Activity activity) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		return displaymetrics.widthPixels;

	}

	/**
	 * <p>
	 * Return the screen Height of device
	 * </p>
	 * 
	 * @param activity
	 *            get the reference of the activity from where you call the
	 *            function
	 * 
	 * @return Height of the screen as an integer
	 */
	public static int getScreenHeight(Activity activity) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		activity.getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		return displaymetrics.heightPixels;

	}

}
