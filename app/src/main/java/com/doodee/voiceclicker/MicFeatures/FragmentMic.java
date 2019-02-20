package com.doodee.voiceclicker.MicFeatures;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.doodee.voiceclicker.DooLog;
import com.doodee.voiceclicker.R;
import com.doodee.voiceclicker.backend.JavaTransmission;

import java.util.Objects;

public class FragmentMic extends Fragment {
    ////FOR NATIVE PURPOSES
    TextView tv;
    Button btn;
    Button btnTransmit;
    boolean isEngineStarted = false;
    boolean isTransmissionOn = false;
    String ipAddrs;
    private JavaTransmission mJavaTransmission;
    private String TRANSMISSION_KEY = "transmissionObj";

    public static FragmentMic newInstance(Bundle bundle) {
        FragmentMic fragmentConnection = new FragmentMic();
        fragmentConnection.setArguments(bundle);
        return fragmentConnection;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_mic, container, false);
        btn = view.findViewById(R.id.btnAAudio);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEngine();
            }
        });

        btnTransmit = view.findViewById(R.id.btnTransmit);
        btnTransmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTransmission();
            }
        });

        tv = view.findViewById(R.id.txt_connection_status);
        setupNativeWorks();

        return view;
    }

    private void setupNativeWorks() {
        ipAddrs = mJavaTransmission.getIpAddrs();
    }

    void toggleEngine() {
        if (isEngineStarted) {
            AudioEngine.stopEngine();
            tv.setText("Recording Stream Stopped");
            btn.setText("Start Audio");
            isEngineStarted = false;
        } else {
            if (checkMyPermission()) {
                AudioEngine.startEngine(ipAddrs);
                tv.setText("Recording Stream started");
                btn.setText("Stop Audio");
                isEngineStarted = true;
            } else {
                showToastShort("Audio Engine not created");
            }
        }
    }

    void toggleTransmission() {
        if (isTransmissionOn) {
            Transmission.stopTransmission();
            tv.setText("Transmission Stopped");
            btnTransmit.setText("Start Transmission");
            isTransmissionOn = false;
        } else {
            if (checkMyPermission()) {
                Transmission.startTransmission(ipAddrs);
                tv.setText("Transmission started");
                btnTransmit.setText("Stop Transmission!");
                isTransmissionOn = true;
            } else {
                showToastShort("Transmission not started");
            }
        }
    }

    protected boolean checkMyPermission() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted


            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET},
                    3);

            //check if permission is granted
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                //not granted
                showToastShort("LOOOSERRR");
                return false;
            } else {
                //granted
                showToastShort("a lesser LOOOSERRR");
                return true;
            }


        } else {
            // Permission has already been granted
            showToastShort("Permission to use microphone is granted");
            return true;
        }

    }

    /**
     * to use toast in inner class such as onClickListener
     *
     * @param message :: the message pops by the toaster
     */
    protected void showToastShort(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
