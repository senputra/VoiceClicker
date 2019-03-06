package com.doodee.voiceclicker.backend.NetworkServiceDiscovery;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

import com.doodee.voiceclicker.DooLog;

import java.net.ServerSocket;

public class ServiceRegistrationManager {

    private String SERVICE_NAME;
    private String SERVICE_TYPE;
    private ServerSocket mServerSocket;
    private NsdManager.RegistrationListener regristrationListener;
    private NsdManager nsdManager;

    public ServiceRegistrationManager(String SERVICE_NAME, String SERVICE_TYPE, Context context) {
        this.SERVICE_NAME = SERVICE_NAME;
        this.SERVICE_TYPE = SERVICE_TYPE;
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }

    public void registerService() {
        // Create NSDinfo object
        NsdServiceInfo mNSDinfo = new NsdServiceInfo();

        // Name can be changed if there is conflict.
        mNSDinfo.setServiceName(SERVICE_NAME);
        mNSDinfo.setServiceType(SERVICE_TYPE);

        try {
            mServerSocket = new ServerSocket(0);
        } catch (Exception e) {
            DooLog.d("MDNS server socket cant find new port");
        }
        mNSDinfo.setPort(mServerSocket.getLocalPort());

        nsdManager.registerService(mNSDinfo, NsdManager.PROTOCOL_DNS_SD, regristrationListener);
    }

    public void initializeRegistrationListener() {
        regristrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                DooLog.d("On registration failed");

            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                DooLog.d("On unregistration failed");

            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                SERVICE_NAME = serviceInfo.getServiceName();
                DooLog.d("On service registered " + SERVICE_NAME);
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                DooLog.d("On service unregistered");

            }
        };
    }

    public String getSERVICE_NAME() {
        return SERVICE_NAME;
    }

    public void setSERVICE_NAME(String SERVICE_NAME) {
        this.SERVICE_NAME = SERVICE_NAME;
    }

    public String getSERVICE_TYPE() {
        return SERVICE_TYPE;
    }

    public void setSERVICE_TYPE(String SERVICE_TYPE) {
        this.SERVICE_TYPE = SERVICE_TYPE;
    }

    public void tearDown() {
        nsdManager.unregisterService(regristrationListener);
        DooLog.d("SRM Torn down");
    }

    public boolean isReady() {
        return (regristrationListener != null);
    }
}
