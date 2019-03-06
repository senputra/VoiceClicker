package com.doodee.voiceclicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.doodee.voiceclicker.backend.NetworkServiceDiscovery.ServiceDiscoveryManager;

import java.util.Map;

/**
 * This manages the user profile and the available ports.
 */
public class ProfileManager implements ProfileManagerCallback {


    public final int AUDIO_PORT_MASK = 1;
    public final int INPUT_PORT_MASK = 2;
    private final String SHARED_PREFERENCES_TAG = "VOICE-CLICKER";
    private final String SERVICE_TYPE = "_vcHello._udp";
    private final String AUDIO_PORT_KEY = "vc_audio";
    private final String INPUT_PORT_KEY = "vc_input";
    public FragmentConnectionCallback fragmentConnectionCallback;
    Context context;
    private String deviceName;
    private String remoteDeviceName;
    private String remoteIPAddress;
    private int audioStreamPort;
    private int inputStreamPort;
    private int heartbeatPort;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private ServiceDiscoveryManager sdm;

    public ProfileManager(Context context, FragmentConnectionCallback fragmentConnectionCallback) {
        this.fragmentConnectionCallback = fragmentConnectionCallback;
        this.mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_TAG, Context.MODE_PRIVATE);
        loadPreferences();
        this.sdm = new ServiceDiscoveryManager(SERVICE_TYPE, context, this);
        this.mEditor = mSharedPreferences.edit();
    }

    public void updatePreferences() {
        mEditor.putString("Device-name", this.deviceName);
        mEditor.putString("Remote-Device-name", this.remoteDeviceName);

        mEditor.putInt("Audio-Stream-Port", this.audioStreamPort);
        mEditor.putInt("Input-Stream-Port", this.inputStreamPort);
        mEditor.putInt("Heartbeat-Stream-Port", this.heartbeatPort);
    }

    public void commit() {
        mEditor.commit();
    }

    private void loadPreferences() {
        if (mSharedPreferences == null) {
            DooLog.d("no saved files");
            return;
        }
        this.deviceName = mSharedPreferences.getString("Device-name", Build.DISPLAY);
        this.remoteDeviceName = mSharedPreferences.getString("Remote-Device-name", Build.DISPLAY);

        this.audioStreamPort = mSharedPreferences.getInt("Audio-Stream-Port", 0);
        this.inputStreamPort = mSharedPreferences.getInt("Input-Stream-Port", 0);
        this.heartbeatPort = mSharedPreferences.getInt("Heartbeat-Stream-Port", 0);

    }

    public void getNeighbour() {
        DooLog.d("nigga key");
        sdm.discoverServices();
    }

    /*
            GETTER METHODS
        */
    public int getAudioStreamPort() {
        return audioStreamPort;
    }

    /*
        SETTER METHODS
    */
    public void setAudioStreamPort(int audioStreamPort) {
        this.audioStreamPort = audioStreamPort;
    }

    public int getInputStreamPort() {
        return inputStreamPort;
    }

    public void setInputStreamPort(int inputStreamPort) {
        this.inputStreamPort = inputStreamPort;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getRemoteDeviceName() {
        return remoteDeviceName;
    }

    public void setRemoteDeviceName(String remoteDeviceName) {
        this.remoteDeviceName = remoteDeviceName;
    }

    public int getHeartbeatPort() {
        return heartbeatPort;
    }

    public void setHeartbeatPort(int heartbeatPort) {
        this.heartbeatPort = heartbeatPort;
    }

    public String getRemoteIPAddress() {
        return remoteIPAddress;
    }

    public void setRemoteIPAddress(String remoteIPAddress) {
        this.remoteIPAddress = remoteIPAddress;
    }

    @Override
    public void updatePorts(String remoteIPAddress, int port, Map<String, byte[]> map) {

        this.remoteIPAddress = remoteIPAddress;

        DooLog.d("PM CALLBACK: " + port);
        heartbeatPort = port;
        audioStreamPort = (map.get(AUDIO_PORT_KEY) == null) ? 0 : Integer.valueOf(new String(map.get(AUDIO_PORT_KEY)));
        inputStreamPort = (map.get(INPUT_PORT_KEY) == null) ? 0 : Integer.valueOf(new String(map.get(INPUT_PORT_KEY)));
        this.remoteIPAddress = remoteIPAddress;

        DooLog.d("AUDIO_PORT_KEY LOLOLOL:::: " + audioStreamPort);
        DooLog.d("INPUT_PORT_KEY LOLOLOL:::: " + inputStreamPort);


        fragmentConnectionCallback.updateUI();
    }

}

