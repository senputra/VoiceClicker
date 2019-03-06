package com.doodee.voiceclicker.MicFeature;

public enum AudioEngine {

    INSTANCE;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    public static native void startEngine(String s, int port);

    public static native void stopEngine();

}
