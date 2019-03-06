package com.doodee.voiceclicker;

import java.util.Map;

public interface ProfileManagerCallback {
    void updatePorts(String remoteIPAddress, int port, Map<String, byte[]> attributes);
}
