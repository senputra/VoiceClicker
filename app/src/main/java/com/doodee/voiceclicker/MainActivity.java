package com.doodee.voiceclicker;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.three_dots_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        DooLog.d(menu.getTitle().toString() + " WOAHHH");

        switch (menu.getItemId()){
            case R.id.usefulLinks:
                break;
            case R.id.about:
                break;
            case R.id.quickStart:
                startActivity(new Intent(MainActivity.this, QuickStart.class));
                break;
        }
        return true;
    }

}
