package com.doodee.voiceclicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.doodee.voiceclicker.backend.JavaTransmission;

public class MainActivity extends AppCompatActivity {

    // Variables for the swipe views
    public SectionsPagerAdapter mSectionsPagerAdapter;
    public CustomViewPager mViewPager; //To host the content
    JavaTransmission mJavaTransmission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mJavaTransmission = new JavaTransmission();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mJavaTransmission); //bundling the java server together

        mViewPager = findViewById(R.id.customViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setSwipeable(false);
    }
}
