package com.doodee.voiceclicker.KeyboardMouseFeature;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.doodee.voiceclicker.DooLog;
import com.doodee.voiceclicker.R;
import com.doodee.voiceclicker.backend.JavaTransmission;
import com.doodee.voiceclicker.backend.NetworkPacket;

import java.util.Objects;

public class FragmentClicker extends Fragment {
    CustomKeyboardCaptureView mCustomKeyboardView;

    private JavaTransmission mJavaTransmission;
    private String TRANSMISSION_KEY = "transmissionObj";

    public static FragmentClicker newInstance(Bundle bundle) {
        FragmentClicker fragmentClicker = new FragmentClicker();
        fragmentClicker.setArguments(bundle);
        return fragmentClicker;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJavaTransmission = getArguments() != null ? (JavaTransmission) getArguments().getSerializable(TRANSMISSION_KEY) : null;
        if (mJavaTransmission == null) {
            DooLog.e("mJavaTransmission not transferred");
        }
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_clicker, container, false);
        view.setOnTouchListener(new MousePadListener(mJavaTransmission));

        Button btnKey = view.findViewById(R.id.btn_key);

        mCustomKeyboardView = view.findViewById(R.id.keyListener);
        btnKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard();
            }
        });

        Button btnLeft = view.findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendControl(0x25);
            }
        });
        Button btnRight = view.findViewById(R.id.btnRight);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendControl(0x27);
            }
        });

        return view;
    }

    private void showKeyboard() {
        mCustomKeyboardView.requestFocus();
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(mCustomKeyboardView.getWindowToken(), 0, 0);
        mCustomKeyboardView.setmJavaTransmission(mJavaTransmission);

    }

    private void sendControl(int data) {
        if (mJavaTransmission == null) {
            DooLog.d("Java Transmission is null");
            return;
        }
        mJavaTransmission.send(NetworkPacket.getPacket(NetworkPacket.INPUT_TYPE_KEYBOARD, NetworkPacket.KEYBOARD_ACTION_OTHERS, (byte) data));
    }

}
