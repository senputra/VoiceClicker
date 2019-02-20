package com.doodee.voiceclicker;

public class DooLog {

    private static boolean DEBUG = true;

    private static String TAG = "VOICE-CLICKER-JAVA";

    public static void d(String message) {
        if (DEBUG) {
            android.util.Log.d(TAG, message);
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            android.util.Log.e(TAG, message);
        }
    }

    public static void w(String message) {
        if (DEBUG) {
            android.util.Log.w(TAG, message);
        }
    }

    public static void i(String message) {
        if (DEBUG) {
            android.util.Log.i(TAG, message);
        }
    }

    public static String getTAG() {
        return TAG;
    }

    public static void setTAG(String TAG) {
        DooLog.TAG = TAG;
    }
}
