package com.doodee.voiceclicker;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class Server {
    static String TAG = "VOICE-CLICKER";
    private String ipAddrs;
    private int port;
    private DatagramSocket datagramSocket;

    Server(String ipAddrs, int port) {
        try {
            datagramSocket = new DatagramSocket();
            this.ipAddrs = ipAddrs;
            this.port = port;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean send(byte buffer[]) {
        try {
            if (datagramSocket != null) {
                final DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipAddrs), port);
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
                ;

                Log.d(TAG, "send: SENT");
            } else {
                Log.d(TAG, "Socket not set");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
