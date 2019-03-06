package com.doodee.voiceclicker.backend;

import com.doodee.voiceclicker.DooLog;

import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class JavaTransmission implements Serializable {
    public static int NOT_READY = 1;
    public static int READY = 2;

    private String ipAddrs;
    private int inputStreamPort = 0;
    private int audioStreamPort = 0;
    private DatagramSocket datagramSocket;

    private int status = NOT_READY;

    private Runnable sendKeyRunnable;

//    public JavaTransmission(String ipAddrs) {
//        try {
//            this.datagramSocket = new DatagramSocket();
//            this.ipAddrs = ipAddrs;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public JavaTransmission() {
        try {
            this.datagramSocket = new DatagramSocket();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean send(byte buffer[]) {
        if (this.status == READY) {
            try {
                if (datagramSocket != null) {
                    final DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipAddrs), inputStreamPort);
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                datagramSocket.send(packet);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    Thread mThread = new Thread(runnable);
                    mThread.start();
                    //                    DooLog.d("send: SENT");
                } else {
                    DooLog.d("Socket not set");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            DooLog.d("Socket not READY");
            return false;
        }
    }

    private void checkStatus() {
        this.status = (inputStreamPort == 0 || audioStreamPort == 0) ? NOT_READY : READY;
    }

    public String getIpAddrs() {
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

