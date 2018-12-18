package com.doodee.voiceclicker;

public enum Transmission {
    INSTANCE;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public static native void startTransmission();

    public static native void stopTransmission();

}
