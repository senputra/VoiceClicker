package com.doodee.voiceclicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.doodee.voiceclicker.KeyEventFeatures.CustomKeyboardCaptureView;

import java.util.Objects;

public class FragmentClicker extends Fragment {
    CustomKeyboardCaptureView ckcView;

    int MOUSE = 1;
    int KEYBOARD = 2;
    int KEYBOARD_CHAR = 3;

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
        Button btnKey = view.findViewById(R.id.btn_key);
        final EditText et = view.findViewById(R.id.editText);
        et.setKeyListener(new KeyListener() {
            @Override
            public int getInputType() {
                return 0;
            }

            @Override
            public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
                DooLog.d(keyCode + " " + event.getDisplayLabel());
                sendControl(KEYBOARD_CHAR, event.getDisplayLabel());
                return false;
            }

            @Override
            public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {

                return false;
            }

            /**
             * This function will be called if an emoticon key is pressed.
             * The length of the character is 2 bytes, unlike usual char (1 byte)
             *
             * @param view
             * @param text
             * @param event
             * @return
             */
            @Override
            public boolean onKeyOther(View view, Editable text, KeyEvent event) {
                DooLog.d("asdsadsad" + " " + event.getCharacters().length());
                return false;
            }

            @Override
            public void clearMetaKeyState(View view, Editable content, int states) {

            }
        });

        ckcView = view.findViewById(R.id.keyListener);

        btnKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard();
            }
        });
        return view;
    }

    private void showKeyboard() {
        ckcView.requestFocus();
        InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(ckcView.getWindowToken(), 0, 0);

        @SuppressLint("ServiceCast") InputMethodService ims = (InputMethodService) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);


    }

    private void sendControl(int type, int data) {
        byte[] buffer = new byte[]{(byte) type, (byte) (data)};
        mJavaTransmission.send(buffer);
        DooLog.d("onClick: send Control successful");
    }
}
