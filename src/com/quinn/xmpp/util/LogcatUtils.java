package com.quinn.xmpp.util;

import android.util.Log;

/**
 * @author Quinn
 * @date 2015-3-31
 */
public class LogcatUtils {
	/**
	 * if it is essential to print bug message. I can initial this value in the
	 * launch activity
	 */
	public static boolean isDebug = true;
	// A good convention is to declare a TAG constant in your class:
	private static final String TAG = "Quinn";

	// subsequent calls to the log methods.
	public static void i(String msg) {
		if (isDebug) {
			Log.i(TAG, msg);
		}
	}

	public static void d(String msg) {
		if (isDebug) {
			Log.d(TAG, msg);
		}
	}

	public static void e(String msg) {
		if (isDebug) {
			Log.i(TAG, msg);
		}
	}

	public static void v(String msg) {
		if (isDebug) {
			Log.i(TAG, msg);
		}
	}
}
