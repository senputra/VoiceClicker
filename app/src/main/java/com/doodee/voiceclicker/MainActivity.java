package com.doodee.voiceclicker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    // Static, Universal variable
    static String TAG = "VOICE-CLICKER";

    boolean isEngineStarted = false;
    boolean isTransmissionOn = false;
    TextView tv;
    Button btn;
    Button btnTransmit;
    Server mServer;
    EditText etIPAddrs;


    int MOUSE = 1;
    int KEYBOARD = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        tv = findViewById(R.id.sample_text);

        btn = findViewById(R.id.btnAAudio);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEngine();
            }
        });

        btnTransmit = findViewById(R.id.btnTransmit);
        btnTransmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTransmission();
            }
        });

        Button btnLeft = findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendControl(KEYBOARD, (int) 0x25)) Log.d(TAG, "arrow sent");
            }
        });
        Button btnRight = findViewById(R.id.btnRight);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendControl(KEYBOARD, (int) 0x27)) Log.d(TAG, "arrow sent");
            }
        });

        etIPAddrs = findViewById(R.id.etIPAddrs);
    }


    boolean sendControl(int type, int data) {
        byte[] buffer = new byte[]{(byte) type, (byte) (data)};
        if (mServer == null) {
            setupServer(etIPAddrs.getText().toString(), 5009);
            return false;
        } else {
            mServer.send(buffer);
            Log.d(TAG, "onClick: send Control successful");
        }
        return true;
    }

    void setupServer(String ipAddress, int port) {
        if (mServer == null) {
            mServer = new Server(ipAddress, port);
        }
    }

    void toggleEngine() {
        if (isEngineStarted) {
            AudioEngine.stopEngine();
            tv.setText("Recording Stream Stopped");
            btn.setText("Start Audio");
            isEngineStarted = false;
        } else {
            if (checkMyPermission()) {
                AudioEngine.startEngine();
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
                Transmission.startTransmission();
                tv.setText("Transmission started");
                btnTransmit.setText("Stop Transmission!");
                isTransmissionOn = true;
            } else {
                showToastShort("Transmission not started");
            }
        }
    }

    /**
     * get permission to use microphones
     *
     * @return true->permission granted ; false->otherwise
     */
    protected boolean checkMyPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted


            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET},
                    3);

            //check if permission is granted
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
