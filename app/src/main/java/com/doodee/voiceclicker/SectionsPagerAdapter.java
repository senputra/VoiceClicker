package com.doodee.voiceclicker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.doodee.voiceclicker.KeyboardMouseFeature.FragmentClicker;
import com.doodee.voiceclicker.MicFeature.FragmentMic;
import com.doodee.voiceclicker.backend.JavaTransmission;

/**
 * Section Pager Adapter
 * <p>
 * This adapter gives the ViewPager the needed fragment based on the position requested.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private JavaTransmission mJavaTransmission = null;
    private String TRANSMISSION_KEY = "transmissionObj";

    SectionsPagerAdapter(FragmentManager fm, JavaTransmission mJavaTransmission) {
        super(fm);
        this.mJavaTransmission = mJavaTransmission;
    }

    /**
     * @param position starts from 0
     * @return the fragment of a specific position
     */
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                DooLog.d("case 0");
                bundle.putSerializable(TRANSMISSION_KEY, this.mJavaTransmission);
                return FragmentConnection.newInstance(bundle);
            case 1:
                DooLog.d("case 1");
                bundle.putSerializable(TRANSMISSION_KEY, this.mJavaTransmission);
                return FragmentClicker.newInstance(bundle);
            case 2:
                DooLog.d("case 2");
                bundle.putSerializable(TRANSMISSION_KEY, this.mJavaTransmission);
                return FragmentMic.newInstance(bundle);
            default:
                return null;
        }
    }

    /**
     * the getCount() will be called to let ViewPager know the number of Fragments
     *
     * @return the number of page.
     */
    @Override
    public int getCount() {
        return 3;
    }

    /**
     * Returns the page title for the top indicator
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "HOME";
            case 1:
                return "CLICKER";
            case 2:
                return "MIC";
            default:
                return null;
        }
    }

}