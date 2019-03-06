package com.doodee.voiceclicker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.doodee.voiceclicker.backend.JavaTransmission;
import com.doodee.voiceclicker.backend.NetworkServiceDiscovery.ServiceDiscoveryManager;
import com.doodee.voiceclicker.backend.NetworkServiceDiscovery.ServiceRegistrationManager;

public class FragmentConnection extends Fragment implements FragmentConnectionCallback {
    private JavaTransmission mJavaTransmission;
    private String TRANSMISSION_KEY = "transmissionObj";

    Button btnRefresh;
    TextView tvWelcome;
    private ServiceDiscoveryManager sdm;
    private ServiceRegistrationManager srm;
    private ProfileManager mProfileManager;

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
//
//      srm = new ServiceRegistrationManager("INPUT EVENT DOODEE","_nabe._tcp", getActivity());
        mProfileManager = new ProfileManager(getActivity(), this);
//      sdm = new ServiceDiscoveryManager("_VCAudio._udp",getActivity());
//      mProfileManager.getHeartbeatPort();


    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.tab_connection, container, false);
        btnRefresh = view.findViewById(R.id.btn_refresh);
        tvWelcome = view.findViewById(R.id.tv_welcome);

        Button btnConnect = view.findViewById(R.id.btn_connect);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfileManager.getNeighbour();
            }
        });
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProfileManager.getInputStreamPort() == 0 || mProfileManager.getAudioStreamPort() == 0) {
                    DooLog.d("PORT NOT READY");
                    return;
                }
                mJavaTransmission.setInputStreamPort(mProfileManager.getInputStreamPort());
                mJavaTransmission.setAudioStreamPort(mProfileManager.getAudioStreamPort());
            }
        });


        return view;
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        if(sdm.isReady()){
//            sdm.discoverServices();
//        }
//        if(srm.isReady()){
//            srm.registerService();
//        }
//    }
//
//    @Override
//    public void onPause(){
//        if(sdm.isReady()){
//            sdm.tearDown();
//        }
//        if(srm.isReady()){
//            srm.tearDown();
//        }
//        super.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        sdm.tearDown();
//        srm.tearDown();
//        sdm = null;
//        srm = null;
//        super.onDestroy();
//    }


    /**
     * Override for FragmentConnectoinCallback
     */
    @Override
    public void updateUI() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvWelcome.setText(mProfileManager.getHeartbeatPort() + "");
                mJavaTransmission.setIpAddrs(mProfileManager.getRemoteIPAddress());
                DooLog.d("UPDATE UI");
            }
        });

    }
}
