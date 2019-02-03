package com.doodee.voiceclicker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * @param position starts from 0
     * @return the fragment of a specific position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DooLog.d("case 0");
                return FragmentConnection.newInstance("HOME");
            case 1:
                DooLog.d("case 1");
                return FragmentClicker.newInstance("CLICKER");
            default:
                return null;
        }
    }

    /**
     * the getCount() will be called to
     *
     * @return the number of page.
     */
    @Override
    public int getCount() {
        return 2;
    }

    /**
     * Returns the page title for the top indicator
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}