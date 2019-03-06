package com.doodee.voiceclicker.backend.NetworkServiceDiscovery;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

import com.doodee.voiceclicker.DooLog;
import com.doodee.voiceclicker.ProfileManagerCallback;

public class ServiceDiscoveryManager {

    private ProfileManagerCallback profileManagerCallback;

    private NsdManager.DiscoveryListener discoveryListener;
    private NsdManager.ResolveListener resolveListener;
    private String SERVICE_TYPE;
    private NsdManager nsdManager;
    private NsdServiceInfo mDNSinfo;

    private boolean isDiscovering = false;
    private boolean isRestarting = false;


    public ServiceDiscoveryManager(String SERVICE_TYPE, Context context, ProfileManagerCallback profileManagerCallback) {
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        this.SERVICE_TYPE = SERVICE_TYPE;
        this.profileManagerCallback = profileManagerCallback;
    }

    public void discoverServices() {
        initializeDiscoveryListener();
        if (discoveryListener == null) {
            DooLog.d("discoveryListener is null");
            return;
        }

        if (isRestarting) {
            DooLog.d("It is restarting please wait");
        } else {
            if (isDiscovering) {
                DooLog.d("Discovery is ongoing. Going to restart discovery");
                nsdManager.stopServiceDiscovery(discoveryListener);
                isDiscovering = true;
                isRestarting = true;
            } else {
                DooLog.d("Start Discovery");
                nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
                isDiscovering = true;
            }

        }

    }

    private void initializeDiscoveryListener() {
        if (this.discoveryListener != null) {
            DooLog.d("Discovery listener is already initialized");
            return;
        }

        // Instantiate a new DiscoveryListener
        this.discoveryListener = new NsdManager.DiscoveryListener() {

            // Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                DooLog.d("Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                DooLog.d("Service discovery success " + service.getServiceName() + " " + service.getServiceType() + " " + service.getHost() + " " + service.getPort());
                nsdManager.resolveService(service, new NsdManager.ResolveListener() {
                    @Override
                    public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                        DooLog.d("not resolved " + errorCode);
                    }

                    @Override
                    public void onServiceResolved(NsdServiceInfo service) {
                        DooLog.d("Service discovery success (resolved) " + service.getServiceName() + " " + service.getServiceType() + " " + service.getHost() + " " + service.getPort());
                        profileManagerCallback.updatePorts(service.getHost().getHostAddress(), service.getPort(), service.getAttributes());
                    }
                });
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                DooLog.d("service lost: " + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                DooLog.d("Discovery stopped: " + serviceType);
                if (isRestarting) {
                    nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
                    isRestarting = false;
                }
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                DooLog.d("Discovery failed: Error code:" + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                DooLog.d("Discovery failed: Error code:" + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }
        };
    }

    public void tearDown() {
        if (isDiscovering) {
            nsdManager.stopServiceDiscovery(discoveryListener);
            DooLog.d("SDR Torn down");
            isDiscovering = false;
        } else {
            DooLog.d("SDR not discovering");
        }

    }

    public boolean isReady() {
        return (discoveryListener != null) && (resolveListener != null);
    }

}

