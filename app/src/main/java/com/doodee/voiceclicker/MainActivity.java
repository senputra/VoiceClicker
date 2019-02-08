package com.doodee.voiceclicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Variables for the swipe views
    public SectionsPagerAdapter mSectionsPagerAdapter;
    public CustomViewPager mViewPager; //To host the content

    // Static, Universal variable
    static String TAG = "VOICE-CLICKER";

    boolean isEngineStarted = false;
    boolean isTransmissionOn = false;
    TextView tv;
    Button btn;
    Button btnTransmit;
    JavaTransmission mJavaTransmission;
    EditText etIPAddrs;


    int MOUSE = 1;
    int KEYBOARD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mJavaTransmission = new JavaTransmission();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mJavaTransmission);
        mViewPager = (CustomViewPager) findViewById(R.id.customViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        ////////////////////////////////////////////////////////////////////////////
//        etIPAddrs = findViewById(R.id.etIPAddrs);
//
//        // Example of a call to a native method
//        tv = findViewById(R.id.sample_text);
//
//        btn = findViewById(R.id.btnAAudio);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggleEngine();
//            }
//        });
//
//        btnTransmit = findViewById(R.id.btnTransmit);
//        btnTransmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toggleTransmission();
//            }
//        });
//
//        Button btnLeft = findViewById(R.id.btnLeft);
//        btnLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sendControl(KEYBOARD, (int) 0x25)) DooLog.d(TAG, "arrow sent");
//            }
//        });
//        Button btnRight = findViewById(R.id.btnRight);
//        btnRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (sendControl(KEYBOARD, (int) 0x27)) DooLog.d(TAG, "arrow sent");
//            }
//        });


    }

//    boolean sendControl(int type, int data) {
//        byte[] buffer = new byte[]{(byte) type, (byte) (data)};
//        if (mJavaTransmission == null) {
//            setupServer(etIPAddrs.getText().toString(), 5009);
//            return false;
//        } else {
//            mJavaTransmission.send(buffer);
//            DooLog.d(TAG, "onClick: send Control successful");
//        }
//        return true;
//    }
//
void setupServer(String ipAddress, int port) {
    if (mJavaTransmission == null) {
        mJavaTransmission = new JavaTransmission(ipAddress, port);
    }
}
//
//    void toggleEngine() {
//        if (isEngineStarted) {
//            AudioEngine.stopEngine();
//            tv.setText("Recording Stream Stopped");
//            btn.setText("Start Audio");
//            isEngineStarted = false;
//        } else {
//            if (checkMyPermission()) {
//                AudioEngine.startEngine(etIPAddrs.getText().toString());
//                tv.setText("Recording Stream started");
//                btn.setText("Stop Audio");
//                isEngineStarted = true;
//            } else {
//                showToastShort("Audio Engine not created");
//            }
//        }
//    }
//
//    void toggleTransmission() {
//        if (isTransmissionOn) {
//            Transmission.stopTransmission();
//            tv.setText("Transmission Stopped");
//            btnTransmit.setText("Start Transmission");
//            isTransmissionOn = false;
//        } else {
//            if (checkMyPermission()) {
//                Transmission.startTransmission(etIPAddrs.getText().toString());
//                tv.setText("Transmission started");
//                btnTransmit.setText("Stop Transmission!");
//                isTransmissionOn = true;
//            } else {
//                showToastShort("Transmission not started");
//            }
//        }
//    }
//
//    /**
//     * get permission to use microphones
//     *
//     * @return true->permission granted ; false->otherwise
//     */
//    protected boolean checkMyPermission() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//
//
//            // No explanation needed; request the permission
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET},
//                    3);
//
//            //check if permission is granted
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
//                    ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
//                //not granted
//                showToastShort("LOOOSERRR");
//                return false;
//            } else {
//                //granted
//                showToastShort("a lesser LOOOSERRR");
//                return true;
//            }
//
//
//        } else {
//            // Permission has already been granted
//            showToastShort("Permission to use microphone is granted");
//            return true;
//        }
//
//    }
//
//    /**
//     * to use toast in inner class such as onClickListener
//     *
//     * @param message :: the message pops by the toaster
//     */
//    protected void showToastShort(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }



}
