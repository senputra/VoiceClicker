package com.doodee.voiceclicker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.doodee.voiceclicker.backend.JavaTransmission;

public class FragmentConnection extends Fragment {
    private JavaTransmission mJavaTransmission;
    private String TRANSMISSION_KEY = "transmissionObj";

    public static FragmentConnection newInstance(Bundle bundle) {
        FragmentConnection fragmentConnection = new FragmentConnection();
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


        View view = inflater.inflate(R.layout.tab_connection, container, false);
        Button btnConnect = view.findViewById(R.id.btnConnect);
        final EditText etIPAddrs = view.findViewById(R.id.etIPAddrs);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mJavaTransmission.getStatus() == JavaTransmission.NOT_READY) {
                    mJavaTransmission.connect(etIPAddrs.getText().toString(), 23789);
                }
            }
        });
        return view;
    }
}
