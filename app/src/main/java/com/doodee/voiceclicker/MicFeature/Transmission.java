package com.doodee.voiceclicker.MicFeature;

public enum Transmission {
    INSTANCE;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");

    }

    public static native void startTransmission(String ipAddrs, int audioStreamPort);

    public static native void stopTransmission();

}
