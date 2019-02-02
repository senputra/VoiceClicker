package com.doodee.voiceclicker;

public enum Transmission {
    INSTANCE;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static native void startTransmission(String s);

    public static native void stopTransmission();

}
