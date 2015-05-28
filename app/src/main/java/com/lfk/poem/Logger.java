package com.lfk.poem;

import android.util.Log;

public class Logger {

	public final static boolean MODEL_DEBUG = true;
	public final static boolean MODEL_LAUNCHER = false;

	private static boolean Model = MODEL_DEBUG;

	public Logger() {
	}

	/**
	 * 发布应用时，调用Logger.type(Logger.MODEL_LAUNCHER); 所有的Logger将不在打印
	 * 
	 * @param type
	 */
	public final static void type(boolean type) {
		Model = type;
	}

	public final static boolean getType() {
		return Model;
	}

	public final static void i(String tag, String msg) {
		if (Model) {
			Log.i(tag, msg);
		}
	}

	public final static void e(String tag, String msg) {
		if (Model) {
			Log.e(tag, msg);
		}
	}

	public final static void v(String tag, String msg) {
		if (Model) {
			Log.v(tag, msg);
		}
	}

	public final static void d(String tag, String msg) {
		if (Model) {
			Log.d(tag, msg);
		}
	}

	public final static void w(String tag, String msg) {
		if (Model) {
			Log.w(tag, msg);
		}
	}
}
