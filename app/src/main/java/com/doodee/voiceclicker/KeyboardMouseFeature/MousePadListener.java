package com.doodee.voiceclicker.KeyboardMouseFeature;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.doodee.voiceclicker.DooLog;
import com.doodee.voiceclicker.backend.JavaTransmission;
import com.doodee.voiceclicker.backend.NetworkPacket;

public class MousePadListener implements View.OnTouchListener {


    //runnables for gesture
    private final Handler handler = new Handler();
    int firstXAverage, firstYAverage;
    int fingerDownX, fingerDownY;
    private final int TAP_TIMEOUT = 100;
    private final int CONTINUOUS_TAP_TIMEOUT = 200;
    private final Runnable mLeftClick = new Runnable() {
        public void run() {
            mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_LEFT_CLICK));
            DooLog.d("Left Click");
        }
    };
    private final Runnable mLeftDown = new Runnable() {
        public void run() {
            mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_LEFT_DOWN));
            DooLog.d("Left Down");
        }
    };
    private boolean isContinous = false;

    private JavaTransmission mJavaTransmission;
    private long mLastDownTime = 0;
    private boolean mIsGestureHandled;

    MousePadListener(JavaTransmission mjt) {
        mJavaTransmission = mjt;
    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();
        try {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mIsGestureHandled = false;
                    fingerDownX = (int) event.getX();
                    fingerDownY = (int) event.getY();
                    mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_MOVE, (byte) -1, (byte) -1, (byte) -1, (byte) -1)); //reset mouse coord
                    mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_MOVE, (byte) (fingerDownX >> 8), (byte) fingerDownX, (byte) (fingerDownY >> 8), (byte) fingerDownY));
                    //DooLog.d(TAG,"finger down");
                    if (event.getEventTime() - mLastDownTime <= CONTINUOUS_TAP_TIMEOUT) {
                        handler.postDelayed(mLeftDown, 10);
                        isContinous = true;
                    }
                    mLastDownTime = event.getEventTime();
                    break;

                case MotionEvent.ACTION_UP:
                    mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_MOVE, (byte) -1, (byte) -1, (byte) -1, (byte) -1)); //reset mouse coord
                    if (!mIsGestureHandled) {
                        if (isContinous) {
                            mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_LEFT_UP));
                            isContinous = false;
                            mIsGestureHandled = true;
                            break;
                        }

                        if (event.getEventTime() - mLastDownTime <= TAP_TIMEOUT) {
                            handler.postDelayed(mLeftClick, TAP_TIMEOUT);
                            mIsGestureHandled = true;
                            break;
                        }
                    }
                    break;


                case MotionEvent.ACTION_MOVE:
                    switch (event.getPointerCount()) {
                        case 1:
                            mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_MOVE, (byte) ((short) event.getX() >> 8), (byte) event.getX(), (byte) ((short) event.getY() >> 8), (byte) event.getY()));
                            break;
                        case 2:
                            int xAvearage = (int) (event.getX(0) + event.getX(1)) / 2;
                            int yAvearage = (int) (event.getY(0) + event.getY(1)) / 2;
                            DooLog.d("scrolling " + xAvearage + " " + yAvearage);
                            mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_SCROLL, (byte) ((xAvearage >> 8)), (byte) xAvearage, (byte) ((short) yAvearage >> 8), (byte) yAvearage));
                            break;
                        //Log.d(TAG,e.getX(0)+"X"+e.getX(1) + " = "+((e.getX(0) + e.getX(1)) / 2));
                        //Log.d(TAG,e.getY(0)+"Y"+e.getY(1)+ " = "+((e.getY(0) + e.getY(1)) / 2));
                    }
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    switch (event.getPointerCount()) {
                        case 2:
                            handler.removeCallbacks(mLeftClick, "");
                            firstXAverage = (int) (event.getX(0) + event.getX(1)) / 2;
                            firstYAverage = (int) (event.getY(0) + event.getY(1)) / 2;
                            mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_MOVE, (byte) -1, (byte) -1, (byte) -1, (byte) -1));
                            mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_SCROLL, (byte) ((firstXAverage >> 8)), (byte) firstXAverage, (byte) ((short) firstYAverage >> 8), (byte) firstYAverage));
                    }
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_SCROLL, (byte) -1, (byte) -1, (byte) -1, (byte) -1)); // Reset scrolling

                    int count = event.getPointerCount();
                    if (event.getEventTime() - mLastDownTime <= TAP_TIMEOUT) {
                        switch (count) {
                            case 2:
                                if (!mIsGestureHandled) {
                                    mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_RIGHT_CLICK));
                                    mIsGestureHandled = true;
                                }
                                break;
                            case 3:
                                if (!mIsGestureHandled) {
                                    mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_MOUSE, NetworkPacket.MOUSE_ACTION_MIDDLE_CLICK));
                                    mIsGestureHandled = true;
                                }
                                break;
                        }
                    }
                    break;
            }
        } catch (Exception er) {
            er.printStackTrace();
        }

        return true;
    }
}
