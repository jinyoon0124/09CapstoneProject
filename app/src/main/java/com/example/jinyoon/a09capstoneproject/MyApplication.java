package com.example.jinyoon.a09capstoneproject;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Application subclass for Analytics
 */

public class MyApplication extends Application {
    //Get the tracker associated with this app
    public Tracker mTracker;
    public void startTracking(){
        if(mTracker==null){
            GoogleAnalytics ga = GoogleAnalytics.getInstance(this);
            mTracker=ga.newTracker(R.xml.track_app);
            ga.enableAutoActivityReports(this);
        }
    }
    public Tracker getTracker(){
        startTracking();
        return mTracker;
    }
}
