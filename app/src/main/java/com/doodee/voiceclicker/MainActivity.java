package com.doodee.voiceclicker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    // Static, Universal variable
    static String TAG = "VOICE-CLICKER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(AudioEngine.stringFromJNI());

        Button btn = findViewById(R.id.btnAAudio);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMyPermission()) {
                    AudioEngine.startEngine();
                } else {
                    showToastShort("Audio Engine not created");
                }
            }
        });

        Button btnCheck = findViewById(R.id.btnCheckStat);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioEngine.checkStat();
            }
        });
    }

    /**
     * get permission to use microphones
     *
     * @return true->permission granted ; false->otherwise
     */
    protected boolean checkMyPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted


            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    3);

            //check if permission is granted
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
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
