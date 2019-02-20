package com.doodee.voiceclicker.MicFeatures;

public enum AudioEngine {

    INSTANCE;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    public static native void startEngine(String s);

    public static native void stopEngine();

}
