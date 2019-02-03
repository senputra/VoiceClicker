package com.doodee.voiceclicker;

public class DooLog {

    private static String TAG = "VOICE-CLICKER-JAVA";

    public static void d(String message) {
        android.util.Log.d(TAG, message);
    }

    public static void e(String message) {
        android.util.Log.e(TAG, message);
    }

    public static void w(String message) {
        android.util.Log.w(TAG, message);
    }

    public static void i(String message) {
        android.util.Log.i(TAG, message);
    }

    public static String getTAG() {
        return TAG;
    }

    public static void setTAG(String TAG) {
        DooLog.TAG = TAG;
    }
}
