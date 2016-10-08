package com.liu.handbeauty.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.liu.handbeauty.R;
import com.liu.handbeauty.bean.Classify;
import com.liu.handbeauty.fragment.TestFragment;
import com.liu.handbeauty.system.SystemBean;
import com.liu.handbeauty.view.TabPageIndicator;

public class MainActivity extends AppCompatActivity {

    private ViewPager pager;

    private Classify testbean;
    private TabPageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        testbean= SystemBean.testbean;

        pager = (ViewPager)findViewById(R.id.pager);
        indicator = (TabPageIndicator)findViewById(R.id.indicator);
        initTab();
    }

    private void initTab() {
        FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }






    class GoogleMusicAdapter extends FragmentPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TestFragment.newInstance(testbean.getTngou().get(position).getId());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return testbean.getTngou().get(position).getName();
        }

        @Override
        public int getCount() {
            return testbean.getTngou().size();
        }
    }
}
