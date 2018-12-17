package com.doodee.voiceclicker;

public enum AudioEngine {

    INSTANCE;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static native String stringFromJNI();

    public static native void startEngine();

    public static native void checkStat();

}
