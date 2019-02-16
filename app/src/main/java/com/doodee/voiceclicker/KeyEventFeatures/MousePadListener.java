package com.doodee.voiceclicker.KeyEventFeatures;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.doodee.voiceclicker.DooLog;
import com.doodee.voiceclicker.backend.JavaTransmission;
import com.doodee.voiceclicker.backend.NetworkPacket;

public class MousePadListener implements View.OnTouchListener {

    private final Runnable mLongPressed = new Runnable() {
        public void run() {
            //mainConnection.sendData(mainConnection.udpPacketAdaptor(3,emptyData));
            DooLog.d("Long press!");
        }
    };
    //runnables for gesture
    private final Handler handler = new Handler();
    //variable to save extra data
    long lastFingerDownTime;
    long lastTapTime;
    boolean mustTwoFingerTap;
    int firstXAverage, firstYAverage;
    int fingerDownX, fingerDownY;
    boolean longPressActive;
    boolean needSingleTap = true;
    boolean isDoubleTapHold = false;
    boolean isScroll = false;
    int tapCount = 0;
    boolean isTwoFingerTap = false;
    //threshold
    int clickDistanceThreshold = 20; //in pixel
    //variables for the tap timeout
    int singleTapTimeout = 110;
    int longTapTimeout = 1000;
    int doubleTapTimeout = 50;
    int secondTapTimeout = 150;
    //empty byte array
    byte[] emptyData = {};
    private ClickType doubleTapAction, tripleTapAction;
    private JavaTransmission mJavaTransmission;
    final Runnable mSingleTap = new Runnable() {
        public void run() {
            mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_LEFT_CLICK));
            tapCount = 0;
            lastTapTime = System.currentTimeMillis();
            DooLog.d("Single Tap");
        }
    };
    private final Runnable mTwoFingerTap = new Runnable() {
        @Override
        public void run() {
            mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_RIGHT_CLICK));
            DooLog.d("two finger press!");
            isTwoFingerTap = true;
        }
    };
    private final Runnable mDoubleTapHold = new Runnable() {
        @Override
        public void run() {
            mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_LEFT_DOWN));
            isDoubleTapHold = true;
            DooLog.d("Double Tap hold!");
        }
    };
    private final Runnable mDoubleTapOff = new Runnable() {
        @Override
        public void run() {
            mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_LEFT_UP));
            isDoubleTapHold = false;
            DooLog.d("Double Tap off!");
        }
    };

    MousePadListener(String dta, String tta, JavaTransmission mjt) {
        doubleTapAction = ClickType.fromString(dta);
        tripleTapAction = ClickType.fromString(tta);
        mJavaTransmission = mjt;
    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v The view the touch event has been dispatched to.
     * @param e The MotionEvent object containing full information about
     *          the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent e) {
        int action = e.getActionMasked();
        try {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    //DooLog.d(TAG," tapcount: "+tapCount);
                    lastFingerDownTime = System.currentTimeMillis();
                    fingerDownX = (int) e.getX();
                    fingerDownY = (int) e.getY();
                    longPressActive = true;
                    handler.removeCallbacks(mSingleTap);

                    if (tapCount == 1) {
                        if (System.currentTimeMillis() - lastTapTime <= secondTapTimeout) {
                            handler.removeCallbacks(mSingleTap);
                            handler.removeCallbacks(mLongPressed);
                            mDoubleTapHold.run();
                            needSingleTap = false;
                            mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_MOVE, (byte) fingerDownX, (byte) fingerDownY));
                            break;
                        }
                    }

                    tapCount = 0;
                    needSingleTap = true;
                    handler.postDelayed(mLongPressed, longTapTimeout);

                    mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_MOVE, (byte) fingerDownX, (byte) fingerDownY));
                    //DooLog.d(TAG,"finger down");
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (e.getPointerCount() == 1) {
                        if (longPressActive) {
                            if (fingerDownX - e.getX() > clickDistanceThreshold || fingerDownX - e.getX() > clickDistanceThreshold) {
                                handler.removeCallbacks(mLongPressed);
                                lastFingerDownTime = 0;
                                longPressActive = false;
                            }
                        }
                        //long lastTime = System.currentTimeMillis();
                        mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_MOVE, (byte) fingerDownX, (byte) fingerDownY));
                        //DooLog.d("gaptime : " + (System.currentTimeMillis() - lastTime));
                        //DooLog.d(TAG,"X coor:"+e.getX()+" Y Coor:"+e.getY());
                    } else if (e.getPointerCount() == 2) {
                        int xAvearage = (int) (e.getX(0) + e.getX(1)) / 2;
                        int yAvearage = (int) (e.getY(0) + e.getY(1)) / 2;
                        if ((!isScroll) && (firstXAverage - xAvearage > clickDistanceThreshold || firstYAverage - yAvearage > clickDistanceThreshold
                                || firstXAverage - xAvearage < -clickDistanceThreshold || firstYAverage - yAvearage < -clickDistanceThreshold)) {
                            mustTwoFingerTap = false;
                            isScroll = true;
                            DooLog.d("scrolling");
                        }
                        mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_SCROLL, (byte) fingerDownX, (byte) fingerDownY));
                        //DooLog.d(TAG,e.getX(0)+"X"+e.getX(1) + " = "+((e.getX(0) + e.getX(1)) / 2));
                        //DooLog.d(TAG,e.getY(0)+"Y"+e.getY(1)+ " = "+((e.getY(0) + e.getY(1)) / 2));
                    }

                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    handler.removeCallbacks(mLongPressed);
                    firstXAverage = (int) (e.getX(0) + e.getX(1)) / 2;
                    firstYAverage = (int) (e.getY(0) + e.getY(1)) / 2;
                    mustTwoFingerTap = true;
                    isScroll = false;
                    mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_MOVE, (byte) -1, (byte) -1));
                    break;

                case MotionEvent.ACTION_UP:
                    if (isDoubleTapHold && (System.currentTimeMillis() - lastFingerDownTime <= doubleTapTimeout)) {
                        mDoubleTapOff.run();
                        mSingleTap.run();

                    } else if (isDoubleTapHold) {
                        mDoubleTapOff.run();
                    } else if (isTwoFingerTap) {
                        handler.removeCallbacks(mSingleTap);
                        isTwoFingerTap = false;
                    } else if (needSingleTap) {
                        if (System.currentTimeMillis() - lastFingerDownTime <= singleTapTimeout) {
                            tapCount = 1;
                            lastTapTime = System.currentTimeMillis();
                            handler.postDelayed(mSingleTap, 180);
                        }
                    } else if (mustTwoFingerTap) {
                        mTwoFingerTap.run();
                    }

                    lastFingerDownTime = 0;
                    handler.removeCallbacks(mLongPressed);
                    mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_MOVE, (byte) -1, (byte) -1));
                    break;


                case MotionEvent.ACTION_POINTER_UP:

                    break;

                case MotionEvent.ACTION_OUTSIDE:
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
        } catch (Exception er) {
            er.printStackTrace();
        }

        return true;
    }

    enum ClickType {
        NONE, MIDDLE, RIGHT;

        static ClickType fromString(String s) {
            switch (s) {
                case "right":
                    return RIGHT;
                case "middle":
                    return MIDDLE;
                default:
                    return NONE;
            }
        }
    }
}
