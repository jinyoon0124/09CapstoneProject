package com.han.jinyoon.a09capstoneproject;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.han.jinyoon.a09capstoneproject.Notification.NotificationEventReceiver;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(savedInstanceState==null){
//            Log.e(LOG_TAG, "onCreate Called:: BUNDLE NULL");
//        }else{
//            Log.e(LOG_TAG, "onCreate Called:: BUNDLE NOT NULL");
//        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.drawable.ic_logo_toolbar);

        ((MyApplication) getApplication()).startTracking();
        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));

        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter adapter = new PageAdapter(this, manager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        viewPager.setAdapter(adapter);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        //Notification.. set up alarm only if the activity is not created from notification
        if(getIntent().getExtras()!=null){
            Bundle bundle = getIntent().getExtras();
            boolean cameFromNotification = bundle.getBoolean(this.getString(R.string.activity_from_notification_key));
            if(!cameFromNotification){
                NotificationEventReceiver.setupAlarm(this);
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
//        Log.e(LOG_TAG, "onResume Called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
