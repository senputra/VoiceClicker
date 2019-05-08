package com.doodee.voiceclicker.backend;

import android.os.Parcelable;

import com.doodee.voiceclicker.DooLog;

import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class JavaTransmission implements Serializable {
    private static int NOT_READY = 1;
    private static int READY = 2;

    private String ipAddrs;
    private int inputStreamPort = 0;
    private int audioStreamPort = 0;
    private transient DatagramSocket datagramSocket;

    private int status = NOT_READY;

    private Runnable sendKeyRunnable;

    public JavaTransmission()  {
        try {
            this.datagramSocket = new DatagramSocket();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(byte buffer[]) {
        if (this.status == READY) {
            try {
                if (datagramSocket != null) {
                    final DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipAddrs), inputStreamPort);
                    new Thread(()->{
                        try {
                            datagramSocket.send(packet);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    DooLog.d("Socket not set");
//                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
//                return false;
            }
//            return true;
        } else {
            DooLog.d("Socket not READY");
//            return false;
        }
    }

    private void checkStatus() {
        this.status = (inputStreamPort == 0 || audioStreamPort == 0) ? NOT_READY : READY;
    }

    public String getIpAddress() {
        return this.ipAddrs;
    }

    public int getInputStreamPort() {
        return inputStreamPort;
    }

    public void setInputStreamPort(int inputStreamPort) {
        this.inputStreamPort = inputStreamPort;
        checkStatus();
    }

    public int getAudioStreamPort() {
        return audioStreamPort;
    }

    public void setAudioStreamPort(int audioStreamPort) {
        this.audioStreamPort = audioStreamPort;
        checkStatus();
    }

    public void setIpAddrs(String ipAddrs) {
        this.ipAddrs = ipAddrs;
    }
}

