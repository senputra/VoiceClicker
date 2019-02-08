package com.doodee.voiceclicker;

import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class JavaTransmission implements Serializable {
    public static int NOT_READY = 1;
    public static int READY = 2;

    private String ipAddrs;
    private int port;
    private DatagramSocket datagramSocket;

    private int status = NOT_READY;

    JavaTransmission(String ipAddrs, int port) {
        try {
            this.datagramSocket = new DatagramSocket();
            this.ipAddrs = ipAddrs;
            this.port = port;

            this.status = READY;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    JavaTransmission() {
        this.status = NOT_READY;
    }

    boolean send(byte buffer[]) {
        if (this.status == READY) {
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
                    DooLog.d("send: SENT");
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
            DooLog.d("Socket not initiated");
            return false;
        }
    }

    boolean sendKey(byte buffer[]) {
        if (this.status == READY) {
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
                    DooLog.d("send: SENT");
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
            DooLog.d("Socket not initiated");
            return false;
        }
    }

    void connect(String ipAddrs, int port) {
        try {
            this.datagramSocket = new DatagramSocket();
            this.ipAddrs = ipAddrs;
            this.port = port;

            this.status = READY;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    int getStatus() {
        return this.status;
    }
}
