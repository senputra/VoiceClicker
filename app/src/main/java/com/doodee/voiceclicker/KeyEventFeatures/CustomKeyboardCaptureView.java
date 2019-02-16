package com.doodee.voiceclicker.KeyEventFeatures;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.doodee.voiceclicker.DooLog;
import com.doodee.voiceclicker.backend.JavaTransmission;
import com.doodee.voiceclicker.backend.NetworkPacket;

public class CustomKeyboardCaptureView extends View {

    public static final SparseIntArray SpecialKeysMap = new SparseIntArray();
    private JavaTransmission mJavaTransmission;

    static {
        int i = 0;
        SpecialKeysMap.put(KeyEvent.KEYCODE_DEL, ++i);              // 1
        SpecialKeysMap.put(KeyEvent.KEYCODE_TAB, ++i);              // 2
        SpecialKeysMap.put(KeyEvent.KEYCODE_ENTER, 12);
        ++i;        // 3 is not used, return is 12 instead
        SpecialKeysMap.put(KeyEvent.KEYCODE_DPAD_LEFT, ++i);        // 4
        SpecialKeysMap.put(KeyEvent.KEYCODE_DPAD_UP, ++i);          // 5
        SpecialKeysMap.put(KeyEvent.KEYCODE_DPAD_RIGHT, ++i);       // 6
        SpecialKeysMap.put(KeyEvent.KEYCODE_DPAD_DOWN, ++i);        // 7
        SpecialKeysMap.put(KeyEvent.KEYCODE_PAGE_UP, ++i);          // 8
        SpecialKeysMap.put(KeyEvent.KEYCODE_PAGE_DOWN, ++i);        // 9
        SpecialKeysMap.put(KeyEvent.KEYCODE_MOVE_HOME, ++i);    // 10
        SpecialKeysMap.put(KeyEvent.KEYCODE_MOVE_END, ++i);     // 11
        SpecialKeysMap.put(KeyEvent.KEYCODE_NUMPAD_ENTER, ++i); // 12
        SpecialKeysMap.put(KeyEvent.KEYCODE_FORWARD_DEL, ++i);  // 13
        SpecialKeysMap.put(KeyEvent.KEYCODE_ESCAPE, ++i);       // 14
        SpecialKeysMap.put(KeyEvent.KEYCODE_SYSRQ, ++i);        // 15
        SpecialKeysMap.put(KeyEvent.KEYCODE_SCROLL_LOCK, ++i);  // 16
        ++i;           // 17
        ++i;           // 18
        ++i;           // 19
        ++i;           // 20
        SpecialKeysMap.put(KeyEvent.KEYCODE_F1, ++i);           // 21
        SpecialKeysMap.put(KeyEvent.KEYCODE_F2, ++i);           // 22
        SpecialKeysMap.put(KeyEvent.KEYCODE_F3, ++i);           // 23
        SpecialKeysMap.put(KeyEvent.KEYCODE_F4, ++i);           // 24
        SpecialKeysMap.put(KeyEvent.KEYCODE_F5, ++i);           // 25
        SpecialKeysMap.put(KeyEvent.KEYCODE_F6, ++i);           // 26
        SpecialKeysMap.put(KeyEvent.KEYCODE_F7, ++i);           // 27
        SpecialKeysMap.put(KeyEvent.KEYCODE_F8, ++i);           // 28
        SpecialKeysMap.put(KeyEvent.KEYCODE_F9, ++i);           // 29
        SpecialKeysMap.put(KeyEvent.KEYCODE_F10, ++i);          // 30
        SpecialKeysMap.put(KeyEvent.KEYCODE_F11, ++i);          // 31
        SpecialKeysMap.put(KeyEvent.KEYCODE_F12, ++i);          // 21
    }

    public CustomKeyboardCaptureView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_FULLSCREEN;
        return new KeyInputConnection(this, true);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        DooLog.d("On key up :" + event.getKeyCode());
        return true;
    }

    public void sendChars(CharSequence c) {
        if (mJavaTransmission == null) {
            DooLog.d("Java Transmission is null");
            return;
        }
        mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_KEYBOARD, NetworkPacket.KEYBOARD_ACTION_KEYCHAR, (byte) c.charAt(0)));
        DooLog.d("Custom Keyboard Capture :" + c.charAt(0) + " byte" + (byte) c.charAt(0) + " length " + c.length());
    }

    public void setmJavaTransmission(JavaTransmission jt) {
        mJavaTransmission = jt;
    }
}
